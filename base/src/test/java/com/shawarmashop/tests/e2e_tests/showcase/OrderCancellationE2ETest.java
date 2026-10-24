package com.shawarmashop.tests.e2e_tests.showcase;

import com.shawarmashop.tests.auth.ApiClient;
import com.shawarmashop.tests.auth.Users;
import com.shawarmashop.tests.db.TestDbScope;
import com.shawarmashop.tests.db.repository.OrderRepository;
import com.shawarmashop.tests.dto.orders.CreateOrderRequest;
import com.shawarmashop.tests.dto.orders.OrderResponse;
import com.shawarmashop.tests.dto.payments.IdempotencyKey;
import com.shawarmashop.tests.dto.payments.PayRequest;
import com.shawarmashop.tests.dto.payments.WebhookRequest;
import com.shawarmashop.tests.junit.DbTest;
import com.shawarmashop.tests.junit.KafkaTest;
import com.shawarmashop.tests.junit.PartnersTest;
import com.shawarmashop.tests.kafka.KafkaTestBus;
import com.shawarmashop.tests.kafka.OrderEventsTopic;
import com.shawarmashop.tests.kafka.events.OrderEvent;
import com.shawarmashop.tests.partners.PartnerStubs;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@PartnersTest
@KafkaTest
@DbTest
@DisplayName("E2E: отмена заказа")
class OrderCancellationE2ETest {

    private static final int RECIPE_ID = 1;
    private static final Duration PAID_STATUS_TIMEOUT = Duration.ofSeconds(3);

    private final ApiClient owner = ApiClient.asUser(Users.OWNER);
    private final OrderRepository orderDb = new OrderRepository();

    @Test
    @DisplayName("PENDING → CANCELLED + ORDER_CANCELLED")
    void cancelFromPending(KafkaTestBus kafka) {
        OrderEventsTopic orderEvents = kafka.orders();
        OrderResponse order = createOrder();

        OrderResponse cancelled = owner.orders()
                .cancel(order.getId())
                .expect(200);

        assertThat(cancelled.getStatus()).isEqualTo("CANCELLED");

        OrderEvent cancelledOrderEvent = orderEvents.waitCancelled(order.getId());

        assertCancelledEvent(cancelledOrderEvent, order.getId());
    }

    @Test
    @DisplayName("PAID → CANCELLED + ORDER_CANCELLED")
    void cancelFromPaid(PartnerStubs stubs, KafkaTestBus kafka) {
        OrderEventsTopic orderEvents = kafka.orders();

        String txnId = "txn_"+ UUID.randomUUID() + "cancel_paid";
        stubs.payments().respondPending(txnId);

        OrderResponse order = createOrder();

        owner.payments()
                .pay(order.getId(), new PayRequest("CARD"), IdempotencyKey.random())
                .expect(202);

        moveToPaid(order.getId(), txnId);

        OrderResponse cancelled = owner.orders()
                .cancel(order.getId())
                .expect(200);

        assertThat(cancelled.getStatus()).isEqualTo("CANCELLED");

        OrderEvent cancelledOrderEvent = orderEvents.waitCancelled(order.getId());

        assertCancelledEvent(cancelledOrderEvent, order.getId());
    }

    @Test
    @DisplayName("повторная отмена CANCELLED-заказа возвращает 400")
    void cancelTwiceIs400() {
        OrderResponse order = createOrder();

        owner.orders()
                .cancel(order.getId())
                .expect(200);

        owner.orders()
                .cancel(order.getId())
                .assertStatus(400);
    }

    private OrderResponse createOrder() {
        CreateOrderRequest body = CreateOrderRequest.of(RECIPE_ID, 1, "CARD");

        OrderResponse response = owner.orders()
                .create(body)
                .success();

        TestDbScope.current().track("orders", response.getId());

        return response;
    }

    private void moveToPaid(long orderId, String txnId) {
        WebhookRequest body = new WebhookRequest(txnId, "SUCCEEDED", Instant.now(), null);

        owner.payments()
                .sendWebhook(body)
                .assertStatus(204);

        orderDb.awaitStatus(orderId, "PAID", PAID_STATUS_TIMEOUT);
    }

    private static void assertCancelledEvent(OrderEvent event, long orderId) {
        assertThat(event.getOrderId()).isEqualTo(orderId);
        assertThat(event.getReason()).isEqualTo("user-cancelled");
    }
}
