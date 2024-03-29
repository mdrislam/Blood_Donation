package com.mristudio.blooddonation.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.ViewPagerAdapter;
import com.mristudio.blooddonation.view.fragment.ContactsFragment;
import com.mristudio.blooddonation.view.fragment.MyChatsFragment;

public class MyRequestActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    ViewPagerAdapter  adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_request);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupTabLayout();
    }
    private void setupTabLayout( ) {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);


        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        tabLayout.addTab(tabLayout.newTab().setText("MY REQUESTS"));
        tabLayout.addTab(tabLayout.newTab().setText("ACCEPTED REQUESTS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onResume() {
        super.onResume();

        getSupportActionBar().setTitle("My Request ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();

        getSupportActionBar().setTitle("My Request ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        finish();
        return true;
    }
}