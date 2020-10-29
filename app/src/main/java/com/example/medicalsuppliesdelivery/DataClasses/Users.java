package com.example.medicalsuppliesdelivery.DataClasses;

public class Users {
    private String username;
    private String fullName;
    private String number;
    private String gender;
    private String date;
    private String language;

    public Users() {
    }

    public Users(String username, String fullName, String number, String gender, String date, String language) {
        this.username = username;
        this.fullName = fullName;
        this.number = number;
        this.gender = gender;
        this.date = date;
        this.language = language;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
