package com.mristudio.blooddonation.notification;

import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.constraintlayout.solver.state.State;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.view.activity.PostDetailsActivity;
import com.mristudio.blooddonation.view.activity.SendMessageActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class NotificationHelper {
    Context context;
    private static final int id = 100;

    public NotificationHelper(Context context) {
        this.context = context;
    }


    public void tiggerNotificationForMessage(String tittle, String sendername, String msg, String receiver) {

        Log.e(TAG, "tiggerNotificationForMessage: "+tittle );
        Intent intent = new Intent(context, SendMessageActivity.class);
        intent.putExtra("userChatId", receiver);
        intent.putExtra("status", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.id)
                .setContentTitle(tittle)
                .setContentText(sendername+": "+msg)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(defaultSound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        builder.setChannelId(Constants.id);
        managerCompat.notify(id, builder.build());

    }

    public void tiggerNotification(String tittle, String address, String image, String description, String userPostId) {

        Intent intent = new Intent(context, PostDetailsActivity.class);
        intent.putExtra("userPostId", userPostId);
        intent.putExtra("status", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.id)
                .setContentTitle(tittle)
                .setContentText(address)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(defaultSound)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(image)).setBigContentTitle(tittle))
                .setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(id, builder.build());

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
