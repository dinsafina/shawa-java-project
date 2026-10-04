package com.oop_basics;

public class OrderResponse {
    private int id;
    private String status;
    private double price;
    private int receiptId;

    public OrderResponse(int id, String status, double price, int receiptId) {
        this.id = id;
        this.status = status;
        this.price = price;
        this.receiptId = receiptId;
    }

    public OrderResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public void summary() {
        System.out.println(status + id + price + receiptId);
    }

    public void addDiscount(int percent) {
        boolean isValidPercent = validateDiscount(percent);
        if(!isValidPercent) {
            System.out.println("Скидка не применилась: " + percent);
            return;
        }
        this.price  = this.price + percent;

    }

    private boolean validateDiscount(int percent) {
        if (percent < 0) {
            return false;
        }
        if (percent > 100) {
            return false;
        }
        return true;
    }
}
