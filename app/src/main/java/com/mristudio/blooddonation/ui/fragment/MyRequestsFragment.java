package com.mristudio.blooddonation.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.firebase.MyDonateRequestAdapter;
import com.mristudio.blooddonation.adapter.firebase.MyDonationAdapter;
import com.mristudio.blooddonation.model.ImageSliderData;
import com.mristudio.blooddonation.model.UserInformation;

import static android.content.ContentValues.TAG;


public class MyRequestsFragment extends Fragment {

    private MyDonateRequestAdapter adapter;
    private RecyclerView rVMyRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_requests, container, false);
        rVMyRequest = view.findViewById(R.id.rVMyRequest);
        rVMyRequest.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirebaseRecyclerOptions<ImageSliderData> options =
                new FirebaseRecyclerOptions.Builder<ImageSliderData>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("userSliderImages"), ImageSliderData.class)
                        .build();
        adapter = new MyDonateRequestAdapter(options);

        Toast.makeText(getActivity(), "" + options.getSnapshots().size(), Toast.LENGTH_SHORT).show();

        Log.e(TAG, "onCreateView: " + options.getSnapshots().size());
        rVMyRequest.setAdapter(adapter);

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