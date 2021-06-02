package com.mristudio.blooddonation.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.AddDonateDateAdapter;
import com.mristudio.blooddonation.adapter.AllUserListAdapter;
import com.mristudio.blooddonation.model.AdminDataModel;
import com.mristudio.blooddonation.model.ImageSliderData;
import com.mristudio.blooddonation.model.UserInformation;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ManageDonationActivity extends AppCompatActivity implements AddDonateDateAdapter.AdminViewClick {
    private static final String TAG = "UserListActivity";
    private List<UserInformation> userInformationList = new ArrayList<>();
    private RecyclerView userListRechyclerView;

    private DatabaseReference rootRer;
    private DatabaseReference adminRef;
    private DatabaseReference generalUserRef;
    private GridLayoutManager gridLayoutManager;
    private AddDonateDateAdapter adapter;
    private ProgressDialog progress;
    private Toolbar toolbar;
    private ImageButton backButton;
    private TextView adminHomePageTittle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_janage_donation);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        backButton = toolbar.findViewById(R.id.adminToolBarBack);
        adminHomePageTittle = toolbar.findViewById(R.id.adminHomePageTittle);
        adminHomePageTittle.setText("Add Donate Date ");
        userListRechyclerView = findViewById(R.id.userListRechyclerView);
        rootRer = FirebaseDatabase.getInstance().getReference();
        adminRef = rootRer.child("userAdminTable");
        generalUserRef = rootRer.child("generalUserTable");

        loadAdminDataList();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
        userListRechyclerView.setLayoutManager(gridLayoutManager);
        adapter = new AddDonateDateAdapter(userInformationList, this);
        adapter.setHasStableIds(true);

        generalUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

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

    @Override
    public void updateUserDonateBloodDate(UserInformation userInformation, AlertDialog buttonAlertDialog) {
        generalUserRef.child(userInformation.getUserId()).setValue(userInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toasty.success(ManageDonationActivity.this, "Sucessfully Update  Donate Date !", Toast.LENGTH_SHORT).show();
                loadAdminDataList();
                buttonAlertDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(ManageDonationActivity.this, "Something is wrong try again !", Toast.LENGTH_SHORT).show();

            }
        });
    }
}