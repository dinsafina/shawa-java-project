package com.lambdas_streams;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupExamples {

    public static void main(String[] args) {

        List<Order> orders = List.of(
                new Order(1L, "CREATED", 150.0, "Alice"),
                new Order(2L, "PAID", 320.99, "Bob"),
                new Order(1L, "CANCELLED", 89.76, "Alice"),
                new Order(1L, "PAID", 2100.0, "Charlie"),
                new Order(1L, "PAID", 548.90, "Bob")
        );

        Map<String, List<Order>> grouping = orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer));
        System.out.println(grouping);

        Map<String, Long> keyValueGrouping = orders.stream()
                .collect(Collectors.groupingBy(x -> x.getCustomer(), Collectors.counting()));
        System.out.println(keyValueGrouping);

    }
}
