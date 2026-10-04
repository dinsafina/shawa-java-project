package com.Homeworks.Homework3;

public abstract class MenuItem {

    protected final String name;
    protected final int basePrice;

    protected MenuItem(String name, int basePrice) {
        this.name = name;
        this.basePrice = basePrice;
    }

    abstract int price();

    abstract String kind();

    void printReceipt() {
        System.out.println("[" + kind() + "] " + name + " — " + price() + " руб");
    }
}
