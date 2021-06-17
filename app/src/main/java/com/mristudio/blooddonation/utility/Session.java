package com.mristudio.blooddonation.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


public class Session {
    public static final String PREFER_NAME = "blood_donation";
    public static final String IS_USER_LOGIN = "IsUserLoggedIn";
    public static final String KEY_ID_USER = "idUser";
    public static final String KEY_ID_USERType = "idUserType";
    public static final String KEY_ID_USER_EMAIL = "userEmail";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;


    public Session(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getData(String id) {
        return pref.getString(id, "");
    }


    public void setData(String id, String val) {
        editor.putString(id, val);
        editor.commit();
    }

    public void saveLogingSession(boolean isUserLoging, String userId, String userType,String email) {
        editor.putBoolean(IS_USER_LOGIN, isUserLoging);
        editor.putBoolean(IS_USER_LOGIN, isUserLoging);
        editor.putString(KEY_ID_USER, userId);
        editor.putString(KEY_ID_USERType, userType);
        editor.putString(KEY_ID_USER_EMAIL, email);
        editor.commit();
    }
    public  String  getUserType(){
        return pref.getString(KEY_ID_USERType,"admin");
    }
    public  String  getEmail(){
        return pref.getString(KEY_ID_USER_EMAIL,null);
    }

    public void saveLogingSessionData(boolean isUserLoging, String userId) {
        editor.putBoolean(IS_USER_LOGIN, isUserLoging);
        editor.putString(KEY_ID_USER, userId);
        editor.commit();
    }

    public String getUserSessionId() {
        return pref.getString(KEY_ID_USER, null);
    }

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

}