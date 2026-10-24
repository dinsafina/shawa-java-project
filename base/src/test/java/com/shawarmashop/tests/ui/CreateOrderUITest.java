package com.shawarmashop.tests.ui;

import com.codeborne.selenide.Condition;
import com.shawarmashop.tests.db.TestDbScope;
import com.shawarmashop.tests.dto.orders.OrderResponse;
import com.shawarmashop.tests.junit.DbTest;
import com.shawarmashop.tests.ui.components.OrderModal;
import com.shawarmashop.tests.ui.junit.UseSelenideProxy;
import com.shawarmashop.tests.ui.model.OrderUiStatus;
import com.shawarmashop.tests.ui.pages.MenuPage;
import com.shawarmashop.tests.ui.pages.OrdersPage;
import io.netty.handler.codec.http.HttpMethod;
import org.junit.jupiter.api.Test;

@DbTest
public class CreateOrderUITest extends BaseUiTest {

    private final OrdersPage ordersPage = new OrdersPage();
    private final MenuPage menuPage = new MenuPage();

    /** E2E-сценарий: создание заказа через UI, перехват ответа бэкенда
     * для получения ID и проверка статуса заказа на странице. */
    @Test
    @UseSelenideProxy
    public void test1() {
        String recipeName = "BBQ Курица";
        defaultAuth();
        OrderModal orderModal = menuPage.openMenu()
                .startOrderFor(recipeName);
        OrderResponse orderResponse = interceptResponse(HttpMethod.POST,
                "/api/v1/orders",
                OrderResponse.class,
                orderModal::submit);

        TestDbScope.current().track("orders", orderResponse.getId());
        ordersPage.openOrders()
                .cardById(orderResponse.getId())
                .shouldHaveStatus(OrderUiStatus.PENDING)
                .getRecipeName().shouldHave(Condition.partialText(recipeName));
    }
}
