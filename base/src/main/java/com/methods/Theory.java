package com.methods;

public class Theory {
    public static void main(String[] args) {
        String message = "Вызов из main";
        logStep(message);
        String orderUrl = buildUrl("https://google.com", 55);
        System.out.println(orderUrl);

        String env = "LOCAL";
        System.out.println(getUrlFromEnv(env));
    }

    public static void logStep(String message) {
        System.out.println("Логируем шаг: " + message);
    }

    public static String buildUrl(String baseUrl, int orderId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(baseUrl);
        stringBuilder.append("/api/orders/");
        stringBuilder.append(orderId);
        return stringBuilder.toString();
    }

    public static String getUrlFromEnv(String env) {

        return switch (env) {
            case "LOCAL" -> "localhost:8080";
            case "PRODUCTION" -> "google.com";
            case "STAGE" -> "ya.ru";
            default -> "Unknown";
        };
    }
}
