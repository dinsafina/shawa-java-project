package com.Homeworks.Homework5;

import java.util.List;

public class Homework5 {
    public static void main(String[] args) {
        List<OrderRow> orders = List.of(
                new OrderRow(
                        1,
                        "Alice",
                        OrderStatus.PAID,
                        PaymentChoice.CASH,
                        320.14
                ),
                new OrderRow(
                        2,
                        "Mike",
                        OrderStatus.CREATED,
                        PaymentChoice.CRYPTO,
                        780.00
                ),
                new OrderRow(
                        3,
                        "Daniel",
                        OrderStatus.PREPARING,
                        PaymentChoice.CARD,
                        440.01
                ),
                new OrderRow(
                        4,
                        "Alex",
                        OrderStatus.COMPLETED,
                        PaymentChoice.CASH,
                        510.12
                ),
                new OrderRow(
                        5,
                        "Olga",
                        OrderStatus.PREPARING,
                        PaymentChoice.CRYPTO,
                        321.1
                ),
                new OrderRow(
                        6,
                        "Kate",
                        OrderStatus.READY,
                        PaymentChoice.CARD,
                        363.7
                ),
                new OrderRow(
                        7,
                        "Mark",
                        OrderStatus.CANCELLED,
                        PaymentChoice.CRYPTO,
                        489.13
                ),
                new OrderRow(
                        8,
                        "John",
                        OrderStatus.PAID,
                        PaymentChoice.CASH,
                        345.01
                ),
                new OrderRow(
                        9,
                        "Courtney",
                        OrderStatus.READY,
                        PaymentChoice.CRYPTO,
                        750.02
                ),
                new OrderRow(
                        10,
                        "Ashley",
                        OrderStatus.READY,
                        PaymentChoice.CARD,
                        621.55
                )
        );

        OrderAnalytics
                .paidByCustomer(orders, "Alice")
                .forEach(System.out::println);

        System.out.println("Общая выручка: " +
                OrderAnalytics.revenue(orders)
        );

        System.out.println("Количество заказов по статусам: " +
                OrderAnalytics.countByStatus(orders)
        );

        System.out.println("Выручка по способам оплаты: " +
                OrderAnalytics.revenueByPayment(orders)
        );

        System.out.println("Пагинация:" +
                OrderAnalytics.page(orders, 2, 4)
        );
    }
}
