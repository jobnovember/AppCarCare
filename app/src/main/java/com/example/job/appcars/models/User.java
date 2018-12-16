package com.example.job.appcars.models;

import java.util.ArrayList;

public class User {
    private String uid;
    private String profile_image;
    private String name;
    private String phone;
    private ArrayList<String> cars;
    private ArrayList<String> cars_number;
    public User() {

    }

    public User(String id,String profile_image, String name,String phone,ArrayList<String> cars,ArrayList<String> numbers){
        this.uid = id;
        this.profile_image = profile_image;
        this.name = name;
        this.phone = phone;
        this.cars = cars;
        this.cars_number = numbers;
    }

    public String getUid() {
        return uid;
    }

    public String getProfile_image() {
       return profile_image;
    }
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public ArrayList<String> getCars() {
        return cars;
    }


    public ArrayList<String> getCars_number() {
        return cars_number;
    }

    public void setUid (String uid){
       this.uid = uid;
    }

    public void setName (String name) {
        this.name = name;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCars(ArrayList<String> cars) {
        this.cars = cars;
    }

    public void setCars_number(ArrayList<String> cars_number) {
        this.cars_number = cars_number;
    }

}
