package com.mristudio.blooddonation.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.view.activity.UserSignInActivity;
import com.mristudio.blooddonation.utility.IntroPrefManager;

public class WelcomeScreenFragmentThree extends Fragment {
    private Button buttonStart;
    private IntroPrefManager introPrefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome_screen3, container, false);
        buttonStart = v.findViewById(R.id.buttonStart);
        introPrefManager = new IntroPrefManager(getActivity());
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getActivity(), "New Activity  Called ", Toast.LENGTH_SHORT).show();
                introPrefManager.setFirstTimeLaunch(false);
                startActivity(new Intent(getActivity(), UserSignInActivity.class));
                getActivity().finish();
            }
        });
        return v;
    }

    public static WelcomeScreenFragmentThree newInstance() {
        WelcomeScreenFragmentThree fragment = new WelcomeScreenFragmentThree();
        return fragment;
    }

}
