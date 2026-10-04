package com.oop_inheritance;

public class LoginPage extends BasePage{

    protected LoginPage(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public String url() {
        return baseUrl + "/login";
    }

    @Override
    public String readySelector() {
        return "submitBtn";
    }

    public void enterCredentials(String email, String password) {
        System.out.println("[LOGIN] " + email + " / " + "*".repeat(password.length()));
    }
}
