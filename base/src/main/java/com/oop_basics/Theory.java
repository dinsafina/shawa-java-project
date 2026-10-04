package com.oop_basics;

public class Theory {
    public static void main(String[] args) {

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(2);
        orderResponse.setStatus("NEW");

        System.out.println(orderResponse.getId());

        OrderResponse orderResponse1 = new OrderResponse(5, "DONE" , 99.99, 10);
        System.out.println(orderResponse.getId());

        orderResponse1.summary();

        orderResponse.addDiscount(300);
        System.out.println(orderResponse.getPrice());
    }
}
