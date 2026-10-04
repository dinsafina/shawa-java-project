package com.base.allure;

import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class AllureHelloTest {

    @Test
    public void test1() {
        loginAs("hello225@gmail.com");
        int i = placeOrder(4, 2);
        Assertions.assertThat(i).isGreaterThan(100);
    }


    @Step("Авторизация под {0}")
    public void loginAs(String email) {
//
    }

    @Step("Создаем заказ с рецептом {0} и количеством {1}")
    public int placeOrder(int receiptId, int quantity) {
        return 10;
    }

}
