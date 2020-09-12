package com.service.car.models;

import androidx.annotation.NonNull;

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
    private boolean isValidUser;

    @SerializedName("orderStatus")
    @Expose
    private String orderStatus;

    @SerializedName("uuid")
    @Expose
    private String uuid;


    public Customer(String vehicleType, String washingType, String primaryNumber, String secondaryNumber,
                    String userLocation, String userMessage, String date, boolean isValidUser,
                    String orderStatus, String uuid) {
        this.vehicleType = vehicleType;
        this.washingType = washingType;
        this.primaryNumber = primaryNumber;
        this.secondaryNumber = secondaryNumber;
        this.userLocation = userLocation;
        this.userMessage = userMessage;
        this.date = date;
        this.isValidUser = isValidUser;
        this.orderStatus = orderStatus;
        this.uuid = uuid;
    }

    @NonNull
    @Override
    public String toString() {
        return "Customer vehicleType=" + vehicleType + ", washingType=" + washingType
                + ", primaryNumber=" + primaryNumber + ", secondaryNumber=" + secondaryNumber + ", userLocation="
                + userLocation + ", userMessage=" + userMessage + ", orderStatus=" + orderStatus + ", date=" + date
                + ", isValidUser=" + isValidUser + ", uuid= "+uuid;
    }
}
