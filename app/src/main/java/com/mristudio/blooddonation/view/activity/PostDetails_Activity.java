package com.mristudio.blooddonation.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.CommentAdapter;
import com.mristudio.blooddonation.model.CommentModel;
import com.mristudio.blooddonation.model.RequestModel;
import com.mristudio.blooddonation.utility.Time_Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class PostDetails_Activity extends AppCompatActivity {
    private static final String TAG = "PostDetails_Activity";
    private CircleImageView ivProfileImage;
    private RecyclerView commentRecyclerView;
    private TextView tVProfileName, tvAddressAndMinutes, shareCounterTV, tVDetails, commentCounterTV, postlikeCounterTV;
    private LinearLayout llPostLike, llPostShare;
    private ImageView postlikeIv, ivPostImage;
    private EditText messageEt;
    private ImageButton ibSend;
    private RequestModel requestModel;
    private Toolbar toolbar;
    private List<CommentModel> commentModelList = new ArrayList<>();
    private CommentAdapter adapter;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details_);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getIntent().getExtras() != null) {
            requestModel = (RequestModel) getIntent().getSerializableExtra("data");
            Log.e(TAG, "onCreate: " + requestModel.toString());
        }
        initView();
        loadCommits(requestModel.getTblId());
    }

    private void loadCommits(String tblId) {
        FirebaseDatabase.getInstance().getReference().child("USER_PUBLIC_POST").child(tblId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            DataSnapshot comments = snapshot.child("COMMENTS");
                            DataSnapshot loves = snapshot.child("LOVES");
                            DataSnapshot shares = snapshot.child("SHARES");

                            if (comments.exists()) {

                                commentModelList.clear();
                                commentCounterTV.setText(" " + comments.getChildrenCount());

                                for (DataSnapshot commentSnapshot : comments.getChildren()) {
                                    CommentModel commentModel = commentSnapshot.getValue(CommentModel.class);
                                    commentModelList.add(commentModel);
                                }
                                adapter.setComment(commentModelList);

                            } else {

                                commentCounterTV.setText(" 0");
                            }


                            if (loves.exists()) {

                                postlikeCounterTV.setText(" " + loves.getChildrenCount());
                                DataSnapshot loveSnap = loves.child(currentUser.getUid());

                                if (loveSnap.exists()) {

                                    postlikeIv.setImageResource(R.drawable.heart_fill);
                                    postlikeIv.setTag("liked");
                                    postlikeIv.setColorFilter(ContextCompat.getColor(PostDetails_Activity.this, R.color.red_light500), android.graphics.PorterDuff.Mode.MULTIPLY);


                                } else {

                                    postlikeIv.setImageResource(R.drawable.heart);
                                    postlikeIv.setTag("unliked");
                                    postlikeIv.setColorFilter(ContextCompat.getColor(PostDetails_Activity.this, R.color.gray_blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                                }


                            } else {
                                postlikeCounterTV.setText(" 0");
                            }

                            if (shares.exists()) {

                                shareCounterTV.setText(" " + shares.getChildrenCount());

                            } else {

                                shareCounterTV.setText(" 0");
                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: " + error.getMessage());
                    }
                });
    }

    private void initView() {

        ivProfileImage = findViewById(R.id.ivProfileImage);
        ivPostImage = findViewById(R.id.ivPostImage);
        tVProfileName = findViewById(R.id.tVProfileName);
        tvAddressAndMinutes = findViewById(R.id.tvAddressAndMinutes);
        tVDetails = findViewById(R.id.tVDetails);
        commentCounterTV = findViewById(R.id.commentCounterTV);
        postlikeCounterTV = findViewById(R.id.postlikeCounterTV);
        llPostLike = findViewById(R.id.llPostLike);
        llPostShare = findViewById(R.id.llPostShare);
        shareCounterTV = findViewById(R.id.shareCounterTV);
        postlikeIv = findViewById(R.id.postlikeIv);
        messageEt = findViewById(R.id.messageEt);
        ibSend = findViewById(R.id.ibSend);
        commentRecyclerView = findViewById(R.id.commentRecyclerView);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        adapter = new CommentAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(linearLayoutManager);
        commentRecyclerView.setHasFixedSize(true);
        commentRecyclerView.setAdapter(adapter);

        llPostLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postlikeIv.getTag().toString().equals("unliked")) {

                    liked(requestModel.getTblId(), postlikeIv);

                } else {
                    unliked(requestModel.getTblId(), postlikeIv);
                }
            }
        });

        if (requestModel != null) {
            tVProfileName.setText(requestModel.getUserProfileName());
            Picasso.get().load(requestModel.getUserProfileImageUrl()).into(ivProfileImage);
            Picasso.get().load(requestModel.getImagesUrl()).into(ivPostImage);
            tVDetails.setText(requestModel.getRequestMessage());
            tvAddressAndMinutes.setText(Time_Utils.getTimesAgo(requestModel.getPostDateTime()));
        }

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetails_Activity.this, DonnerProfileActivity.class);
                intent.putExtra("uId", requestModel.getuId());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
            }
        });
        ivPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetails_Activity.this, ImageActivity.class);
                intent.putExtra("imageurl", requestModel.getImagesUrl());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
            }
        });

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageEt.getText().toString() != null) {

                    insertNewComment(messageEt.getText().toString().trim());


                } else {
                    Toasty.error(PostDetails_Activity.this, "Empty Comment not allowed !", Toasty.LENGTH_SHORT).show();
                }
            }
        });

    }

    //Inser New Message on THis Post
    private void insertNewComment(String msg) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("USER_PUBLIC_POST").child(requestModel.getTblId()).child("COMMENTS");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String tblId = reference.push().getKey();

        CommentModel commentModel = new CommentModel(tblId, currentUser.getUid(), msg);

        reference.child(tblId).setValue(commentModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toasty.success(PostDetails_Activity.this, "New Comment", Toasty.LENGTH_SHORT).show();
                    messageEt.setText(" ");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(PostDetails_Activity.this, "" + e.getMessage(), Toasty.LENGTH_SHORT).show();

                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });
    }

    //Liked
    private void unliked(String tblId, ImageView likeBtn) {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postRef = rootRef.child("USER_PUBLIC_POST").child(tblId).child("LOVES");
        postRef.child(currentUser).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                likeBtn.setImageResource(R.drawable.heart);
                likeBtn.setTag("unliked");
                likeBtn.setColorFilter(ContextCompat.getColor(PostDetails_Activity.this, R.color.gray_blue), android.graphics.PorterDuff.Mode.MULTIPLY);

            }
        });


    }

    private void liked(String tblId, ImageView likeBtn) {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postRef = rootRef.child("USER_PUBLIC_POST").child(tblId).child("LOVES");
        HashMap<String, String> map = new HashMap<>();
        map.put(currentUser, currentUser);

        postRef.child(currentUser).setValue(currentUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                likeBtn.setImageResource(R.drawable.heart_fill);
                likeBtn.setTag("liked");
                likeBtn.setColorFilter(ContextCompat.getColor(PostDetails_Activity.this, R.color.red_light500), android.graphics.PorterDuff.Mode.MULTIPLY);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getSupportActionBar().setTitle("Details ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public void onPause() {
        super.onPause();

        getSupportActionBar().setTitle("Details ");
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