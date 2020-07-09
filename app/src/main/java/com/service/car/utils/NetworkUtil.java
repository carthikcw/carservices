package com.service.car.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtil {

    static NetworkUtil networkUtil;
    public static NetworkUtil getInstance(){
        return networkUtil = new NetworkUtil();
    }

    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
