package com.service.car.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Customer {

    /***
     * For Registering Using Customer POJO
     */


    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;

    @SerializedName("washingType")
    @Expose
    private String washingType;

    @SerializedName("primaryNumber")
    @Expose
    private String primaryNumber;

    @SerializedName("secondaryNumber")
    @Expose
    private String secondaryNumber;

    @SerializedName("userLocation")
    @Expose
    private String userLocation;

    @SerializedName("userMessage")
    @Expose
    private String userMessage;

    //"yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("isValidUser")
    @Expose
    private String isValidUser;

    @SerializedName("orderStatus")
    @Expose
    private String orderStatus;


    public Customer(String vehicleType, String washingType, String primaryNumber, String secondaryNumber, String userLocation, String userMessage, String date, String isValidUser, String orderStatus) {
        this.vehicleType = vehicleType;
        this.washingType = washingType;
        this.primaryNumber = primaryNumber;
        this.secondaryNumber = secondaryNumber;
        this.userLocation = userLocation;
        this.userMessage = userMessage;
        this.date = date;
        this.isValidUser = isValidUser;
        this.orderStatus = orderStatus;
    }
}
