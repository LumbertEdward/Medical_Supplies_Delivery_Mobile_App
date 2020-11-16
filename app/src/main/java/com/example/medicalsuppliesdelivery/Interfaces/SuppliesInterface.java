package com.example.medicalsuppliesdelivery.Interfaces;

import com.example.medicalsuppliesdelivery.DataClasses.Companies;
import com.example.medicalsuppliesdelivery.DataClasses.Products;

public interface SuppliesInterface {
    void getCompany(Companies companies);
    void getData(Products products);
    void getLabData();
    void getSurgeryData();
    void getMaternityData();
    void getSterilisationData();
    void addToCart(Products products);
    void deleteItem(Products products);
    void getPopular();
    void getNewArrivals();
    void getFavorites(Products products);
    void removeFav(Products products);
    void trackOrders();
    void orderMpesa(int price);
    void orderPaypal(int price);
    void onBackPressed();
    void orderPOD(int price);
}
