package com.mristudio.blooddonation.view.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class DonnerProfileFragment extends Fragment {

    private static final String TAG = "DonnerProfileFragment";
    private ImageView ivProfile_image;
    private TextView tvDonatdeCounter, tvMyReqCounter, tvLoveCounter, tvBloodGroup, tvStarRatings, tvUserCurrentAddress;
    private LinearLayout llChat, llShare;
    private RecyclerView rvMyPostView;

    public DonnerProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donner_profile, container, false);

        initView(view);
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            loadDetailsData(bundle.getString("uId"));
        }
        return view;
    }

    /**
     * User Profile Info by userId
     */
    private void loadDetailsData(String uId) {
        FirebaseDatabase.getInstance().getReference().child("generalUserTable").child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInformation userInformation = snapshot.getValue(UserInformation.class);
                Picasso.get().load(userInformation.getUserProfilePicture()).into(ivProfile_image);


                DataSnapshot donatesSnapshot = snapshot.child("DONATES");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView(View view) {
        ivProfile_image = view.findViewById(R.id.ivProfile_image);
        tvDonatdeCounter = view.findViewById(R.id.tvDonatdeCounter);
        tvMyReqCounter = view.findViewById(R.id.tvMyReqCounter);
        tvLoveCounter = view.findViewById(R.id.tvLoveCounter);
        tvBloodGroup = view.findViewById(R.id.tvBloodGroup);
        tvStarRatings = view.findViewById(R.id.tvStarRatings);
        tvUserCurrentAddress = view.findViewById(R.id.tvUserCurrentAddress);
        llChat = view.findViewById(R.id.llChat);
        llShare = view.findViewById(R.id.llShare);
        rvMyPostView = view.findViewById(R.id.rvMyPostView);

        tvStarRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRatingBar();
            }
        });

        llChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void openRatingBar() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        final View mView = LayoutInflater.from(getActivity()).inflate(R.layout.custom_rating_entry_dialog, null);

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

                                        Toasty.success(getActivity(), "Sucessfully Rate " + ratingPoint, Toasty.LENGTH_SHORT).show();
                                        buttonAlertDialog.dismiss();
                                    }
                                }
                            });


                        }



                    } else {
                        Toasty.warning(getActivity(), "Something was Wrong").show();
                    }
                } else {

                    Toasty.warning(getActivity(), "Please ! Loging First.").show();

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
}