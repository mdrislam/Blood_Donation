package com.mristudio.blooddonation.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.RatingModel;
import com.mristudio.blooddonation.model.UserInformation;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;

public class DonnerProfileActivity extends AppCompatActivity {
    private static final String TAG = "DonnerProfileActivity";
    private ImageView ivProfile_image;
    private TextView tvDonatdeCounter, tvMyReqCounter, tvLoveCounter, tvBloodGroup, tvStarRatings, tvUserCurrentAddress;
    private LinearLayout llChat, llShare;
    private RecyclerView rvMyPostView;
    private Toolbar toolbar;
    private UserInformation userInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donner_profile);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();

        if (getIntent().getExtras() != null) {
            Log.e(TAG, "onCreate: "+getIntent().getExtras().getString("uId") );

           loadDetailsData(getIntent().getExtras().getString("uId"));
        }
    }

    /**
     * User Profile Info by userId
     */
    private void loadDetailsData(String uId) {
        FirebaseDatabase.getInstance().getReference().child("generalUserTable").child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userInformation = snapshot.getValue(UserInformation.class);
                if(userInformation!=null)
                Picasso.get().load(userInformation.getUserProfilePicture()).into(ivProfile_image);

                DataSnapshot donatesSnapshot = snapshot.child("DONATES");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView() {
        ivProfile_image = findViewById(R.id.ivProfile_image);
        tvDonatdeCounter = findViewById(R.id.tvDonatdeCounter);
        tvMyReqCounter = findViewById(R.id.tvMyReqCounter);
        tvLoveCounter = findViewById(R.id.tvLoveCounter);
        tvBloodGroup = findViewById(R.id.tvBloodGroup);
        tvStarRatings = findViewById(R.id.tvStarRatings);
        tvUserCurrentAddress = findViewById(R.id.tvUserCurrentAddress);
        llChat = findViewById(R.id.llChat);
        llShare = findViewById(R.id.llShare);
        rvMyPostView = findViewById(R.id.rvMyPostView);

        tvStarRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRatingBar();
            }
        });

        llChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userInformation != null) {
                    Intent intent = new Intent(DonnerProfileActivity.this, SendMessageActivity.class);

                    intent.putExtra("userChatId", userInformation.getUserId());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
                }
            }
        });

        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void openRatingBar() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        final View mView = LayoutInflater.from(this).inflate(R.layout.custom_rating_entry_dialog, null);

        //updateButton=mView.findViewById(R.id.updateButton);

        EditText messageEditText = (EditText) mView.findViewById(R.id.messageEditText);
        Button cancelButton = (Button) mView.findViewById(R.id.cancelButton);
        Button submitButton = (Button) mView.findViewById(R.id.submitButton);
        RatingBar ratingBarDialog = (RatingBar) mView.findViewById(R.id.ratingBarDialog);
        mBuilder.setView(mView);
        AlertDialog buttonAlertDialog = mBuilder.create();
        buttonAlertDialog.show();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String commentMessage = messageEditText.getText().toString().trim();
                Float ratingPoint = ratingBarDialog.getRating();
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                    if (commentMessage != null && ratingPoint > 0) {
                        //  saveReview(commentMessage, ratingPoint);

                        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {

                            RatingModel ratingModel = new RatingModel(
                                    FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                    commentMessage, ratingPoint);

                            FirebaseDatabase.getInstance().getReference()
                                    .child("generalUserTable")
                                    .child("UserRatings")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(ratingModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Toasty.success(DonnerProfileActivity.this, "Sucessfully Rate " + ratingPoint, Toasty.LENGTH_SHORT).show();
                                        buttonAlertDialog.dismiss();
                                    }
                                }
                            });


                        }


                    } else {
                        Toasty.warning(DonnerProfileActivity.this, "Something was Wrong", Toasty.LENGTH_SHORT).show();
                    }
                } else {

                    Toasty.warning(DonnerProfileActivity.this, "Please ! Loging First.", Toasty.LENGTH_SHORT).show();

                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAlertDialog.dismiss();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        getSupportActionBar().setTitle("Profile ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();

        getSupportActionBar().setTitle("Profile ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
        //overridePendingTransition(R.anim.right_in, R.anim.right_out);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        finish();
        return true;
    }
}