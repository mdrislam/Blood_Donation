package com.mristudio.blooddonation.view.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.view.activity.MainActivity;


public class NotificationFragment extends Fragment {


    public NotificationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        TextView tittleTv = MainActivity.toolbar.findViewById(R.id.toolbar_Hompage_titleTV);
        tittleTv.setText("LPI BLOOD BANK");
//        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
       // MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();

//        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
       // MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
    }
}