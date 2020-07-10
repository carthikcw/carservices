package com.service.car.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

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

}
