package com.example.medicalsuppliesdelivery.DataClasses;

import android.os.Parcel;
import android.os.Parcelable;

public class Products implements Parcelable {
    private String name;
    private String rating;
    private int price;
    private String imgUrl;
    private String description;


    public Products() {
    }

    public Products(String name, String rating, int price, String imgUrl, String description) {
        this.name = name;
        this.rating = rating;
        this.price = price;
        this.imgUrl = imgUrl;
        this.description = description;
    }

    protected Products(Parcel in) {
        name = in.readString();
        rating = in.readString();
        price = in.readInt();
        imgUrl = in.readString();
        description = in.readString();
    }

    public static final Creator<Products> CREATOR = new Creator<Products>() {
        @Override
        public Products createFromParcel(Parcel in) {
            return new Products(in);
        }

        @Override
        public Products[] newArray(int size) {
            return new Products[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(rating);
        dest.writeInt(price);
        dest.writeString(imgUrl);
        dest.writeString(description);
    }
}
