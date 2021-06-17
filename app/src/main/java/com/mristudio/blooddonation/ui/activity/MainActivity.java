package com.mristudio.blooddonation.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.ui.fragment.HomeFragment;
import com.mristudio.blooddonation.ui.fragment.MessagingFragment;
import com.mristudio.blooddonation.ui.fragment.NotificationFragment;
import com.mristudio.blooddonation.ui.fragment.PostRequestFragment;
import com.mristudio.blooddonation.ui.fragment.ProfileFragment;
import com.mristudio.blooddonation.model.ImageSliderData;
import com.mristudio.blooddonation.model.UserInformation;
import com.mristudio.blooddonation.utility.Session;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN ACTIVITY";
    public static Toolbar toolbar;
    private Activity activity;
    public static BottomNavigationView bottomNavigationView;
    public static Fragment active;
    public static FragmentManager fm = null;
    public static Fragment homeFragment, messageFragment, notificationFragment, profileFragment;
    public static boolean homeClicked = false, messageClicked = false, notificationClicked = false, profileClicked = false;
    private String from = "";
    Menu menu;
    public static RelativeLayout toolbarHomePageLyt;
    boolean doubleBackToExitPressedOnce = false;

    private List<UserInformation> userInformationList = new ArrayList<>();
    private DatabaseReference rootRer;
    private DatabaseReference generalUserRef;
    private Session session;
    public static TextView toolbarHomePageAvaiableStatusTV, toolbar_Hompage_titleTV;
    private SwitchCompat toolbar_homapage_availableStatusSwitch;
    private LinearLayout toolbarbloodDonateAvailableStatusLyt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = MainActivity.this;
        toolbarHomePageLyt = toolbar.findViewById(R.id.toolbarHomePageLyt);
        toolbarHomePageAvaiableStatusTV = toolbar.findViewById(R.id.toolbarHomePageAvaiableStatusTV);
        toolbar_Hompage_titleTV = toolbar.findViewById(R.id.toolbar_Hompage_titleTV);
        toolbarbloodDonateAvailableStatusLyt = toolbar.findViewById(R.id.toolbarbloodDonateAvailableStatusLyt);
        toolbar_homapage_availableStatusSwitch = toolbar.findViewById(R.id.toolbar_homapage_availableStatusSwitch);


        /**
         * firebase database Ref
         * */
        rootRer = FirebaseDatabase.getInstance().getReference();
        generalUserRef = rootRer.child("generalUserTable");
        loadUserDataList();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fm = getSupportFragmentManager();


        homeFragment = new HomeFragment();
        messageFragment = new MessagingFragment();
        notificationFragment = new NotificationFragment();
        profileFragment = new ProfileFragment();
        // loadFragment(homeFragment);

        if (from.equals("track_order")) {

        } else {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            active = homeFragment;
            homeClicked = true;
            fm.beginTransaction().add(R.id.fragment_container, homeFragment).commit();
            toolbarHomePageLyt.setVisibility(View.VISIBLE);
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        toolbar_Hompage_titleTV.setText("Campaign");
                        toolbarHomePageLyt.setVisibility(View.VISIBLE);
                        toolbar.setNavigationIcon(null);
                        if (!homeClicked) {
                            fm.beginTransaction().add(R.id.fragment_container, homeFragment).show(homeFragment).hide(active).commit();
                            homeClicked = true;
                        } else {
                            fm.beginTransaction().show(homeFragment).hide(active).commit();
                        }
                        active = homeFragment;


                        return true;
                    case R.id.navigation_messaging:
                        if (!messageClicked) {
                            fm.beginTransaction().add(R.id.fragment_container, messageFragment).show(messageFragment).hide(active).commit();
                            messageClicked = true;
                        } else {
                            fm.beginTransaction().show(messageFragment).hide(active).commit();
                        }
                        active = messageFragment;

                        return true;

                    case R.id.navigation_notification:
                        if (!notificationClicked) {
                            fm.beginTransaction().add(R.id.fragment_container, notificationFragment).show(notificationFragment).hide(active).commit();
                            notificationClicked = true;
                        } else {
                            fm.beginTransaction().show(notificationFragment).hide(active).commit();
                        }
                        active = notificationFragment;

                        return true;

                    case R.id.navigation_profile:
                        if (isUserLoggedIn()) {
                            //System.out.println("ACTIVE : " + active);
                            if (!profileClicked) {
                                fm.beginTransaction().add(R.id.fragment_container, profileFragment).show(profileFragment).hide(active).commit();
                                profileClicked = true;
                            } else {
                                fm.beginTransaction().show(profileFragment).hide(active).commit();
                            }
                            active = profileFragment;
                        } else {
                            Toast.makeText(MainActivity.this, "You are not Loging", Toast.LENGTH_SHORT).show();
                        }
                        active = profileFragment;

                        return true;
                }
                return false;
            }
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.navigation_home:
                    case R.id.navigation_messaging:
                    case R.id.navigation_notification:
                    case R.id.navigation_profile:
                        break;
                }
            }
        });

        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment currentFragment = fm.findFragmentById(R.id.fragment_container);
                currentFragment.onResume();
            }
        });


    }

    /**
     * Cheak user Is Logged IN Or Not??
     */
    private boolean isUserLoggedIn() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.toolbar_settings:

                break;
        }

        //  menu.findItem(R.id.toolbar_search).setVisible(true);


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (active!=homeFragment){
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setTitle(this.getResources().getString(R.string.app_name));
            toolbar.setTitleTextColor(getApplication().getResources().getColor(R.color.red_light500));

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        invalidateOptionsMenu();
        if (fm.getBackStackEntryCount() > 0) {

            bottomNavigationView.setVisibility(View.GONE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setTitle(this.getResources().getString(R.string.app_name));
            toolbar.setTitleTextColor(getApplication().getResources().getColor(R.color.red_light500));

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbarHomePageLyt.setVisibility(View.GONE);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fm.popBackStack();
                }
            });

        } else {

            if (fm.getBackStackEntryCount() == 0) {

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(activity, ""+fm.getBackStackEntryCount(), Toast.LENGTH_SHORT).show();
                        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                        homeClicked = true;
                        fm.beginTransaction().hide(active).show(homeFragment).commit();
                        active = homeFragment;
                        toolbarHomePageLyt.setVisibility(View.VISIBLE);
                        bottomNavigationView.setVisibility(View.VISIBLE);
                        toolbar.setNavigationIcon(null);
                    }
                });

                //Toast.makeText(activity, "Home Back Space Call", Toast.LENGTH_SHORT).show();
            }
//            if (active == homeFragment) {
//                toolbarHomePageLyt.setVisibility(View.VISIBLE);
//                bottomNavigationView.setVisibility(View.VISIBLE);
//                toolbar.setNavigationIcon(null);
//            } else {
//
//            }
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        doubleBack();
    }

    public void doubleBack() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Log.e(TAG, " pages= " + fm.getBackStackEntryCount());
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        if (fm.getBackStackEntryCount() == 0) {
            if (active != homeFragment) {
                bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                homeClicked = true;
                fm.beginTransaction().hide(active).show(homeFragment).commit();
                active = homeFragment;
                this.doubleBackToExitPressedOnce = false;
                toolbarHomePageLyt.setVisibility(View.VISIBLE);
                toolbarbloodDonateAvailableStatusLyt.setVisibility(View.VISIBLE);

            } else {
                Toast.makeText(this, "Press Again !", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }

        } else {
            Log.e(TAG, " pages= " + fm.getBackStackEntryCount());
        }

    }


    /**
     * Load SliderImages Data
     */
    private void loadUserDataList() {
        session = new Session(this);
        userInformationList.clear();

        generalUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    UserInformation userInformation = ds.getValue(UserInformation.class);
                    userInformationList.add(userInformation);
                    Log.e(TAG, "Data: " + ds.getValue(ImageSliderData.class).getCreateBy());

                }

//                int position = getIndexByuserId(session.getEmail(), userInformationList);
//                int days = (int) getDaysFromCompairTwoDate(userInformationList.get(position).getLast_donateDate());
//                if (true) {
//                    toolbarHomePageAvaiableStatusTV.setText("available");
//                    toolbar_homapage_availableStatusSwitch.setChecked(true);
//                } else {
//                    toolbarHomePageAvaiableStatusTV.setText("available at " + days + " days");
//                    toolbar_homapage_availableStatusSwitch.setChecked(false);
//                }

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


}