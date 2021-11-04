package com.mristudio.blooddonation.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Contrnt-Type:application/json",
            "Authorization:key=AAAAZmteolo:APA91bF5gyU9mrSHcVPdeEkLXlx-GfPhaFzUY_9f4585Ga4xvqxgv6uJCZ9YN6r1IJ0IexFbVmM3-geT-pH1_DcX8ELZB4RZEvtDP5SJ8HtJIQB6NriHBGEds5SskSy2BwmCBgVRE7di "
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
