package com.Homeworks.Homework4;

public enum PaymentChoice {

    CARD(true),
    CASH(false),
    SBP(true);

    private final boolean online;

    PaymentChoice(boolean online) {
        this.online = online;
    }

    public boolean isOnline() {
        return online;
    }
}
