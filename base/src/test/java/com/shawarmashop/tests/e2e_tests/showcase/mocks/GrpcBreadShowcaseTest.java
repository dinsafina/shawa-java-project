package com.shawarmashop.tests.e2e_tests.showcase.mocks;

import com.shawarmashop.tests.auth.ApiClient;
import com.shawarmashop.tests.auth.Users;
import com.shawarmashop.tests.db.repository.OrderRepository;
import com.shawarmashop.tests.db.rows.OrderRow;
import com.shawarmashop.tests.dto.orders.CreateOrderRequest;
import com.shawarmashop.tests.dto.payments.IdempotencyKey;
import com.shawarmashop.tests.dto.orders.OrderResponse;
import com.shawarmashop.tests.dto.payments.PayRequest;
import com.shawarmashop.tests.junit.DbTest;
import com.shawarmashop.tests.junit.KafkaTest;
import com.shawarmashop.tests.junit.PartnersTest;
import com.shawarmashop.tests.kafka.KafkaTestBus;
import com.shawarmashop.tests.kafka.OrderEventsTopic;
import com.shawarmashop.tests.kafka.events.OrderEvent;
import com.shawarmashop.tests.partners.PartnerStubs;
import com.shawarmashop.tests.partners.bread_grpc.dto.BreadBakery;
import com.shawarmashop.tests.partners.bread_grpc.dto.BreadConfirmationDto;
import com.shawarmashop.tests.partners.bread_grpc.dto.BreadOrderDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@Tag("rest")
@PartnersTest
@KafkaTest
@DbTest
@DisplayName("Showcase: gRPC bread bakery — happy + UNAVAILABLE")
class GrpcBreadShowcaseTest {

    private static final int RECIPE_ID = 1;

    private final ApiClient owner = ApiClient.asUser(Users.OWNER);
    private final OrderRepository orderDb = new OrderRepository();


    @Test
    @DisplayName("happy: pay → пекарня подтверждает → бэк дёргает gRPC ровно один раз")
    void breadHappyPath(PartnerStubs stubs) {
        BreadConfirmationDto confirmation = BreadConfirmationDto.builder()
                .batchId("B-" + UUID.randomUUID().toString().substring(0, 8))
                .readyInSec(400)
                .pricePerUnit(2500)
                .totalPrice(25000)
                .bakery(BreadBakery.builder().branch("test-branch").masterChef("Showcase Chef").build())
                .build();
        stubs.bread().respondWith(confirmation);
        stubs.payments().responseSuccess("txn_bread_" + UUID.randomUUID());

        OrderResponse order = owner.orders().create(CreateOrderRequest.of(RECIPE_ID, 1, "CARD")).success();
        owner.payments().pay(order.getId(), new PayRequest("CARD"), IdempotencyKey.random()).assertStatus(202);

        orderDb.awaitStatus(order.getId(), "PREPARING", Duration.ofSeconds(15));

        // breadBatchId захвачен в строке заказа.
        assertThat(orderDb.findById(order.getId()).orElseThrow().getBreadBatchId())
                .isEqualTo(confirmation.getBatchId());

        // Бэк отправил пекарне ровно один gRPC-вызов.
        List<BreadOrderDto> calls = stubs.bread().recordedCalls();
        assertThat(calls).hasSize(1);
        assertThat(calls.get(0).getOrderRef()).isEqualTo("order-" + order.getId());
    }

    @Test
    @DisplayName(
            "UNAVAILABLE: /pay → 202, заказ остаётся PAID, BREAD_RESERVATION_FAILED публикуется в Kafka"
    )
    void breadUnavailable(PartnerStubs stubs, KafkaTestBus kafka) {
        OrderEventsTopic orderEvents = kafka.orders();

        stubs.bread().respondUnavailable();
        stubs.payments().responseSuccess("txn_bread_unavail_" + UUID.randomUUID());

        OrderResponse order = owner.orders()
                .create(CreateOrderRequest.of(RECIPE_ID, 1, "CARD"))
                .expect(201);

        // /pay отвечает за оплату, а не за хлеб.
        owner.payments()
                .pay(order.getId(), new PayRequest("CARD"), IdempotencyKey.random())
                .assertStatus(202);

        // Scheduler подождёт около 5 секунд,
        // попробует вызвать пекарню и опубликует событие.
        OrderEvent failedEvent = orderEvents.waitBreadReservationFailed(order.getId());

        assertThat(failedEvent.getOrderId()).isEqualTo(order.getId());
        assertThat(failedEvent.getReason()).contains("UNAVAILABLE");

        // После ошибки хлеба заказ остаётся оплаченным.
        OrderRow paidOrder = orderDb.findById(order.getId()).orElseThrow();

        assertThat(paidOrder.getStatus()).isEqualTo("PAID");
        assertThat(paidOrder.getBreadBatchId()).isNull();

        // Scheduler может сделать больше одной попытки,
        // поэтому нельзя проверять hasSize(1).
        assertThat(stubs.bread().recordedCalls())
                .anySatisfy(call ->
                        assertThat(call.getOrderRef())
                                .isEqualTo("order-" + order.getId())
                );
    }
}

