package com.example.medicalsuppliesdelivery.DataClasses;

public class Orders {
    private String orderId;
    private int price;

    public Orders() {
    }

    public Orders(String orderId, int price) {
        this.orderId = orderId;
        this.price = price;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
