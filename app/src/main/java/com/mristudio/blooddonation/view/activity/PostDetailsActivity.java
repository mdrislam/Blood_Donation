package com.mristudio.blooddonation.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.RequestModel;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;

public class PostDetailsActivity extends AppCompatActivity {

    private static final String TAG = "PostDetailsActivity";
    private Toolbar toolbar;
    private TextView tvbloodName, tvGender, tvUnits, tvTotalAccept, tvDonateDateTime, tvCause, tvDonateLocation;
    private TextView tVProfileName, tVLokingFor, tvDonateDatetimeAndPostTime, tVDetails, tvAcceptclk, tvDeclineClk, tvShareClk;
    private ImageView ivProfileImage, ivPostImage;
    private RequestModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();


        if (getIntent().getExtras() != null) {

            loadDetailsData(getIntent().getExtras().getString("userPostId"));
        }
    }

    private void initView() {

        tvbloodName = findViewById(R.id.tvbloodName);
        tvGender = findViewById(R.id.tvGender);
        tvUnits = findViewById(R.id.tvUnits);
        tvTotalAccept = findViewById(R.id.tvTotalAccept);
        tvDonateDateTime = findViewById(R.id.tvDonateDateTime);
        tvCause = findViewById(R.id.tvCause);
        tvDonateLocation = findViewById(R.id.tvDonateLocation);
        tVProfileName = findViewById(R.id.tVProfileName);
        tVLokingFor = findViewById(R.id.tVLokingFor);
        tvDonateDatetimeAndPostTime = findViewById(R.id.tvDonateDatetimeAndPostTime);
        tVDetails = findViewById(R.id.tVDetails);
        tvAcceptclk = findViewById(R.id.tvAcceptclk);
        tvDeclineClk = findViewById(R.id.tvDeclineClk);
        tvShareClk = findViewById(R.id.tvShareClk);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        ivPostImage = findViewById(R.id.ivPostImage);

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
        ivPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model != null) {

                    Intent intent = new Intent(PostDetailsActivity.this, ImageActivity.class);
                    intent.putExtra("imageurl", model.getImagesUrl());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
                } else {
                    Toasty.error(PostDetailsActivity.this, "Unable to Open Image", Toasty.LENGTH_SHORT).show();
                }
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
                    model = snapshot.getValue(RequestModel.class);
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

                        Picasso.get().load(model.getImagesUrl()).into(ivPostImage);

                        if (model.getUserProfileImageUrl().equalsIgnoreCase("null")) {
                            ivProfileImage.setImageResource(R.drawable.female_icon);
                        } else if (model.getUserProfileImageUrl() == null) {
                            ivProfileImage.setImageResource(R.drawable.female_icon);
                        } else {
                            Picasso.get().load(model.getUserProfileImageUrl()).into(ivProfileImage);
                        }

                    } else {
                        Log.e(TAG, "Model Is Null:");
                        startActivity(new Intent(PostDetailsActivity.this, MainActivity.class));

                    }

                } else {
                    Log.e(TAG, "onCancelled: Some thing was Wrong");
                    startActivity(new Intent(PostDetailsActivity.this, MainActivity.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e(TAG, "onCancelled: " + error.getMessage());
                startActivity(new Intent(PostDetailsActivity.this, MainActivity.class));

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();


        getSupportActionBar().setTitle(" Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        // MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();

        getSupportActionBar().setTitle(" Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getExtras().getBoolean("status")) {
            startActivity(new Intent(PostDetailsActivity.this, MainActivity.class));

        } else {
            overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        if (getIntent().getExtras().getBoolean("status")) {
            startActivity(new Intent(PostDetailsActivity.this, MainActivity.class));

        } else {
            onBackPressed();
            finish();
        }
        return true;
    }
}