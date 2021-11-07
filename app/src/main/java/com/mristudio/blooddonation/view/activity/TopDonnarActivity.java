package com.mristudio.blooddonation.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.TopDonnerDemoAdapter;
import com.mristudio.blooddonation.model.RatingModel;
import com.mristudio.blooddonation.model.UserInformation;

import java.util.ArrayList;
import java.util.List;

public class TopDonnarActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private static final String TAG = "TopDonnarActivity";
    RecyclerView rvTopDonner;
    private List<UserInformation> userInformationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_donnar);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvTopDonner = findViewById(R.id.rvTopDonner);
        LinearLayoutManager llm = new LinearLayoutManager(TopDonnarActivity.this);
        rvTopDonner.setLayoutManager(llm);

        TopDonnerDemoAdapter adapter = new TopDonnerDemoAdapter(TopDonnarActivity.this, userInformationList, new TopDonnerDemoAdapter.TopDonnerClickLesenner() {
            @Override
            public void donnerPostClick(String userId) {
//                Bundle bundle = new Bundle();
//                bundle.putString("uId", userId); // Put anything what you want
//                DonnerProfileFragment donnerProfileFragment = new DonnerProfileFragment();
//                donnerProfileFragment.setArguments(bundle);
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left,R.anim.enter_left_to_right,R.anim.exit_left_to_right)
//                        .replace(R.id.fragment_container, donnerProfileFragment).addToBackStack(null).commit();
                Intent intent = new Intent(TopDonnarActivity.this, DonnerProfileActivity.class);
                intent.putExtra("uId", userId);
                startActivity(intent);
                // overridePendingTransition(R.anim.left_in, R.anim.left_out);
                overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
                // getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);

            }
        });


        FirebaseDatabase.getInstance().getReference().child("generalUserTable").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    userInformationList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);

                        DataSnapshot ratingSnapshort = dataSnapshot.child("RATINGS");
                        if (ratingSnapshort.exists()) {
                            List<RatingModel> ratingModelList = new ArrayList<>();

                            for (DataSnapshot rating : ratingSnapshort.getChildren()) {
                                RatingModel ratingModel = rating.getValue(RatingModel.class);
                                ratingModelList.add(ratingModel);
                            }

                            userInformation.setRatingModelList(ratingModelList);

                        } else {
                            Log.e(TAG, "onDataChange: Rating Not found ");
                        }

                        userInformationList.add(userInformation);
                    }
                    rvTopDonner.setAdapter(adapter);

                } else {
                    Log.e(TAG, "onDataChange: ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.getMessage());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        getSupportActionBar().setTitle("Top Donners ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();

        getSupportActionBar().setTitle("Top Donners ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        finish();
        return true;
    }
}