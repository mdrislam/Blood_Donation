package com.mristudio.blooddonation.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.AllUserListAdapter;
import com.mristudio.blooddonation.model.AdminDataModel;
import com.mristudio.blooddonation.model.ImageSliderData;
import com.mristudio.blooddonation.model.UserInformation;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity implements AllUserListAdapter.AdminViewClick {
    private static final String TAG = "UserListActivity";
    private List<UserInformation> userInformationList = new ArrayList<>();
    private RecyclerView userListRechyclerView;

    private DatabaseReference rootRer;
    private DatabaseReference adminRef;
    private DatabaseReference generalUserRef;
    private GridLayoutManager gridLayoutManager;
    private AllUserListAdapter adapter;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        userListRechyclerView = findViewById(R.id.userListRechyclerView);
        rootRer = FirebaseDatabase.getInstance().getReference();
        adminRef = rootRer.child("userAdminTable");
        generalUserRef = rootRer.child("generalUserTable");

        loadAdminDataList();

    }

    /**
     * Load SliderImages Data
     */
    private void loadAdminDataList() {
        userInformationList.clear();
        gridLayoutManager = new GridLayoutManager(this, 1);
        userListRechyclerView.setLayoutManager(gridLayoutManager);
        adapter = new AllUserListAdapter(userInformationList, this);
        adapter.setHasStableIds(true);

        generalUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    UserInformation userInformation = ds.getValue(UserInformation.class);
                    userInformationList.add(userInformation);
                    Log.e(TAG, "Data: " + ds.getValue(ImageSliderData.class).getCreateBy());

                }
                userListRechyclerView.setAdapter(adapter);
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
    public void adminLayoutClickAccess(UserInformation userInformation) {
        AdminDataModel adminDataModel = new AdminDataModel();
        adminDataModel.setAdmin(true);
        adminDataModel.setEmail(userInformation.getemail());
        adminDataModel.setName(userInformation.getName());
        adminDataModel.setName(userInformation.getName());
        adminDataModel.setPassword(userInformation.getPassword());
        adminDataModel.setUserType("admin");
        String adminId = adminRef.push().getKey();
        adminDataModel.setUserId(adminId);

        adminRef.child(adminId).setValue(adminDataModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onBackPressed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
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