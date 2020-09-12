package com.service.car.services;

import com.service.car.models.BaseResponse;
import com.service.car.models.Customer;
import com.service.car.models.FcmUser;
import com.service.car.models.OtpMessageResponse;
import com.service.car.models.UserRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EndPoints {

    @Headers({ "Content-Type: application/json" })
    @POST("requestWash")
    Call<BaseResponse> requestWash(@Body Customer customer);

    @Headers({ "Content-Type: application/json" })
    @POST("getRequests")
    Call<BaseResponse> getRequests(@Body UserRequest userRequest);

    @Headers({ "Content-Type: application/json" })
    @POST("changeOrderStatus")
    Call<BaseResponse> changeOrderStatus(String requestId, String orderStatus);

    @Headers({ "Content-Type: application/json" })
    @POST("termsconditions")
    Call<BaseResponse> termsConditions();

    @Headers({ "Content-Type: application/json" })
   @POST("addToken")
   Call<BaseResponse> addToken(@Body FcmUser fcmUser);


    @GET("{api_key}/SMS/+91{users_phone_no}/AUTOGEN")
    Call<OtpMessageResponse> sendOTP(@Path("api_key")String apiKey, @Path("users_phone_no")String phone_no);

    @GET("{api_key}/SMS/VERIFY/{session_id}/{otp_entered_by_user}")
    Call<OtpMessageResponse> verifyOTP(@Path("api_key")String apiKey, @Path("session_id")String session_id, @Path("otp_entered_by_user")String otp_entered_by_user);

}
