package com.example.matrixhive.mvpdemo1.main;

import android.text.TextUtils;
import android.util.Patterns;

public class MainActivityInteractor implements MainActivityContract.interactor {
    private String name;
    private String email;
    private String contact;
    private String image;

    public MainActivityInteractor(String name, String email, String contact,String image) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.image = image;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getContact() {
        return contact;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int isValidateData() {

        if (TextUtils.isEmpty(getName()))
            return 0;
        else if (!Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches())
            return 1;
        else if (getContact().length() < 10)
            return 2;
        else if (TextUtils.isEmpty(getImage()))
            return 3;
        else
            return -1;
    }



}
