package com.strings;

import java.util.Arrays;

public class FormatExample {
    public static void main(String[] args) {
        int userId = 39;
        int size = 10;
        String sort = "ASC";

        String url = "https://google.com/users/%d/orders?size=%d&sort=%s".formatted(userId, size, sort);
        System.out.println(url);

        String endpoint = "/api/orders/";
        int id = 42;
        String result =  "http://localhost:8080%s%d".formatted(endpoint, id);
        System.out.println(result);

        String  csv = "яблоко,банан,киви";
        String [] resultArr = csv.split(",");
        System.out.println(Arrays.toString(resultArr));

        String[] tags = {"java", "test", "qa"};
        String res = String.join(",", tags);
        System.out.println(res);
    }
}
