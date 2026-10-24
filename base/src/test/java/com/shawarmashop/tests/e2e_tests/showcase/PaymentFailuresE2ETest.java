package com.shawarmashop.tests.e2e_tests.showcase;

import com.shawarmashop.tests.auth.ApiClient;
import com.shawarmashop.tests.auth.Users;
import com.shawarmashop.tests.db.TestDbScope;
import com.shawarmashop.tests.db.repository.PaymentRepository;
import com.shawarmashop.tests.db.rows.PaymentRow;
import com.shawarmashop.tests.dto.orders.CreateOrderRequest;
import com.shawarmashop.tests.dto.orders.OrderResponse;
import com.shawarmashop.tests.dto.payments.IdempotencyKey;
import com.shawarmashop.tests.dto.payments.PayRequest;
import com.shawarmashop.tests.dto.payments.PaymentResponse;
import com.shawarmashop.tests.dto.payments.WebhookRequest;
import com.shawarmashop.tests.junit.DbTest;
import com.shawarmashop.tests.junit.PartnersTest;
import com.shawarmashop.tests.partners.PartnerStubs;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@PartnersTest
@DbTest
@DisplayName("E2E: сбои оплаты")
class PaymentFailuresE2ETest {

    private static final int RECIPE_ID = 1;
    private static final Duration PAYMENT_STATUS_TIMEOUT = Duration.ofSeconds(5);

    private final ApiClient owner = ApiClient.asUser(Users.OWNER);
    private final PaymentRepository paymentDb = new PaymentRepository();

    @Test
    @DisplayName("declined → 402, платёж FAILED, заказ остаётся PENDING")
    void declinedPaymentIs402(PartnerStubs stubs) {
        String failureReason = "Insufficient funds";
        stubs.payments().respondFailed(failureReason);

        OrderResponse order = createOrder();
        IdempotencyKey idempotencyKey = IdempotencyKey.random();

        owner.payments()
                .pay(order.getId(), new PayRequest("CARD"), idempotencyKey)
                .assertStatus(402);

        PaymentRow payment = findAndTrackPayment(idempotencyKey);

        assertThat(payment.getStatus()).isEqualTo("FAILED");
        assertThat(payment.getFailureReason()).isEqualTo(failureReason);
        assertOrderPending(order.getId());
    }


    @Test
    @DisplayName("webhook FAILED переводит PENDING-платёж в FAILED")
    void webhookFailedKeepsOrderUnpaid(PartnerStubs stubs) {
        String txnId = "txn_" + UUID.randomUUID() + "cancel_paid";
        stubs.payments().respondPending(txnId);

        OrderResponse order = createOrder();
        IdempotencyKey idempotencyKey = IdempotencyKey.random();

        PaymentResponse payment = owner.payments()
                .pay(order.getId(), new PayRequest("CARD"), idempotencyKey)
                .expect(202);

        trackPayment(payment.getId());

        assertOrderPending(order.getId());

        WebhookRequest body = new WebhookRequest(txnId, "FAILED", Instant.now(), null);
        owner.payments()
                .sendWebhook(body)
                .assertStatus(204);

        Awaitility.await("платёж txnId=" + txnId + " переходит в FAILED")
                .atMost(PAYMENT_STATUS_TIMEOUT)
                .pollInterval(Duration.ofMillis(250))
                .untilAsserted(() -> {
                    PaymentRow actual = paymentDb
                            .findByTxnId(txnId)
                            .orElseThrow();

                    assertThat(actual.getStatus()).isEqualTo("FAILED");
                });

        assertOrderPending(order.getId());
    }

    @Test
    @DisplayName("повтор с тем же ключом не создаёт второй платёж")
    void declinedReplayIsIdempotent(PartnerStubs stubs) {
        stubs.payments().respondFailed("Card blocked");

        OrderResponse order = createOrder();
        IdempotencyKey idempotencyKey = IdempotencyKey.random();

        owner.payments()
                .pay(order.getId(), new PayRequest("CARD"), idempotencyKey)
                .assertStatus(402);

        PaymentRow originalPayment = findAndTrackPayment(idempotencyKey);

        owner.payments()
                .pay(order.getId(), new PayRequest("CARD"), idempotencyKey)
                .assertStatus(402);

        assertThat(paymentDb.findByOrderId(order.getId()))
                .singleElement()
                .satisfies(payment -> {
                    assertThat(payment.getId()).isEqualTo(originalPayment.getId());
                    assertThat(payment.getIdempotencyKey()).isEqualTo(idempotencyKey.getValue());
                    assertThat(payment.getStatus()).isEqualTo("FAILED");
                });
    }

    @Test
    @DisplayName("тот же Idempotency-Key для другого заказа → 409")
    void idempotencyKeyIsScopedToOrder(PartnerStubs stubs) {
        String txnId = "txn_" + UUID.randomUUID() + "scope";
        stubs.payments().responseSuccess(txnId);

        OrderResponse firstOrder = createOrder();
        OrderResponse secondOrder = createOrder();

        IdempotencyKey idempotencyKey = IdempotencyKey.random();

        PaymentResponse payment = owner.payments()
                .pay(firstOrder.getId(), new PayRequest("CARD"), idempotencyKey)
                .expect(202);

        trackPayment(payment.getId());

        owner.payments()
                .pay(secondOrder.getId(), new PayRequest("CARD"), idempotencyKey)
                .assertStatus(409);

        assertOrderPending(secondOrder.getId());

        assertThat(paymentDb.findByOrderId(firstOrder.getId()))
                .singleElement()
                .satisfies(actual -> {
                    assertThat(actual.getIdempotencyKey()).isEqualTo(idempotencyKey.getValue());
                    assertThat(actual.getTxnId()).isEqualTo(txnId);
                });

        assertThat(paymentDb.findByOrderId(secondOrder.getId())).isEmpty();
    }

    private OrderResponse createOrder() {
        CreateOrderRequest body = CreateOrderRequest.of(RECIPE_ID, 1, "CARD");

        OrderResponse order = owner.orders()
                .create(body)
                .success();

        TestDbScope.current().track("orders", order.getId());

        return order;
    }

    private PaymentRow findAndTrackPayment(IdempotencyKey idempotencyKey) {
        PaymentRow payment = paymentDb
                .findByIdempotencyKey(idempotencyKey.getValue())
                .orElseThrow();

        trackPayment(payment.getId());

        return payment;
    }

    private static void trackPayment(long paymentId) {
        TestDbScope.current().track("payments", paymentId);
    }

    private void assertOrderPending(int orderId) {
        String status = owner.orders()
                .get(orderId)
                .success()
                .getStatus();

        assertThat(status).isEqualTo("PENDING");
    }
}

