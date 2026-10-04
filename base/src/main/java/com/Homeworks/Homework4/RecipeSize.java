package com.Homeworks.Homework4;

public enum RecipeSize {

    S(149),
    M(199),
    L(269);

    private final int basePrice;

    RecipeSize(int basePrice) {
        this.basePrice = basePrice;
    }

    public int getBasePrice() {
        return basePrice;
    }
}
