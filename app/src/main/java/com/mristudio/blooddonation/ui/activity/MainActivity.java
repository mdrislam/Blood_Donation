package com.mristudio.blooddonation.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

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

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.Slider_Pager_Adapter;
import com.mristudio.blooddonation.fragment.HomeFragment;
import com.mristudio.blooddonation.fragment.MessagingFragment;
import com.mristudio.blooddonation.fragment.NotificationFragment;
import com.mristudio.blooddonation.fragment.PostRequestFragment;
import com.mristudio.blooddonation.fragment.ProfileFragment;
import com.mristudio.blooddonation.model.ImageSliderData;
import com.mristudio.blooddonation.model.UserInformation;
import com.mristudio.blooddonation.utility.Session;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import q.rorbin.badgeview.QBadgeView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainActivity extends AppCompatActivity implements HomeFragment.ChangeFragment{

    private static final String TAG = "MAIN ACTIVITY";
    public static Toolbar toolbar;
    public static BottomNavigationView bottomNavigationView;
    public static Fragment active;
    public static FragmentManager fm = null;
    public static Fragment homeFragment, messageFragment, notificationFragment, profileFragment;
    public static boolean homeClicked = false, messageClicked = false, notificationClicked = false, profileClicked = false;
    private String from = "home";
    Menu menu;
    public static RelativeLayout toolbarHomePageLyt;
    boolean doubleBackToExitPressedOnce = false;

    private List<UserInformation> userInformationList = new ArrayList<>();
    private DatabaseReference rootRer;
    private DatabaseReference generalUserRef;
    private Session session;
    private TextView toolbarHomePageAvaiableStatusTV;
    private SwitchCompat toolbar_homapage_availableStatusSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarHomePageLyt = toolbar.findViewById(R.id.toolbarHomePageLyt);
        toolbarHomePageAvaiableStatusTV = toolbar.findViewById(R.id.toolbarHomePageAvaiableStatusTV);
        toolbar_homapage_availableStatusSwitch = toolbar.findViewById(R.id.toolbar_homapage_availableStatusSwitch);


        /**
         *
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
        loadFragment(homeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        if (!homeClicked) {
                            fm.beginTransaction().add(R.id.fragment_container, homeFragment).show(homeFragment).hide(active).commit();
                            homeClicked = true;
                        } else {
                            fm.beginTransaction().show(homeFragment).hide(active).commit();
                        }
                        active = homeFragment;
                        toolbarHomePageLyt.setVisibility(View.VISIBLE);
                        menu.findItem(R.id.toolbar_settings).setVisible(false);
                        menu.findItem(R.id.toolbar_search).setVisible(false);
                        menu.findItem(R.id.toolbar_addRequest).setVisible(false);
                        menu.findItem(R.id.toolbar_locationOn).setVisible(false);

                        return true;
                    case R.id.navigation_messaging:
                        if (!messageClicked) {
                            fm.beginTransaction().add(R.id.fragment_container, messageFragment).show(messageFragment).hide(active).commit();
                            messageClicked = true;
                            toolbarHomePageLyt.setVisibility(View.GONE);
                        } else {
                            fm.beginTransaction().show(messageFragment).hide(active).commit();
                            toolbarHomePageLyt.setVisibility(View.GONE);
                        }
                        active = messageFragment;
                        toolbarHomePageLyt.setVisibility(View.GONE);
                        menu.findItem(R.id.toolbar_settings).setVisible(true);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        return true;

                    case R.id.navigation_notification:
                        if (!notificationClicked) {
                            toolbarHomePageLyt.setVisibility(View.GONE);
                            fm.beginTransaction().add(R.id.fragment_container, notificationFragment).show(notificationFragment).hide(active).commit();
                            notificationClicked = true;
                        } else {
                            toolbarHomePageLyt.setVisibility(View.GONE);
                            fm.beginTransaction().show(notificationFragment).hide(active).commit();
                        }
                        active = notificationFragment;
                        toolbarHomePageLyt.setVisibility(View.GONE);
                        menu.findItem(R.id.toolbar_settings).setVisible(true);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        return true;

                    case R.id.navigation_profile:
                        if (isUserLoggedIn()) {
                            //System.out.println("ACTIVE : " + active);
                            if (!profileClicked) {
                                toolbarHomePageLyt.setVisibility(View.GONE);
                                fm.beginTransaction().add(R.id.fragment_container, profileFragment).show(profileFragment).hide(active).commit();
                                profileClicked = true;
                            } else {
                                toolbarHomePageLyt.setVisibility(View.GONE);
                                fm.beginTransaction().show(profileFragment).hide(active).commit();
                            }
                            active = profileFragment;
                        } else {
                            Toast.makeText(MainActivity.this, "You are not Loging", Toast.LENGTH_SHORT).show();
                        }
                        toolbarHomePageLyt.setVisibility(View.GONE);
                        menu.findItem(R.id.toolbar_settings).setVisible(true);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        return true;
                }
                return false;
            }
        });


    }

    /**
     * Cheak user Is Logged IN Or Not??
     */
    private boolean isUserLoggedIn() {
        return true;
    }

    /**
     * Load Selected Fragment
     */
    private void loadFragment(Fragment fragment) {
        if (homeFragment == fragment) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            active = homeFragment;
            homeClicked = true;
            toolbarHomePageLyt.setVisibility(View.VISIBLE);
            fm.beginTransaction().add(R.id.fragment_container, homeFragment).commit();
        }else {

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (!homeClicked) {
            toolbarHomePageLyt.setVisibility(View.GONE);
            menu.findItem(R.id.toolbar_settings).setVisible(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } else {
            toolbarHomePageLyt.setVisibility(View.VISIBLE);
            menu.findItem(R.id.toolbar_settings).setVisible(false);
            menu.findItem(R.id.toolbar_search).setVisible(false);
            menu.findItem(R.id.toolbar_addRequest).setVisible(false);
            menu.findItem(R.id.toolbar_locationOn).setVisible(false);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.toolbar_settings:

                break;
        }

        //  menu.findItem(R.id.toolbar_search).setVisible(true);

        if (fm.getBackStackEntryCount() > 0) {

            // bottomNavigationView.setVisibility(View.GONE);

            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setTitle(this.getResources().getString(R.string.app_name));
            toolbar.setTitleTextColor(getApplication().getResources().getColor(R.color.red_light500));

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fm.popBackStack();
                    Toast.makeText(MainActivity.this, "On Option Button Click", Toast.LENGTH_SHORT).show();
                }
            });

        }

        return super.onOptionsItemSelected(item);
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
            if (fm.findFragmentById(R.id.container) != homeFragment) {
                bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                homeClicked = true;
                fm.beginTransaction().hide(active).show(homeFragment).commit();
                active = homeFragment;
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

    @Override
    public void callPostRequestFragment(PostRequestFragment fragment) {
        fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        toolbarHomePageLyt.setVisibility(View.GONE);
        menu.findItem(R.id.toolbar_settings).setVisible(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Post blood  for Request");
    }
}