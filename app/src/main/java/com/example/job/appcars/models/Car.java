package com.example.job.appcars.models;

public class Car {
    private String brand;
    private String name;
    private String type;

    public Car() {

    }

    public Car(String brand, String name, String type) {
        this.brand = brand;
        this.name = name;
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }
}
