package com.mristudio.blooddonation.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.firebase.MyDonationAdapter;
import com.mristudio.blooddonation.model.Donation;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView myDonationListRV;
    MyDonationAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        myDonationListRV = findViewById(R.id.myDonationListRV);
        myDonationListRV.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<Donation> options =
                new FirebaseRecyclerOptions.Builder<Donation>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("generaUserTable")
                                .child("7nSindmtQlcUxqousEFPs3BEKak1")
                                .child("donationListTable"), Donation.class)
                        .build();

        adapter = new MyDonationAdapter(options);

        myDonationListRV.setAdapter(adapter);
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