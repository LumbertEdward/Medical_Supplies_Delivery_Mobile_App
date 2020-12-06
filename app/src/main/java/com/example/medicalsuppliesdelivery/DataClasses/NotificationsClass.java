package com.example.medicalsuppliesdelivery.DataClasses;

public class NotificationsClass {
    private String notification;

    public NotificationsClass(String notification) {
        this.notification = notification;
    }

    public NotificationsClass() {
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
