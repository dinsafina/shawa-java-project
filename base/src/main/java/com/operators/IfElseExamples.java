package com.operators;

public class IfElseExamples {
    public static void main(String[] args) {
        int statusCode = 404;
        if(statusCode == 200) {
            System.out.println("OK");
        } else if(statusCode == 201) {
            System.out.println("Created");
        } else if(statusCode >= 400 && statusCode <= 500) {
            System.out.println("Client error " + statusCode);
        } else if(statusCode >= 500) {
            System.out.println("Server error" + statusCode);
        } else {
            System.out.println("Other" + statusCode);
        }
        boolean isLoggedIn = true;
        boolean isAdmin = false;

        if(isLoggedIn) {
            if(isAdmin) {
                System.out.println("Можно редактировать страницу");
            } else {
                System.out.println("Только просмотр");
            }
        } else {
            System.out.println("Сначала авторизуйся");
        }
    }
}
