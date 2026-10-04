package com.Homeworks.Homework3;

public class Combo extends MenuItem implements Discountable {

    protected Combo(String name, int basePrice) {
        super(name, basePrice);
    }

    @Override
    int price() {
        return basePrice;
    }

    @Override
    String kind() {
        return "Комбо";
    }

    @Override
    public int discountPercent() {
        return 15;
    }
}
