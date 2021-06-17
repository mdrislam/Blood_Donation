package com.mristudio.blooddonation.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.AdminTransectionAdapter;
import com.mristudio.blooddonation.model.AdminDataModel;
import com.mristudio.blooddonation.model.ImageSliderData;

import java.util.ArrayList;
import java.util.List;

public class ManageUserActivity extends AppCompatActivity implements AdminTransectionAdapter.AdminViewClick {
    private static final String TAG = "UserListActivity";
    private List<AdminDataModel> userInformationList = new ArrayList<>();
    private RecyclerView manageuserRV;

    private DatabaseReference rootRer;
    private DatabaseReference generalUserRef;
    private DatabaseReference adminRef;
    private GridLayoutManager gridLayoutManager;
    private AdminTransectionAdapter adapter;
    private Toolbar toolbar;
    private ImageButton adminToolBarBack;
    private TextView adminHomePageTittle;
    private Button addAdminButton;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);
        toolbar = findViewById(R.id.toolbar);
        adminToolBarBack = toolbar.findViewById(R.id.adminToolBarBack);
        adminHomePageTittle = toolbar.findViewById(R.id.adminHomePageTittle);
        manageuserRV = findViewById(R.id.manageuserRV);
        addAdminButton = findViewById(R.id.addAdminButton);
        rootRer = FirebaseDatabase.getInstance().getReference();
        generalUserRef = rootRer.child("generalUserTable");
        rootRer = FirebaseDatabase.getInstance().getReference();
        adminRef = rootRer.child("userAdminTable");
        adminHomePageTittle.setText("Manage user");
        loadAdminDataList();
        adminToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageUserActivity.this, UserListActivity.class));
            }
        });
    }

    /**
     * Load SliderImages Data
     */
    private void loadAdminDataList() {
        progressDialog();
        userInformationList.clear();
        gridLayoutManager = new GridLayoutManager(this, 1);
        manageuserRV.setLayoutManager(gridLayoutManager);
        adapter = new AdminTransectionAdapter(userInformationList, this);
        adapter.setHasStableIds(true);

        adminRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    AdminDataModel userInformation = ds.getValue(AdminDataModel.class);
                    userInformationList.add(userInformation);
                    Log.e(TAG, "Data: " + ds.getValue(ImageSliderData.class).getCreateBy());
                }
                manageuserRV.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Log.e(TAG, "Size: " + userInformationList.size());
                progress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                progress.dismiss();
            }
        });


    }

    @Override
    public void adminLayoutClick(AdminDataModel adminDataModel) {

    }

    @Override
    public void adminLayoutClickUpdateAccess(AdminDataModel adminDataModel, String userType, Boolean isAdmin, AlertDialog buttonAlertDialog) {

    }

    @Override
    public void adminLayoutClickDeleteUser(AdminDataModel adminDataModel, AlertDialog buttonAlertDialog) {

    }

    /**
     * Load ProgressDialog
     */
    public void progressDialog() {
        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait a Several Time.....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }
}