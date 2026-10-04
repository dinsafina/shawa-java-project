package com.Homeworks.Homework5;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderAnalytics {

    static List<OrderRow> paidByCustomer(
            List<OrderRow> orders,
            String customer) {
        return orders.stream()
                .filter(x -> x.getCustomer().equals(customer))
                .filter(x -> x.getStatus().equals(OrderStatus.PAID))
                .toList();
    }

    //("Общая выручка")
    static double revenue(List<OrderRow> orders) {
        return orders.stream()
                .filter(x -> !x.getStatus().equals(OrderStatus.CANCELLED))
                .mapToDouble(OrderRow::getTotal)
                .sum();
    }

   //("Количество заказов по статусам")
    static Map<OrderStatus, Long> countByStatus(List<OrderRow> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(x -> x.getStatus(), Collectors.counting()));
    }

    //("Выручка по способам оплаты")
    static Map<PaymentChoice, Double> revenueByPayment(List<OrderRow> orders) {
        return orders.stream()
                .filter(x -> !x.getStatus().equals(OrderStatus.CANCELLED))
                .collect(Collectors.groupingBy(x -> x.getPayment(),
                        Collectors.summingDouble(OrderRow::getTotal)));
    }

   //("Пагинация")
    static Page<OrderRow> page(
            List<OrderRow> orders,
            int pageNum,
            int pageSize) {
        int skipCount = Math.max(0, (pageNum - 1) * pageSize);
        List<OrderRow> items = orders.stream()
                .skip(skipCount)
                .limit(pageSize)
                .toList();
        return new Page<>(
                items,
                pageNum,
                pageSize,
                orders.size()
        );
    }
}
