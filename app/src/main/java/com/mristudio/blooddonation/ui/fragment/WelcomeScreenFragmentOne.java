package com.mristudio.blooddonation.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.ui.activity.WelcomeActivity;

public class WelcomeScreenFragmentOne extends Fragment {
    public static Button  buttonStartJourney;

    public static WelcomeScreenFragmentOne newInstance() {
        WelcomeScreenFragmentOne fragment = new WelcomeScreenFragmentOne();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome_screen1, container, false);
        buttonStartJourney= v.findViewById(R.id.buttonStartJourney);


        buttonStartJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WelcomeActivity.setCurrentItem(1);
            }
        });
        return v;
    }



}
