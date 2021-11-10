package com.mristudio.blooddonation.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mristudio.blooddonation.R;

public class BadageHealper {

    public static void showBadge(Context context, BottomNavigationView
            bottomNavigationView, @IdRes int itemId, int value, boolean status) {
        removeBadge(bottomNavigationView, itemId);
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.layout_badge, bottomNavigationView, false);
        TextView message = badge.findViewById(R.id.badge_text_Message);
        TextView notification = badge.findViewById(R.id.badge_text_Notification);


        if (status) {
            message.setVisibility(View.VISIBLE);
            notification.setVisibility(View.GONE);
            if (value >= 1) {
                if (value < 9) {
                    message.setText(String.valueOf(value));
                } else {
                    message.setText("9+");
                }

            } else {
                message.setVisibility(View.GONE);
                notification.setVisibility(View.GONE);
            }

        } else {
            notification.setVisibility(View.VISIBLE);
            message.setVisibility(View.GONE);

            if (value >= 1) {
                if (value < 9) {
                    notification.setText(String.valueOf(value));
                } else {
                    notification.setText("9+");
                }

            } else {
                message.setVisibility(View.GONE);
                notification.setVisibility(View.GONE);
            }
        }


        //TextView text = badge.findViewById(R.id.badge_text_view);
        //  text.setText(value);
        itemView.addView(badge);
    }

    public static void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 3) {
            itemView.removeViewAt(2);
        }
    }
}
