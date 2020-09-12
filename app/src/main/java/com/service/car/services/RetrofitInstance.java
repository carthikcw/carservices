package com.service.car.services;

import com.service.car.utils.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static retrofit2.Retrofit retrofitInstance;

    private static final String APP_BASE_URL = "http://192.168.0.7:8888/sanitize/api/v1/";

    private static final String FCM_BASE_URL = "http://18.191.97.164:9182/";

    private static String OTP_BASE_URL = "https://2factor.in/API/V1/"; //+"/SMS/"++"/OTP_VALUE";

    public static retrofit2.Retrofit getRetrofit(int type) {
        String BASE_URL = "";
        if (type == 1){
         BASE_URL = APP_BASE_URL;
        }else if (type == 2){
            BASE_URL = FCM_BASE_URL;
        }else if(type == 3){
            BASE_URL = OTP_BASE_URL;
        }
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();

        return retrofitInstance = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
