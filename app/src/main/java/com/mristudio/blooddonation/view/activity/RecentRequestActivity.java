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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.RecentRequestCausesSelectedAdapter;
import com.mristudio.blooddonation.adapter.RecentRequestDemoAdapter;
import com.mristudio.blooddonation.model.RequestModel;
import com.mristudio.blooddonation.model.UserInformation;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;

public class RecentRequestActivity extends AppCompatActivity implements RecentRequestDemoAdapter.RecentRequestClickLesenner {

    private RecyclerView rvCaseList, rvPostView;
    List<RequestModel> requestModelList = new ArrayList<>();
    private Toolbar toolbar;
    private UserInformation userInformation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_request);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvCaseList = findViewById(R.id.rvCaseList);
        rvPostView = findViewById(R.id.rvPostView);
        provideCause();
        loadPostByCause("all");
        Log.e(TAG, "onCreateView: call");
    }

    /**
     * Set Layout Three BloodGroup Data
     */
    private void provideCause() {
        List<String> causese = new ArrayList<>();
        causese.add("All");
        causese.add("Dengue");
        causese.add("Accident");
        causese.add("Cancer");
        causese.add("Thalassemia");
        causese.add("Delivery");
        causese.add("Operation");
        causese.add("Others");
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(RecentRequestActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvCaseList.setLayoutManager(gridLayoutManager);
        RecentRequestCausesSelectedAdapter adapter = new RecentRequestCausesSelectedAdapter(RecentRequestActivity.this, causese, new RecentRequestCausesSelectedAdapter.ClickButton() {
            @Override
            public void seLectedBloodGroup(String cause) {
                // Toasty.success(getActivity(), "" + groupName, Toasty.LENGTH_SHORT).show();
                loadPostByCause(cause);
            }
        });
        adapter.setHasStableIds(true);
        rvCaseList.setAdapter(adapter);


    }

    // load all
    private void loadPostByCause(String cause) {
       // Toasty.success(RecentRequestActivity.this, cause, Toasty.LENGTH_SHORT).show();
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(RecentRequestActivity.this, LinearLayoutManager.VERTICAL, false);
        rvPostView.setLayoutManager(gridLayoutManager);
        RecentRequestDemoAdapter adapter = new RecentRequestDemoAdapter(RecentRequestActivity.this);
        adapter.setHasStableIds(true);
        rvPostView.setAdapter(adapter);
        if (cause.equalsIgnoreCase("all")) {
            FirebaseDatabase.getInstance().getReference().child("USER_PUBLIC_POST")
                    .orderByChild("caouse").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.e(TAG, "onDataChange: All Causes" + snapshot.getChildrenCount());
                    requestModelList.clear();

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        RequestModel model = snapshot1.getValue(RequestModel.class);
                        requestModelList.add(model);
                    }
                    adapter.setRecentRequestDemoAdapterData(requestModelList);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: " + error.getMessage());
                }
            });

        } else {

            FirebaseDatabase.getInstance().getReference().child("USER_PUBLIC_POST")
                    .orderByChild("caouse").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.e(TAG, "onDataChange: Other Causes" + snapshot.getChildrenCount());
                    requestModelList.clear();


                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                        RequestModel model = snapshot1.getValue(RequestModel.class);

                        if (model.getcause().equalsIgnoreCase(cause.toLowerCase())) {
                            requestModelList.add(model);
                        }

                    }
                    adapter.setRecentRequestDemoAdapterData(requestModelList);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: " + error.getMessage());

                }
            });
        }


    }

    @Override
    public void donnerPostClick(String tblId) {

        Intent intent = new Intent(RecentRequestActivity.this, PostDetailsActivity.class);
        intent.putExtra("userPostId", tblId);
        startActivity(intent);
        // getActivity().overridePendingTransition( R.anim.left_in, R.anim.left_out);
        Log.e(TAG, "onClick: ");
        overridePendingTransition(R.anim.slide_up, R.anim.no_animation);

    }

    @Override
    public void chatProfile(String tblId) {


        if (tblId != null) {

            Intent intent = new Intent(RecentRequestActivity.this, SendMessageActivity.class);

            intent.putExtra("userChatId", tblId);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
            userInformation = null;
        } else {
            Toasty.warning(RecentRequestActivity.this, "Unable to Send Message !", Toasty.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        getSupportActionBar().setTitle("Recent Request ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();

        getSupportActionBar().setTitle("Recent Request ");
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