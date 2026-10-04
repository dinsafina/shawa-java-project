package com.Homeworks.Homework3;

public class Drink extends MenuItem {

    protected Drink(String name, int basePrice) {
        super(name, basePrice);
    }

    @Override
    int price() {
        return basePrice;
    }

    @Override
    String kind() {
        return "Напиток";
    }
}
