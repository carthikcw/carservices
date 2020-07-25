package com.service.car.services;

import com.service.car.models.BaseResponse;
import com.service.car.models.Customer;
import com.service.car.models.UserRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface EndPoints {

    @Headers({
            "Content-Type: application/json"
    })
    @POST("requestWash")
    Call<BaseResponse> requestWash(@Body Customer customer);

    @Headers({
            "Content-Type: application/json"
    })
    @POST("getRequests")
    Call<BaseResponse> getRequests(@Body UserRequest userRequest);
}
