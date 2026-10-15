package com.shawarmashop.tests.e2e_tests;

import com.shawarmashop.tests.auth.ApiClient;
import com.shawarmashop.tests.auth.Users;
import com.shawarmashop.tests.dto.orders.CreateOrderRequest;
import com.shawarmashop.tests.dto.payments.IdempotencyKey;
import com.shawarmashop.tests.dto.payments.PayRequest;
import com.shawarmashop.tests.dto.orders.OrderResponse;
import com.shawarmashop.tests.junit.KafkaTest;
import com.shawarmashop.tests.kafka.KafkaTestBus;
import com.shawarmashop.tests.kafka.OrderEventsTopic;
import com.shawarmashop.tests.kafka.events.OrderEvent;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@KafkaTest
public class KafkaExamplesTest {

    private final ApiClient owner = ApiClient.asUser(Users.OWNER);

    @Test
    public void test1(KafkaTestBus kafkaTestBus){
        OrderEventsTopic ordersTopic = kafkaTestBus.orders();

        OrderResponse order = owner.orders()
                .create(CreateOrderRequest.of(1, 1, "CARD"))
                .success();

        OrderEvent orderEvent = ordersTopic.waitPlaced(order.getId());
        assertThat(orderEvent.getRecipeId()).isEqualTo(1);
        assertThat(orderEvent.getQty()).isEqualTo(1);
        assertThat(orderEvent.getTotalPrice()).isPositive();

        owner.payments()
                .pay(order.getId(), new PayRequest("CARD"), IdempotencyKey.random())
                .assertStatus(202);

        OrderEvent orderPaidEvent = ordersTopic.waitPaid(order.getId(), Duration.ofSeconds(30));
        assertThat(orderPaidEvent.getTxnId()).isNotBlank();
        assertThat(orderPaidEvent.getEtaAt()).isNotNull();
    }
}

