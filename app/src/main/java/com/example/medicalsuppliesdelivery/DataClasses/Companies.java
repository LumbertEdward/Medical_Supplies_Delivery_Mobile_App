package com.example.medicalsuppliesdelivery.DataClasses;

import android.os.Parcel;
import android.os.Parcelable;

public class Companies implements Parcelable {
    private String name;
    private String location;
    private String rating;

    public Companies() {
    }

    public Companies(String name, String location, String rating) {
        this.name = name;
        this.location = location;
        this.rating = rating;
    }

    protected Companies(Parcel in) {
        name = in.readString();
        location = in.readString();
        rating = in.readString();
    }

    public static final Creator<Companies> CREATOR = new Creator<Companies>() {
        @Override
        public Companies createFromParcel(Parcel in) {
            return new Companies(in);
        }

        @Override
        public Companies[] newArray(int size) {
            return new Companies[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(rating);
    }
}
