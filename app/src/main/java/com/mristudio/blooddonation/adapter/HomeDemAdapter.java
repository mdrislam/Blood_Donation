package com.mristudio.blooddonation.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.RequestModel;
import com.mristudio.blooddonation.utility.Time_Utils;
import com.mristudio.blooddonation.view.activity.DonnerProfileActivity;
import com.mristudio.blooddonation.view.activity.PostDetailsActivity;
import com.mristudio.blooddonation.view.activity.PostDetails_Activity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class HomeDemAdapter extends RecyclerView.Adapter<HomeDemAdapter.OnViewHoalder> {


    private static final String TAG = "HomeAdapter";
    private static Context mContext;

    List<RequestModel> postRequests = new ArrayList<>();


    public HomeDemAdapter(Context mContext, List<RequestModel> postRequests) {
        this.mContext = mContext;

        this.postRequests = postRequests;
    }

    @NonNull
    @Override
    public OnViewHoalder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lyt_ppoost_view_row, viewGroup, false);

        return new OnViewHoalder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnViewHoalder holder, final int position) {

        RequestModel requestModel = postRequests.get(position);

        if (requestModel.getType().equals("request")) {
            holder.llPostBottom.setVisibility(View.GONE);
            holder.tvLokingFor.setVisibility(View.VISIBLE);
            holder.llRequestBottom.setVisibility(View.VISIBLE);
            holder.transfarentLyt.setVisibility(View.VISIBLE);
            holder.tvAddressAndMinutes.setText("");
            holder.tvAddressAndMinutes.setText(Time_Utils.getTimesAgo(requestModel.getPostDateTime()) + " - " + requestModel.getAddressofHospital());
            holder.tVBloodName.setText(postRequests.get(position).getBloodGroup());
            holder.tVReqAccepted.setText(requestModel.getTotalAccept() + " Accepted");
            holder.tVDonated.setText(requestModel.getTotalDonate() + " Donated");
            holder.tvLokingFor.setText(" Loking for " + postRequests.get(position).getBloodGroup());

        } else {
            holder.tvLokingFor.setVisibility(View.GONE);
            holder.llRequestBottom.setVisibility(View.GONE);
            holder.transfarentLyt.setVisibility(View.GONE);
            holder.llPostBottom.setVisibility(View.VISIBLE);
            holder.tvAddressAndMinutes.setText("");
            holder.tvAddressAndMinutes.setText(Time_Utils.getTimesAgo(requestModel.getPostDateTime()));
        }

        holder.tVProfileName.setText(postRequests.get(position).getUserProfileName());
        holder.tVDetails.setText(postRequests.get(position).getRequestMessage());



        holder.ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        Picasso.get().load(postRequests.get(position).getImagesUrl()).into(holder.ivPostImage);
        if (requestModel.getUserProfileImageUrl().equalsIgnoreCase("null")) {
            holder.ivProfileImage.setImageResource(R.drawable.female_icon);
        } else if (requestModel.getUserProfileImageUrl() == null) {
            holder.ivProfileImage.setImageResource(R.drawable.female_icon);
        } else {
            Picasso.get().load(postRequests.get(position).getUserProfileImageUrl()).into(holder.ivProfileImage);
        }


        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DonnerProfileActivity.class);
                intent.putExtra("uId", postRequests.get(position).getuId());
                mContext.startActivity(intent);

                ((Activity) mContext).overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
            }
        });

        holder.ivPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                    if (requestModel.getType().equals("request")) {
                        Intent intent = new Intent(mContext, PostDetailsActivity.class);
                        intent.putExtra("userPostId", postRequests.get(position).getTblId());
                        mContext.startActivity(intent);
                        ((Activity) mContext).overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
                        viewed(requestModel.getTblId());

                    } else {

                        Intent intent = new Intent(mContext, PostDetails_Activity.class);
                        intent.putExtra("data", postRequests.get(position));
                        mContext.startActivity(intent);
                        ((Activity) mContext).overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
                        //Toast.makeText(mContext, "Post", Toast.LENGTH_SHORT).show();

                    }


                } else {
                    Toasty.error(mContext, "Your are not Registered User", Toasty.LENGTH_SHORT).show();
                }
            }
        });

        holder.llLovesClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.iVLike.getTag().toString().equals("unliked")) {
                    liked(position, holder.iVLike);
                } else {
                    unliken(position, holder.iVLike);
                }
            }
        });
        holder.llPostLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.postlikeIv.getTag().toString().equals("unliked")) {
                    liked(position, holder.postlikeIv);
                } else {
                    unliken(position, holder.postlikeIv);
                }
            }
        });


        FirebaseDatabase.getInstance().getReference().child("USER_PUBLIC_POST").child(requestModel.getTblId()).child("LOVES")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            if (requestModel.getType().equals("request")) {

                                holder.tVLikeCounter.setText(" " + dataSnapshot.getChildrenCount() + " LOVE");

                            } else {

                                holder.postlikeCounterTV.setText(" " + dataSnapshot.getChildrenCount());

                            }

                            DataSnapshot dataSnapshot1 = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            if (dataSnapshot1.exists()) {

                                if (requestModel.getType().equals("request")) {
                                    holder.iVLike.setImageResource(R.drawable.heart_fill);
                                    holder.iVLike.setTag("liked");
                                    holder.iVLike.setColorFilter(ContextCompat.getColor(mContext, R.color.red_light500), android.graphics.PorterDuff.Mode.MULTIPLY);

                                } else {
                                    holder.postlikeIv.setImageResource(R.drawable.heart_fill);
                                    holder.postlikeIv.setTag("liked");
                                    holder.postlikeIv.setColorFilter(ContextCompat.getColor(mContext, R.color.red_light500), android.graphics.PorterDuff.Mode.MULTIPLY);
                                }

                            } else {

                                if (requestModel.getType().equals("request")) {

                                    holder.iVLike.setImageResource(R.drawable.heart);
                                    holder.iVLike.setTag("unliked");
                                    holder.iVLike.setColorFilter(ContextCompat.getColor(mContext, R.color.gray_blue), android.graphics.PorterDuff.Mode.MULTIPLY);


                                } else {

                                    holder.postlikeIv.setImageResource(R.drawable.heart);
                                    holder.postlikeIv.setTag("unliked");
                                    holder.postlikeIv.setColorFilter(ContextCompat.getColor(mContext, R.color.gray_blue), android.graphics.PorterDuff.Mode.MULTIPLY);

                                }

                            }


                        } else {

                            if (requestModel.getType().equals("request")) {
                                holder.tVLikeCounter.setText(" 0 LOVE");
                                holder.iVLike.setTag("unliked");
                            } else {
                                holder.postlikeCounterTV.setText(" 0 ");
                                holder.postlikeIv.setTag("unliked");
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("USER_PUBLIC_POST").child(requestModel.getTblId()).child("VIEWS")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            holder.tvViewsCounter.setText(" " + dataSnapshot.getChildrenCount() + " VIEWS");

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        FirebaseDatabase.getInstance().getReference().child("USER_PUBLIC_POST").child(requestModel.getTblId()).child("COMMENTS")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            holder.commentCounterTV.setText(" " + dataSnapshot.getChildrenCount());
                        } else {
                            holder.commentCounterTV.setText(" 10 ");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


    }

    //Liked
    private void unliken(int position, ImageView likeBtn) {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postRef = rootRef.child("USER_PUBLIC_POST").child(postRequests.get(position).getTblId()).child("LOVES");
        postRef.child(currentUser).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


                if (postRequests.get(position).getType().equals("request")) {

                    likeBtn.setImageResource(R.drawable.heart);
                    likeBtn.setTag("unliked");
                    likeBtn.setColorFilter(ContextCompat.getColor(mContext, R.color.gray_blue), android.graphics.PorterDuff.Mode.MULTIPLY);

                } else {
                    likeBtn.setImageResource(R.drawable.heart);
                    likeBtn.setTag("unliked");
                    likeBtn.setColorFilter(ContextCompat.getColor(mContext, R.color.gray_blue), android.graphics.PorterDuff.Mode.MULTIPLY);

                }

            }
        });


    }

    private void liked(int position, ImageView likeBtn) {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postRef = rootRef.child("USER_PUBLIC_POST").child(postRequests.get(position).getTblId()).child("LOVES");
        HashMap<String, String> map = new HashMap<>();
        map.put(currentUser, currentUser);

        postRef.child(currentUser).setValue(currentUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                if (postRequests.get(position).getType().equals("request")) {

                    likeBtn.setImageResource(R.drawable.heart_fill);
                    likeBtn.setTag("liked");
                    likeBtn.setColorFilter(ContextCompat.getColor(mContext, R.color.red_light500), android.graphics.PorterDuff.Mode.MULTIPLY);

                } else {
                    likeBtn.setImageResource(R.drawable.heart_fill);
                    likeBtn.setTag("liked");
                    likeBtn.setColorFilter(ContextCompat.getColor(mContext, R.color.red_light500), android.graphics.PorterDuff.Mode.MULTIPLY);

                }

            }
        });
    }

    public void viewed(String tblId) {

        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postRef = rootRef.child("USER_PUBLIC_POST").child(tblId).child("VIEWS");
        HashMap<String, String> map = new HashMap<>();
        map.put(currentUser, currentUser);
        postRef.child(currentUser).setValue(currentUser);

    }

    @Override
    public int getItemCount() {
        return postRequests.size();
    }

    public class OnViewHoalder extends RecyclerView.ViewHolder {

        private ImageView ivPostImage, ivProfileImage;
        private ImageView iVLike;
        private TextView tVProfileName, tvLokingFor, tvAddressAndMinutes, tVDetails;
        private TextView tVBloodName, tVReqAccepted, tVDonated, tVLikeCounter, tvViewsCounter;
        private LinearLayout ll_share, llLovesClick;

        private LinearLayout llPostBottom, llRequestBottom, llComment, llPostLike, llPostShare, transfarentLyt;
        private TextView commentCounterTV, postlikeCounterTV;
        private ImageView postlikeIv;


        public OnViewHoalder(@NonNull View itemView) {
            super(itemView);

            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            iVLike = itemView.findViewById(R.id.iVLike);
            tVProfileName = itemView.findViewById(R.id.tVProfileName);
            tvLokingFor = itemView.findViewById(R.id.tvLokingFor);
            tvAddressAndMinutes = itemView.findViewById(R.id.tvAddressAndMinutes);
            tVDetails = itemView.findViewById(R.id.tVDetails);
            tVBloodName = itemView.findViewById(R.id.tVBloodName);
            tVReqAccepted = itemView.findViewById(R.id.tVReqAccepted);
            tVDonated = itemView.findViewById(R.id.tVDonated);
            tVLikeCounter = itemView.findViewById(R.id.tVLikeCounter);
            tvViewsCounter = itemView.findViewById(R.id.tvViewsCounter);
            llLovesClick = itemView.findViewById(R.id.llLovesClick);
            ll_share = itemView.findViewById(R.id.ll_share);
            //
            llPostBottom = itemView.findViewById(R.id.llPostBottom);
            llRequestBottom = (LinearLayout) itemView.findViewById(R.id.llRequestBottom);
            transfarentLyt = (LinearLayout) itemView.findViewById(R.id.transfarentLyt);
            llComment = itemView.findViewById(R.id.llComment);
            llPostLike = itemView.findViewById(R.id.llPostLike);
            llPostShare = itemView.findViewById(R.id.llPostShare);
            commentCounterTV = itemView.findViewById(R.id.commentCounterTV);
            postlikeCounterTV = itemView.findViewById(R.id.postlikeCounterTV);
            postlikeIv = itemView.findViewById(R.id.postlikeIv);


        }

    }


}
