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

    BottomNavigationView bottomNavigationView;
    NavController navController;

    //    public static BottomNavigationView bottomNavigationView;
    public static Fragment active;
    public static FragmentManager fm = null;

    public static LinearLayout toolbar_profileLyt;
    public static RelativeLayout toolbarHomePageLyt;

    boolean doubleBackToExitPressedOnce = false;

    private List<UserInformation> userInformationList = new ArrayList<>();
    private DatabaseReference rootRer;
    private DatabaseReference generalUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar_profileLyt = toolbar.findViewById(R.id.toolbar_profile_Lyt);
        toolbarHomePageLyt = toolbar.findViewById(R.id.toolbarHomePageLyt);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.hostFragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

//        /**
//         * firebase database Ref
//         * */
//        rootRer = FirebaseDatabase.getInstance().getReference();
//        generalUserRef = rootRer.child("generalUserTable");
//        loadUserDataList();
//
//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        fm = getSupportFragmentManager();
//
//
//        fm.beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment selectedFragment = null;
//                switch (item.getItemId()) {
//                    case R.id.navigation_home:
//                        selectedFragment = new HomeFragment();
//                        break;
//                    case R.id.navigation_messaging:
//                        selectedFragment = new MessagingFragment();
//                        break;
//                    case R.id.navigation_notification:
//                        selectedFragment = new NotificationFragment();
//                        break;
//                    case R.id.navigation_profile:
//                        selectedFragment = new ProfileFragment();
//                        break;
//
//                }
//                fm.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
//                return true;
//            }
//        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.popBackStack();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(this.getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(getApplication().getResources().getColor(R.color.red_light500));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        setStatus("ofline");
    }
    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        this.menu = menu;
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//
//        switch (item.getItemId()) {
//            case R.id.toolbar_settings:
//
//                break;
//        }

//        //  menu.findItem(R.id.toolbar_search).setVisible(true);
//
//
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//
////        if (active!=homeFragment){
////            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
////            toolbar.setTitle(this.getResources().getString(R.string.app_name));
////            toolbar.setTitleTextColor(getApplication().getResources().getColor(R.color.red_light500));
////
////            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////            getSupportActionBar().setDisplayShowHomeEnabled(true);
////        }
//        invalidateOptionsMenu();
//        if (fm.getBackStackEntryCount() > 0) {
//
//            bottomNavigationView.setVisibility(View.GONE);
//            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
//            toolbar.setTitle(this.getResources().getString(R.string.app_name));
//            toolbar.setTitleTextColor(getApplication().getResources().getColor(R.color.red_light500));
//
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            toolbarHomePageLyt.setVisibility(View.GONE);
//
//            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    fm.popBackStack();
//                }
//            });
//
//        } else {
//
////            if (fm.getBackStackEntryCount() == 0) {
////
////                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Toast.makeText(activity, ""+fm.getBackStackEntryCount(), Toast.LENGTH_SHORT).show();
////                        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
////                        homeClicked = true;
////                        fm.beginTransaction().hide(active).show(homeFragment).commit();
////                        active = homeFragment;
////                        toolbarHomePageLyt.setVisibility(View.VISIBLE);
////                        bottomNavigationView.setVisibility(View.VISIBLE);
////                        toolbar.setNavigationIcon(null);
////                    }
////                });
////
////                //Toast.makeText(activity, "Home Back Space Call", Toast.LENGTH_SHORT).show();
////            }
//            bottomNavigationView.setVisibility(View.VISIBLE);
//            toolbarHomePageLyt.setVisibility(View.VISIBLE);
//        }
//        return super.onPrepareOptionsMenu(menu);
//    }

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
        userInformationList.clear();

        generalUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    UserInformation userInformation = ds.getValue(UserInformation.class);
                    userInformationList.add(userInformation);
                    Log.e(TAG, "Data: " + ds.getValue(ImageSliderData.class).getCreateBy());

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error: " + error.getMessage());

            }
        });
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