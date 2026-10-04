package com.methods;

import java.util.Arrays;
import java.util.List;

public class Overloading {
    public static void main(String[] args) {
       String baseUrl = "https://localhost:8080";
        String first = buildUrl(baseUrl, "/api/orders/");
        String second = buildUrl(baseUrl, "/api/orders/", 10);
        String third = buildUrl(baseUrl, "/api/orders/", "?sort=ASC");
//        System.out.println(first);
//        System.out.println(second);
//        System.out.println(third);

        System.out.println("Результат:" + Arrays.toString(sum(new int[]{1, 2, 3})));
    }

    public static String buildUrl(String baseUrl, String path) {
        return baseUrl + path;
    }

    public static String buildUrl(String baseUrl, String path, int id) {
        return baseUrl + path + id;
    }

    public static String buildUrl(String baseUrl, String path, String queryParam) {
        return baseUrl + path + queryParam;
    }


    public static int[] sum(int... values) {
      int result = 0;
        for (int value: values) {
            result = result + value;
        }
        return new int[]{result};
    }
}
