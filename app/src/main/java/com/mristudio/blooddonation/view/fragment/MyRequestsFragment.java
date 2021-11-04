package com.mristudio.blooddonation.view.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.ProfileViewPagerAdapter;
import com.mristudio.blooddonation.view.activity.MainActivity;


public class MyRequestsFragment extends Fragment {


    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_requests, container, false);
        setupTabLayout(view);
        return view;
    }


    private void setupTabLayout(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("MY REQUESTS"));
        tabLayout.addTab(tabLayout.newTab().setText("ACCEPTED REQUESTS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ProfileViewPagerAdapter adapter = new ProfileViewPagerAdapter(getActivity(), getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.toolbar_profileLyt.setVisibility(View.GONE);
        MainActivity.toolbarHomePageLyt.setVisibility(View.GONE);

        MainActivity.toolbar.setTitle("My Request ");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();

        MainActivity.toolbar_profileLyt.setVisibility(View.GONE);
        MainActivity.toolbarHomePageLyt.setVisibility(View.GONE);

        MainActivity.toolbar.setTitle("My Request ");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }
}