package com.service.car.services;

import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static retrofit2.Retrofit retrofitInstance;
    private static final String BASE_URL = "http://183.83.80.234:8888/sanitize/api/v1/";

    public static retrofit2.Retrofit getRetrofit() {
        return retrofitInstance = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
