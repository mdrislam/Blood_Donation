package com.mristudio.blooddonation.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.activity.MainActivity;

public class MessagingFragment extends Fragment {


    public MessagingFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messaging, container, false);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MainActivity.toolbarHomePageLyt.setVisibility(View.GONE);

        return super.onOptionsItemSelected(item);
    }
}