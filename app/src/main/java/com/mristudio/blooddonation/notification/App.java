package com.mristudio.blooddonation.notification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(Constants.id, Constants.name, NotificationManager.IMPORTANCE_HIGH);
//            notificationChannel.setDescription(Constants.description);
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(notificationChannel);

            NotificationChannel notificationChannel = new NotificationChannel(Constants.id, Constants.name, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(Constants.description);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }





    }
}
