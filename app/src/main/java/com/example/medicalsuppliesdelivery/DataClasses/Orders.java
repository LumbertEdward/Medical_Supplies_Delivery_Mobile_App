package com.example.medicalsuppliesdelivery.DataClasses;

public class Orders {
    private String orderId;
    private int price;
    private String notification;
    private String date;

    public Orders() {
    }

    public Orders(String orderId, int price, String notification, String date) {
        this.orderId = orderId;
        this.price = price;
        this.notification = notification;
        this.date = date;
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

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
