package com.example.medicalsuppliesdelivery.DataClasses;

import androidx.fragment.app.Fragment;

public class FragmentClass {
    private Fragment fragment;
    private String title;

    public FragmentClass(Fragment fragment, String title) {
        this.fragment = fragment;
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
