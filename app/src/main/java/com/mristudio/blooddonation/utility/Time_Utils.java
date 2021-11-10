package com.mristudio.blooddonation.utility;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Time_Utils {


    public static String getTimesAgo(String date) {

        // String time = "Nov 8, 2021 01:22 PM";
        String returnString = null;

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("MMM d, yyyy hh:mm a");
        String datetimeNow = dateformat.format(c.getTime());


        Date todayDate = null;
        Date postDate = null;
        try {
            postDate = (Date) dateformat.parse(date);
            todayDate = (Date) dateformat.parse(datetimeNow);

            long data = todayDate.getTime() - postDate.getTime();
            returnString = time_Ago(Math.round((Math.abs(data) / 1000) / 60));

            Log.e(TAG, "getDaysAgo: " + time_Ago(Math.round((Math.abs(data) / 1000) / 60)));


        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "getDaysAgo Error Message: " + e.getMessage());
            returnString = "";
        }


        return returnString;

    }

    public static String getCampaignDaysTogo(String date) {

        String returnString = null;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("MMM d, yyyy hh:mm a");
        String datetimeNow = dateformat.format(c.getTime());

        Date todayDate = null;
        Date postDate = null;
        try {
            postDate = (Date) dateformat.parse(date);
            todayDate = (Date) dateformat.parse(datetimeNow);
            Log.e(TAG, "getDaysAgo postDate: " + postDate);
            Log.e(TAG, "getDaysAgo todayDate: " + todayDate);
            long data = todayDate.getTime()-postDate.getTime();

            long days = data / 86400000;

            if (days <= 0) {

                returnString = "Today";
            } else {
                returnString = "" + days + " days to go";
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "getDaysAgo Error Message: " + e.getMessage());
            returnString = "";
        }


        return returnString;
    }

    public static String getDaysToGo(String date) {

        String returnString = null;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("MMM d, yyyy hh:mm a");
        String datetimeNow = dateformat.format(c.getTime());

        Date todayDate = null;
        Date postDate = null;
        try {
            postDate = (Date) dateformat.parse(date);
            todayDate = (Date) dateformat.parse(datetimeNow);
            Log.e(TAG, "getDaysAgo postDate: " + postDate);
            Log.e(TAG, "getDaysAgo todayDate: " + todayDate);
            long data =  todayDate.getTime()-postDate.getTime();

            long days = data / 86400000;
            long day =100-days;

            if (day <=0) {

                returnString = "Availabe";
            } else {
                returnString = "Avaiable at after " + day + " days";
            }


        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "getDaysAgo Error Message: " + e.getMessage());
            returnString = "";
        }


        return returnString;
    }

    public static String time_Ago(int timeDIM) {

        String timeAgo = null;

        if (timeDIM == 0) {
            timeAgo = "less than a minute";
        } else if (timeDIM == 1) {
            return "1 minute";
        } else if (timeDIM >= 2 && timeDIM <= 44) {
            timeAgo = timeDIM + " minutes";
        } else if (timeDIM >= 45 && timeDIM <= 89) {
            timeAgo = "about an hour";
        } else if (timeDIM >= 90 && timeDIM <= 1439) {
            timeAgo = "about " + (Math.round(timeDIM / 60)) + " hours";
        } else if (timeDIM >= 1440 && timeDIM <= 2519) {
            timeAgo = "1 day";
        } else if (timeDIM >= 2520 && timeDIM <= 43199) {
            timeAgo = (Math.round(timeDIM / 1440)) + " days";
        } else if (timeDIM >= 43200 && timeDIM <= 86399) {
            timeAgo = "about a month";
        } else if (timeDIM >= 86400 && timeDIM <= 525599) {
            timeAgo = (Math.round(timeDIM / 43200)) + " months";
        } else if (timeDIM >= 525600 && timeDIM <= 655199) {
            timeAgo = "about a year";
        } else if (timeDIM >= 655200 && timeDIM <= 914399) {
            timeAgo = "over a year";
        } else if (timeDIM >= 914400 && timeDIM <= 1051199) {
            timeAgo = "almost 2 years";
        } else {
            timeAgo = "about " + (Math.round(timeDIM / 525600)) + " years";
        }

        return timeAgo + " ago";

    }
}
