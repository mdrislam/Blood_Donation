package com.mristudio.blooddonation.utility;

import android.content.Context;
import android.net.ConnectivityManager;

public   class UtilMethod {
    private static Context mContext;

    public UtilMethod(Context context) {
        mContext = context;
    }

    /**
     * Cheak User Data Connection
     */
    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
