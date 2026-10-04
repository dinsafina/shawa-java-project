package com.strings;

public class Theory {
    public static void main(String[] args) {
        String actual = "NEW";
        String expected = "NEW";
        System.out.println(actual.equals(expected));
        System.out.println("admin".equalsIgnoreCase("ADMIN"));

        String url = "https://megalodon.com";
        System.out.println(url.contains("mega"));

        url = url.toUpperCase();
        System.out.println(url);

    }
}
