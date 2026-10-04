package com.oop_inheritance;

public abstract class BasePage {

    protected final String baseUrl;

    protected BasePage(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public abstract String url();

    public abstract String readySelector();

    public void waitLoaded() {
        System.out.println("[WAIT] " + readySelector() + " visible");
    }

    public void open() {
        System.out.println("[NAV] open url " + url());
    }
}
