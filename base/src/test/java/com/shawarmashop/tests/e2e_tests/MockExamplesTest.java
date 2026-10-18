package com.shawarmashop.tests.e2e_tests;

import com.shawarmashop.tests.auth.ApiClient;
import com.shawarmashop.tests.auth.Users;
import com.shawarmashop.tests.dto.orders.CreateOrderRequest;
import com.shawarmashop.tests.dto.orders.OrderResponse;
import com.shawarmashop.tests.dto.payments.IdempotencyKey;
import com.shawarmashop.tests.dto.payments.PayRequest;
import com.shawarmashop.tests.junit.PartnersTest;
import com.shawarmashop.tests.partners.PartnerStubs;
import org.junit.jupiter.api.Test;

@PartnersTest
public class MockExamplesTest {

    private final ApiClient owner = ApiClient.asUser(Users.OWNER);

    @Test
    public void test1(PartnerStubs mocks) {
        //мок
        mocks.payments().responseFailed("No funds on card");

        //создание заказа -1й рец, 1 кол-во, карта
        OrderResponse order = owner.orders()
                .create(CreateOrderRequest.of(1, 1, "CARD"))
                .success();
        //пытаемся оплатить заказ
        owner.payments()
                .pay(order.getId(), new PayRequest("CARD"), IdempotencyKey.random())
                .assertStatus(402);
    }

    @Test
    public void test2(PartnerStubs mocks) {
        mocks.payments().responseSuccess("Успешная оплата");

        OrderResponse order = owner.orders()
                .create((CreateOrderRequest.of(1, 1, "CASH")))
                .success();

        owner.payments()
                .pay(order.getId(), new PayRequest("CARD"), IdempotencyKey.random())
                .assertStatus(202);
    }
}
