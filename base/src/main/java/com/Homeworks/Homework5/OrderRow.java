package com.Homeworks.Homework5;

public class OrderRow {

    private final long id;
    private final String customer;
    private final OrderStatus status;
    private final PaymentChoice payment;
    private final double total;

    public OrderRow(long id, String customer, OrderStatus status, PaymentChoice payment, double total) {
        this.id = id;
        this.customer = customer;
        this.status = status;
        this.payment = payment;
        this.total = total;
    }

    public long getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public PaymentChoice getPayment() {
        return payment;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "OrderRow{" +
                "id=" + id +
                ", customer='" + customer + '\'' +
                ", status=" + status +
                ", payment=" + payment +
                ", total=" + total +
                '}';
    }
}
