package com.mristudio.blooddonation.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.firebase.MyDonationAdapter;
import com.mristudio.blooddonation.model.Donation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

public class HistoryActivity extends AppCompatActivity {
    private List<Donation> donationList = new ArrayList<>();
    private RecyclerView myDonationListRV;
    MyDonationAdapter adapter;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myDonationListRV = findViewById(R.id.myDonationListRV);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        user = FirebaseAuth.getInstance().getCurrentUser();
        adapter = new MyDonationAdapter(this);
        myDonationListRV.setLayoutManager(new LinearLayoutManager(this));
        myDonationListRV.setHasFixedSize(true);
        myDonationListRV.setAdapter(adapter);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, InsertDonateActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("generalUserTable").child(user.getUid()).child("DONATIONS")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            donationList.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Donation donation = snapshot.getValue(Donation.class);
                                donationList.add(donation);
                            }

                            Collections.reverse(donationList);
                            adapter.setDonationData(donationList);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();

        getSupportActionBar().setTitle("History ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public void onPause() {
        super.onPause();

        getSupportActionBar().setTitle("History ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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