package com.mristudio.blooddonation.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mristudio.blooddonation.ui.fragment.MyDonationListFragment;
import com.mristudio.blooddonation.ui.fragment.MyRequestsFragment;

import static com.mristudio.blooddonation.ui.activity.MainActivity.fm;

public class ProfileViewPagerAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public ProfileViewPagerAdapter(Context myContext, FragmentManager fm, int totalTabs) {
        super(fm);
        this.myContext = myContext;
        this.totalTabs = totalTabs;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MyDonationListFragment myDonationListFragment = new MyDonationListFragment();
                return myDonationListFragment;
            case 1:
                MyRequestsFragment myRequestsFragment = new MyRequestsFragment();
                return myRequestsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
