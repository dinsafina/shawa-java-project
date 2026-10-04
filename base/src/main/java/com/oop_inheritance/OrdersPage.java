package com.oop_inheritance;

public class OrdersPage extends BasePage{

    protected OrdersPage(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public String url() {
        return baseUrl +  "/orders";
    }

    @Override
    public String readySelector() {
        return "[data-test-id=order-list]";
    }
}
