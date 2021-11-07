package com.mristudio.blooddonation.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.view.fragment.HomeFragment;
import com.mristudio.blooddonation.view.fragment.MessagingFragment;
import com.mristudio.blooddonation.view.fragment.NotificationFragment;
import com.mristudio.blooddonation.view.fragment.ProfileFragment;
import com.mristudio.blooddonation.model.ImageSliderData;
import com.mristudio.blooddonation.model.UserInformation;
import com.mristudio.blooddonation.utility.Session;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN ACTIVITY";
    public static Toolbar toolbar;

   private BottomNavigationView bottomNavigationView;
   private NavController navController;
    public static RelativeLayout toolbarHomePageLyt;
    boolean doubleBackToExitPressedOnce = false;
    private List<UserInformation> userInformationList = new ArrayList<>();
    private DatabaseReference generalUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarHomePageLyt = toolbar.findViewById(R.id.toolbarHomePageLyt);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.hostFragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        loadUserDataList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(this.getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(getApplication().getResources().getColor(R.color.red_light500));

        setStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        setStatus("ofline");
    }

    @Override
    public void onBackPressed() {
        doubleBack();
    }


    public void doubleBack() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

        } else {
            Toasty.warning(this, "Press Again !", Toasty.LENGTH_SHORT).show();
            this.doubleBackToExitPressedOnce = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

    }

    /**
     * Load SliderImages Data
     */
    private void loadUserDataList() {

        generalUserRef = FirebaseDatabase.getInstance().getReference().child("generalUserTable").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userInformationList.clear();

        generalUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInformation userInformation = snapshot.getValue(UserInformation.class);
                if (userInformation != null) {
                    subscribeTopic(userInformation.getBloodGroup());
                }
                Log.e(TAG, "onDataChange: " + userInformation.getBloodGroup());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error: " + error.getMessage());

            }
        });
    }

    private void subscribeTopic(String topic) {
        SharedPreferences.Editor editor = getSharedPreferences("TOPICS", MODE_PRIVATE).edit();
        editor.putString("topic", concateString(topic));
        editor.apply();
        FirebaseMessaging.getInstance().subscribeToTopic(concateString(topic));
    }

    private String concateString(String blood) {
        if (blood.contains("+")) {
            return blood.replace("+", "_Plus");
        } else if (blood.contains("-")) {
            return blood.replace("-", "_Minus");
        } else {
            return "none";
        }
    }

    private Integer getIndexByuserId(final String email, List<UserInformation> userDataList) {
        int indexOf = -1;
        if (email != null && !userDataList.isEmpty()) {

            for (int i = 0; i < userDataList.size(); i++) {
                if (userDataList.get(i).getemail().equalsIgnoreCase(email)) {
                    indexOf = i;
                    break;
                }
            }
        } else {
            indexOf = -1;

        }

        return indexOf;
    }

    private long getDaysFromCompairTwoDate(String depatureDate) {
        long days = 0;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, yyyy", Locale.US);
            Date depetureDate = (Date) formatter.parse(depatureDate);

            Date today = new Date();
            days = (today.getTime() - depetureDate.getTime()) / 86400000;

        } catch (ParseException e) {
            e.printStackTrace();

        }
        return Math.abs(days);
    }

    private void setStatus(String status) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("activeStatus", status);


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("generalUserTable").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            reference.updateChildren(hashMap);
        }


    }
}