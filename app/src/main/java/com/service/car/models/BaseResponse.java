package com.service.car.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("userMessage")
    @Expose
    private String userMessage;

    @SerializedName("requests")
    @Expose
    private List<User> requests;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public List<User> getRequests() {
        return requests;
    }

    public void setRequests(List<User> requests) {
        this.requests = requests;
    }
}
