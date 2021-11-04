package com.mristudio.blooddonation.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.RequestModel;
import com.mristudio.blooddonation.view.activity.MainActivity;
import com.squareup.picasso.Picasso;


public class FindDonnerDetailsFragment extends Fragment {
    private static final String TAG = "FindDonnerDetailsFragment";
    private TextView tvbloodName, tvGender, tvUnits, tvTotalAccept, tvDonateDateTime, tvCause, tvDonateLocation;
    private TextView tVProfileName, tVLokingFor, tvDonateDatetimeAndPostTime, tVDetails, tvAcceptclk, tvDeclineClk, tvShareClk;
    private ImageView ivProfileImage, ivPostImage;

    public FindDonnerDetailsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_donner_details, container, false);

        initView(view);

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            loadDetailsData(bundle.getString("userPostId"));
        }
        return view;
    }

    private void initView(View view) {

        tvbloodName = view.findViewById(R.id.tvbloodName);
        tvGender = view.findViewById(R.id.tvGender);
        tvUnits = view.findViewById(R.id.tvUnits);
        tvTotalAccept = view.findViewById(R.id.tvTotalAccept);
        tvDonateDateTime = view.findViewById(R.id.tvDonateDateTime);
        tvCause = view.findViewById(R.id.tvCause);
        tvDonateLocation = view.findViewById(R.id.tvDonateLocation);
        tVProfileName = view.findViewById(R.id.tVProfileName);
        tVLokingFor = view.findViewById(R.id.tVLokingFor);
        tvDonateDatetimeAndPostTime = view.findViewById(R.id.tvDonateDatetimeAndPostTime);
        tVDetails = view.findViewById(R.id.tVDetails);
        tvAcceptclk = view.findViewById(R.id.tvAcceptclk);
        tvDeclineClk = view.findViewById(R.id.tvDeclineClk);
        tvShareClk = view.findViewById(R.id.tvShareClk);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        ivPostImage = view.findViewById(R.id.ivPostImage);

        tvAcceptclk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tvDeclineClk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tvShareClk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    /**
     * get User Post Details Using TblId
     */
    private void loadDetailsData(String userPostId) {
        FirebaseDatabase.getInstance().getReference().child("USER_PUBLIC_POST").child(userPostId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    RequestModel model = snapshot.getValue(RequestModel.class);
                    DataSnapshot totalAccept = snapshot.child("ACCEPT");

                    if (model != null) {

                        tvbloodName.setText(model.getBloodGroup());
                        tvGender.setText(model.getGender());
                        tvUnits.setText(model.getUnits().toString());
                        // tvTotalAccept,
                        tvDonateDateTime.setText(model.getDate() + " " + model.getTime());
                        tvCause.setText(model.getcause());
                        tvDonateLocation.setText(model.getAddressofHospital());
                        tVProfileName.setText(model.getUserProfileName());
                        tVLokingFor.setText("Loking For " + model.getBloodGroup());
                        tvDonateDatetimeAndPostTime.setText("-- Day ago - " + model.getAddressofHospital());
                        tVDetails.setText(model.getRequestMessage());
                        Picasso.get().load(model.getUserProfileImageUrl()).into(ivProfileImage);
                        Picasso.get().load(model.getImagesUrl()).into(ivPostImage);

                    } else {
                        Log.e(TAG, "Model Is Null:");
                        getActivity().startActivity(new Intent(getActivity(), MainActivity.class));

                    }

                } else {
                    Log.e(TAG, "onCancelled: Some thing was Wrong");
                    getActivity().startActivity(new Intent(getActivity(), MainActivity.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e(TAG, "onCancelled: " + error.getMessage());
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.toolbar_profileLyt.setVisibility(View.GONE);
        MainActivity.toolbarHomePageLyt.setVisibility(View.GONE);

        MainActivity.toolbar.setTitle("Details");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();

        MainActivity.toolbar_profileLyt.setVisibility(View.GONE);
        MainActivity.toolbarHomePageLyt.setVisibility(View.GONE);

        MainActivity.toolbar.setTitle("Details");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }
}