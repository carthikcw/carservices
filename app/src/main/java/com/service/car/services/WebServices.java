package com.service.car.services;

import com.service.car.models.BaseResponse;
import com.service.car.models.Customer;
import com.service.car.models.UserRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WebServices {

    @POST("requestWash")
    Call<Customer> requestWash(@Body Customer customer);


    @POST("getRequests")
    Call<BaseResponse> getRequests(@Body UserRequest userRequest);
}
