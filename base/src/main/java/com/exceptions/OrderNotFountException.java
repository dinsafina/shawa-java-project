package com.exceptions;

public class OrderNotFountException extends RuntimeException {
  
  public OrderNotFountException(int orderId) {
    super("Order not found with id = " + orderId);
  }
}
