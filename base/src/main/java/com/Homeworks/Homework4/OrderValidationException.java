package com.Homeworks.Homework4;

public class OrderValidationException extends RuntimeException {

    public OrderValidationException(String reason) {
        super("OrderValidation: " + reason);
    }
}
