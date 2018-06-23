package com.example.mirek.diabetapp.models;

/**
 * Created by Mirek on 20.06.2018.
 */

public class UserInformation {
    private String phone_number;
    private String settings;

    public UserInformation(){

    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
