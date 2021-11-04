package com.mristudio.blooddonation.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.firebase.MyDonationAdapter;
import com.mristudio.blooddonation.model.Donation;


public class MyDonationListFragment extends Fragment {

    private RecyclerView myDonationListRV;
    MyDonationAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_donation_list, container, false);
        myDonationListRV = view.findViewById(R.id.myDonationListRV);
        myDonationListRV.setLayoutManager(new LinearLayoutManager(getContext()));


        FirebaseRecyclerOptions<Donation> options =
                new FirebaseRecyclerOptions.Builder<Donation>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("generaUserTable")
                                .child("7nSindmtQlcUxqousEFPs3BEKak1")
                                .child("donationListTable"), Donation.class)
                        .build();

        adapter = new MyDonationAdapter(options);

        myDonationListRV.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}