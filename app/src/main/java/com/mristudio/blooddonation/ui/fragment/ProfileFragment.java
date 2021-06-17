package com.mristudio.blooddonation.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.ProfileViewPagerAdapter;
import com.mristudio.blooddonation.ui.activity.MainActivity;

public class ProfileFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);
        setupTabLayout(view);
        return view;
    }

    private void setupTabLayout(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Donation"));
        tabLayout.addTab(tabLayout.newTab().setText("Request"));
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
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MainActivity.toolbarHomePageLyt.setVisibility(View.GONE);
        menu.findItem(R.id.toolbar_settings).setVisible(true);
        MainActivity.toolbar.setTitle("Profile");
        super.onPrepareOptionsMenu(menu);
    }
}