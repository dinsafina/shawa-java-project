package com.collections;

import java.util.*;

public class Theory {
    public static void main(String[] args) {
        List<String> browsers = new ArrayList<>();
        browsers.add("chrome");
        browsers.add("yandex");
        browsers.add("firefox");
        browsers.add("opera");
        int size = browsers.size();
        boolean safari = browsers.contains("safari");
        System.out.println(size);
        System.out.println(safari);

        for (String browser : browsers) {
            System.out.println(browser);
        }

        Map<String, String> cars = new HashMap<>();
        cars.put("KIA", "green");
        cars.put("NISSAN", "yellow");
        cars.put("HONDA", "white");
        System.out.println("MAP: " + cars);
        System.out.println(cars.size());

        Set<Map.Entry<String, String>> entries = cars.entrySet();//достает пары все ключи и значения
        System.out.println(entries);

        for (Map.Entry<String, String> stringStringEntry : cars.entrySet()) {
            stringStringEntry.getKey();
            stringStringEntry.getValue();
        }

        Collection<String> values = cars.values(); //все значения
        System.out.println(values);

        Set<String> keys = cars.keySet();//все ключи
        System.out.println(keys);

        Set<String> colors = new HashSet<>();
        colors.add("green");
        colors.add("black");
        colors.add("green");
        System.out.println("SET: " + colors);
        System.out.println(colors.size());
    }
}
