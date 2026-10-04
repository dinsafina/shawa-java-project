package com.lambdas_streams;

import com.Homeworks.Homework5.OrderRow;
import com.Homeworks.Homework5.OrderStatus;
import com.Homeworks.Homework5.PaymentChoice;
import com.exceptions.OrderNotFountException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Theory {

    public static void main(String[] args) {

        Predicate<String> isLong = x -> x.length() > 5;
        System.out.println(isLong.test("Hi"));
        System.out.println(isLong.test("Guten Tag!"));

        Function<String, Integer> length = x -> x.length();
        System.out.println(length.apply("Hello"));

        List<String> pizzas = List.of("Margarita", "Pepperoni", "Hawaii", "Carbonara");
        List<String> list = pizzas.stream()
                .filter(x -> x.length() > 7)
                .toList();
        System.out.println(list);

        List<Integer> collect = pizzas.stream()
                .map(x -> x.length())
                .collect(Collectors.toList());
        System.out.println(collect);

        Optional<String> h = pizzas.stream()
                .filter(x -> x.startsWith("H"))
                .findFirst();

        if (h.isEmpty()) {
            //System.out.println("Нет значения, возьми другое");
            //String s = h.orElseGet(() -> "1000 Островов");
           // System.out.println(s);
            h.orElseThrow(() -> new OrderNotFountException(1));
        }
       // System.out.println(h);
       //System.out.println(h.get());

        List<Order> orders = List.of(
                new Order(1L, "CREATED", 150.0, "Alice"),
                new Order(2L, "PAID", 320.99, "Bob"),
                new Order(1L, "CANCELLED", 89.76, "Alice"),
                new Order(1L, "PAID", 2100.0, "Charlie"),
                new Order(1L, "PAID", 548.90, "Bob")
        );

        List<Order> paid = orders.stream()
                .filter(x -> x.getStatus().equals("PAID"))
                .toList();
        System.out.println(paid);

        List<String> cancelled = orders.stream()
                .filter(x -> x.getStatus().equals("CANCELLED"))
                .map(x -> x.getCustomer()).toList();
        System.out.println(cancelled);

        /// //////
        List<OrderRow> orderRows = List.of(
                new OrderRow(1L, "Mike", OrderStatus.PAID, PaymentChoice.CARD, 320.99),
                new OrderRow(1L, "Alice", OrderStatus.PAID, PaymentChoice.CARD, 320.99),
                new OrderRow(1L, "Alice", OrderStatus.PAID, PaymentChoice.CARD, 320.99)
        );

        List<OrderRow> list1 = orderRows.stream()
                .filter(x -> x.getStatus().equals("PAID"))
               // .filter(x -> x.getCustomer().equals("Alice"))
                .toList();
        System.out.println("result " + list1);
    }
}
