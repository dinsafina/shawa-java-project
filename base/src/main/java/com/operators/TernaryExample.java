package com.operators;

public class TernaryExample {
    public static void main(String[] args) {
        String env = "PROD";
        String baseUrl = env.equals("PROD1") ? "google.com" : "yandex.ru";
        //System.out.println(baseUrl);

//        [ORDER]   SHAWARMA_XL x2
//        [PRICE]   640 -> 608 (-5%)
//        [BATCH]   small batch
//        [KITCHEN] grill 7 min
//        [CLOSE]   18 min left

        // Входные данные
        String dish = "SHAWARMA_XL";
        int qty = 2;
        int minutesToClose = 18;

        // 1. unitPrice
        int unitPrice = switch (dish) {
            case "SHAWARMA_CLASSIC" -> 220;
            case "SHAWARMA_XL" -> 320;
            case "FALAFEL" -> 180;
            case "DRINK" -> 90;
            default -> 0;
        };

        // 2. subtotal
        int subtotal = unitPrice * qty;

        // 3. discountPercent
        int discountPercent;

        if (subtotal >= 2000) {
            discountPercent = 15;
        } else if (subtotal >= 1000) {
            discountPercent = 10;
        } else if (subtotal >= 500) {
            discountPercent = 5;
        } else {
            discountPercent = 0;
        }

        int total = subtotal - subtotal * discountPercent / 100;

        String batch = qty <= 1 ? "single" : (qty <= 5 ? "small batch" : "big batch");
        String kitchenAction = "";

        if (minutesToClose <= 0) {
            System.out.println("kitchen closed - reject");
        } else {
            kitchenAction = switch (dish) {
                case "SHAWARMA_CLASSIC" -> "grill 4 min";
                case "SHAWARMA_XL" -> "grill 7 min";
                case "FALAFEL" -> "fry 5 min";
                case "DRINK" -> "pour & serve";
                default -> "manual check";
            };
        }
        // 4. total
        System.out.println("[ORDER]   " + dish + " " + "x" + qty);
        System.out.println("[PRICE]   " + subtotal + " -> " + total + " (-" + discountPercent + "%)");
        System.out.println("[BATCH]   " + batch);
        System.out.println("[KITCHEN] " + kitchenAction);
        System.out.println("[CLOSE]   " + minutesToClose + " min left");
    }
}
