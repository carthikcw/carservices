package com.service.car.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    /***
     * For Fetching using User POJO
     */

    @SerializedName("customerId")
    @Expose
    private String customerId;

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


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getWashingType() {
        return washingType;
    }

    public void setWashingType(String washingType) {
        this.washingType = washingType;
    }

    public String getPrimaryNumber() {
        return primaryNumber;
    }

    public void setPrimaryNumber(String primaryNumber) {
        this.primaryNumber = primaryNumber;
    }

    public String getSecondaryNumber() {
        return secondaryNumber;
    }

    public void setSecondaryNumber(String secondaryNumber) {
        this.secondaryNumber = secondaryNumber;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsValidUser() {
        return isValidUser;
    }

    public void setIsValidUser(String isValidUser) {
        this.isValidUser = isValidUser;
    }
}
