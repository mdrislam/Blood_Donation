package com.mristudio.blooddonation.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.ui.activity.WelcomeActivity;

public class WelcomeScreenFragmentTwo extends Fragment {
    private Button buttonNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome_screen2, container, false);
        buttonNext= v.findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WelcomeActivity.setCurrentItem(3);
            }
        });
        return v;
    }

    public static WelcomeScreenFragmentTwo newInstance() {
        WelcomeScreenFragmentTwo fragment = new WelcomeScreenFragmentTwo();
        return fragment;
    }

}
