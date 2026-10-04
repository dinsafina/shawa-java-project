package com.oop_inheritance;

public class Theory {
    public static void main(String[] args) {

        LoginPage loginPage = new LoginPage("http://google.com");
        OrdersPage ordersPage = new OrdersPage("http://google.com");

        System.out.println(loginPage.readySelector());
        System.out.println(ordersPage.readySelector());

        loginPage.open();
        loginPage.waitLoaded();
        loginPage.enterCredentials("test@mail.ru", "12346578");

        ordersPage.waitLoaded();

        Plane plane = new Plane();
        plane.fly();
        plane.startFly("Победа", 1000);
        plane.minDistance();
    }
}
