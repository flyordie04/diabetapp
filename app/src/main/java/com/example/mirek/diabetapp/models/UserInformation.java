package com.example.mirek.diabetapp.models;

/**
 * Created by Mirek on 20.06.2018.
 */

public class UserInformation {
    private String phone_number;
    private String settings;
    private String weight;
    private String city;
    private String name;
    private String userPhoneNumber;
    private String surname;

    public UserInformation(){

    }

    public String getPhone_number() {
        return phone_number;
    }
    public String getWeight(){
        return weight;
    }
    public String getCity(){
        return city;
    }
    public String getName(){
        return name;
    }
    public String getUserPhoneNumber(){
        return userPhoneNumber;
    }
    public String getSurname(){
        return surname;
    }


    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
