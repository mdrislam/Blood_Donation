package com.mristudio.blooddonation.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.ui.activity.MainActivity;


public class NotificationFragment extends Fragment {


    public NotificationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MainActivity.toolbarHomePageLyt.setVisibility(View.GONE);
        menu.findItem(R.id.toolbar_settings).setVisible(true);
        MainActivity.toolbar.setTitle("Notification");
        super.onPrepareOptionsMenu(menu);
    }
}