package com.example.job.appcars.models;

public class Booking {
    private String mId;
    private String car_name;
    private String date;
    private String time;
    private String[] orders;
    private String sum;
    private String user_name;
    private String status;

    public Booking(String id,String time, String carname, String date, String[] orders, String sum, String uid) {
        this.mId = id;
        this.car_name = carname;
        this.date = date;
        this.orders= orders;
        this.sum= sum;
        this.time = time;
        this.user_name= uid;
    }

    public String getId() {
        return mId;
    }

    public String getCarname() {
        return car_name;
    }

    public String getDate() {
        return date;
    }

    public String[] getOrders() {
        return orders;
    }

    public String getSum() {
        return sum;
    }

    public String getTime() {
        return time;
    }

    public String getUid() {
        return user_name;
    }

    public String getStatus() {
        return status;
    }
}
