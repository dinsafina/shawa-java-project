package com.enums;

import java.util.Arrays;

public class Theory {
    public static void main(String[] args) {
        OrderStatus orderStatus = OrderStatus.PAID;
        if (orderStatus == OrderStatus.PAID) {
            System.out.println("Заказ оплачен");
        }

        OrderStatus s  = OrderStatus.PREPARING;
        String hint = switch (s) {
            case CREATED, AWAITING_PAYMENT -> "ждем оплату";
            case PAID, PREPARING -> "готовим";
            case READY, COMPLETED -> "можно выдавать";
            case CANCELLED -> "отменен";
        };

        System.out.println(hint);

        PaymentChoice crypto = PaymentChoice.CRYPTO;
       // crypto.isOnline();
        System.out.println(crypto.getProvider());

        System.out.println(Arrays.toString(PaymentChoice.values()));
        String name = OrderStatus.PAID.name();
        System.out.println(name);

        PaymentChoice crypto1 = PaymentChoice.valueOf("CRYPTO");
        System.out.println(crypto1);


    }
}
