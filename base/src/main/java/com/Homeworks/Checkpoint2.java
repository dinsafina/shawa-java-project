package com.Homeworks;

import java.util.*;

public class Checkpoint2 {

    // 1. menuLine
    static String menuLine(String dish, int price) {
        return dish + " — " + price + " руб";
    }

    // 2. orderTotal
    static int orderTotal(List<Integer> prices) {
        int totalPrice = 0;
        for (int elemPrice : prices) {
            totalPrice += elemPrice;
        }
        return totalPrice;
    }

    // 3. availableDishes
    static List<String> availableDishes(List<String> menu, Set<String> soldOut) {
        List<String> availableDishes = new ArrayList<>();
        for (String elemMenu : menu) {
            if (!soldOut.contains(elemMenu)) {
                availableDishes.add(elemMenu);
            }
        }
        return availableDishes;
    }

    // 4. countOrders
    static Map<String, Integer> countOrders(List<String> orders) {
        Map<String, Integer> countOrders = new HashMap<>();
        for(String order : orders) {
            if(!(countOrders.containsKey(order))) {
                countOrders.put(order, 1);
            } else {
                int valuePlusOne = (countOrders.get(order) + 1);
                countOrders.put(order, valuePlusOne);
            }
           // countOrders.put(order, countOrders.getOrDefault(order, 0) + 1);
        }
        return countOrders;
}
    // 5. joinDishes
    static String joinDishes(List<String> dishes) {
        if (dishes.isEmpty()) return "";
        return String.join(", ", dishes);
    }

    // 6. isOnMenu
    static boolean isOnMenu(List<String> menu, String dish) {
        boolean isContains = false;
        for(String elemMenu: menu) {
          isContains = elemMenu.equalsIgnoreCase(dish);
        }
        return isContains;
    }


    public static void main(String[] args) {
        System.out.println(menuLine("Шаурма классическая", 250)); // Шаурма классическая — 250 руб
        System.out.println(orderTotal(List.of(250, 90, 250)));    // 590
        System.out.println(availableDishes(
                List.of("Шаурма", "Фалафель", "Кола"),
                Set.of("Фалафель")));                             // [Шаурма, Кола]
        System.out.println(countOrders(List.of("Шаурма", "Кола", "Шаурма"))); // {Шаурма=2, Кола=1}
        System.out.println(joinDishes(List.of("Шаурма", "Фалафель", "Кола")));// Шаурма, Фалафель, Кола
        System.out.println(isOnMenu(List.of("Шаурма", "Кола"), "кола"));      // true
    }
}