package com.mristudio.blooddonation.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    private ArrayList<String> tittles;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<>();
        this.tittles = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String tittle) {
        fragments.add(fragment);
        tittles.add(tittle);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tittles.get(position);
    }

    public void setTittles(int position, String tittle){
        tittles.set(position,tittle);
        notifyDataSetChanged();
    }
}
