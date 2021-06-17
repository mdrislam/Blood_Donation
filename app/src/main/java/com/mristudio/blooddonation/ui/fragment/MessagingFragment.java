package com.mristudio.blooddonation.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.ui.activity.MainActivity;

public class MessagingFragment extends Fragment {


    public MessagingFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messaging, container, false);
        MainActivity.toolbar_Hompage_titleTV.setText("Message");

        TextView clickOn = view.findViewById(R.id.clickOn);
        clickOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostRequestFragment()).addToBackStack(null).commit();
            }
        });

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
      //  getActivity()
        MainActivity.toolbarHomePageLyt.setVisibility(View.GONE);
        menu.findItem(R.id.toolbar_settings).setVisible(true);
        MainActivity.toolbar.setTitle("Message");
        super.onPrepareOptionsMenu(menu);
    }


}