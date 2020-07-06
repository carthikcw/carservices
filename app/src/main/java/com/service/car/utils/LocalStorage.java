package com.service.car.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class LocalStorage {

    private static final String sharedName = "CARSERVICE_PREFERENCES";
    private SharedPreferences sharedPreferences;
    private static LocalStorage localStorage;


    /***
     * Created Singleton Class for accessing this class methods
     * @param context
     * @return
     */
    public static LocalStorage localStorage(Context context) {
        if (localStorage == null)
            localStorage = new LocalStorage();
        return localStorage;
    }

    // save String Preferences
    public static void saveToLocalStorage(Context mContext, String key, String value) {
        SharedPreferences mySharedPreferences = mContext.getSharedPreferences(sharedName, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    // load String Preferences
    public static String getLocallyStoredValue(Context mContext, String key) {
        SharedPreferences mySharedPreferences = mContext.getSharedPreferences(sharedName, mContext.MODE_PRIVATE);
        String credential = mySharedPreferences.getString(key, null);
        return credential;
    }

    public static int getLocallyStoredRating(Context mContext, String key) {
        SharedPreferences mySharedPreferences = mContext.getSharedPreferences(sharedName, mContext.MODE_PRIVATE);
        int rating = mySharedPreferences.getInt(key, 0);
        return rating;
    }

    //Remove String Preference
    public static void removePreferences(Context mContext, String key) {
        SharedPreferences mySharedPreferences = mContext.getSharedPreferences(sharedName, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void clearPreferences(Context mContext) {
        SharedPreferences mySharedPreferences = mContext.getSharedPreferences(sharedName, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    /***
     * STORING USER PREFERENCES TO SHARED PREFERENCES
     * @param mContext
     * @param userId
     * @param isVegan
     * @param isVegetarian
     * @param isGluten
     * @param isLakto
     * @param noRestricitons
     */
    public static void storeUserPrefernces(Context mContext,String userId, String isVegan,String isVegetarian,String isGluten,String isLakto,String noRestricitons){
        SharedPreferences mySharedPreferences = mContext.getSharedPreferences(sharedName, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(Constants.userId, userId);
        editor.putString(Constants.isVegan, isVegan);
        editor.putString(Constants.isVegetarian, isVegetarian);
        editor.putString(Constants.isgluten, isGluten);
        editor.putString(Constants.isLakto, isLakto);
        editor.putString(Constants.noRestriction,noRestricitons);
        editor.commit();
    }


    /***
     * STORING USER RATINGS TO SHARED PREFERENCES
     * @param context
     * @param userId
     * @param environmentRating
     * @param fairSocialRating
     */
    public static void storeUserRatings(Context context, String userId, int environmentRating,int fairSocialRating){
        SharedPreferences mySharedPreferences = context.getSharedPreferences(sharedName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(Constants.userId, userId);
        editor.putInt(Constants.environmentrating, environmentRating);
        editor.putInt(Constants.fairsocialrating, fairSocialRating);
        editor.commit();
    }


    public static void removeUserPreferences(Context context){
        SharedPreferences mySharedPreferences = context.getSharedPreferences(sharedName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.remove(Constants.isVegan);
        editor.remove(Constants.isVegetarian);
        editor.remove(Constants.isgluten);
        editor.remove(Constants.isLakto);
        editor.remove(Constants.noRestriction);

        editor.remove(Constants.environmentrating);
        editor.remove(Constants.fairsocialrating);
        editor.commit();
    }

}
