package com.mristudio.blooddonation.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.view.activity.SendMessageActivity;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        NotificationHelper helper = new NotificationHelper(this);

        String type = remoteMessage.getData().get("type");
        String sender = remoteMessage.getData().get("sender");
        String receiver = remoteMessage.getData().get("receiver");
        String tittle = remoteMessage.getData().get("tittle");
        String sendername = remoteMessage.getData().get("sendername");
        String msg = remoteMessage.getData().get("msg");
        String imageurl = remoteMessage.getData().get("imageurl");
        String description = remoteMessage.getData().get("description");
        String address = remoteMessage.getData().get("address");
        String postId = remoteMessage.getData().get("postId");

        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        String myUser = sharedPreferences.getString("currentuser", "none");


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            if (type.equals("topic")) {
                helper.tiggerNotification(tittle, address, imageurl, description, postId);

            } else {

                if (receiver.equals(currentUser.getUid())) {

                    if (!myUser.equals(sender)) {
                        helper.tiggerNotificationForMessage(tittle, sendername, msg, sender);
                        // sendNotificationMessage(remoteMessage);
                    }
                }
            }
        }


//        if (currentUser != null && receiver.equals(currentUser.getUid())) {
//            if (!myUser.equals(sender)) {
//                sendNotificationMessage(remoteMessage);
//            }
//        }


        //Toast.makeText(this, ""+sented, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "onMessageReceived: " + receiver);
        Log.e(TAG, "tittle: " + tittle);


    }

    private void sendNotificationMessage(RemoteMessage remoteMessage) {


        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String tittle = remoteMessage.getData().get("tittle");
        String body = remoteMessage.getData().get("body");


        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).
                setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(tittle)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound);

        Intent intent = new Intent(this, SendMessageActivity.class);
        intent.putExtra("userChatId", user);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        int notifyId = (int) System.currentTimeMillis();
        NotificationManager notyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notyManager.createNotificationChannel(notificationChannel);

        }
        notyManager.notify(notifyId, builder.build());


    }
}
