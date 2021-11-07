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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.RequestModel;
import com.mristudio.blooddonation.utility.UtilsClass;
import com.mristudio.blooddonation.view.activity.DonnerProfileActivity;
import com.mristudio.blooddonation.view.activity.PostDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;

public class HomeDemoAdapter extends RecyclerView.Adapter<HomeDemoAdapter.OnViewHoalder> {


    private static final String TAG = "HomeDemoAdapter";
    private static Context mContext;

    List<RequestModel> postRequests = new ArrayList<>();


    public HomeDemoAdapter(Context mContext, List<RequestModel> postRequests) {
        this.mContext = mContext;

        this.postRequests = postRequests;
    }

    @NonNull
    @Override
    public OnViewHoalder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lyt_post_view_row, viewGroup, false);

        return new OnViewHoalder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnViewHoalder holder, final int position) {

        RequestModel requestModel = postRequests.get(position);
        holder.tVProfileName.setText(postRequests.get(position).getUserProfileName());
        holder.tvLokingFor.setText(" Loking for " + postRequests.get(position).getBloodGroup());
        // holder.tvAddressAndMinutes.settext("");
        holder.tVDetails.setText(postRequests.get(position).getRequestMessage());
        holder.tVBloodName.setText(postRequests.get(position).getBloodGroup());
        holder.tVReqAccepted.setText(requestModel.getTotalAccept()+" Accepted");
        holder.tVDonated.setText(requestModel.getTotalDonate()+" Donated");


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

        if (requestModel.getLovesList() != null) {
            //Cheak User Loging Status
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                if (requestModel.getLovesList().contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                    holder.iVLike.setImageResource(R.drawable.ic_favorite_cheak);


                } else {
                    holder.iVLike.setImageResource(R.drawable.ic_favorite_border_un);


                }
            } else {
                Toasty.error(mContext, "You are not Registred User !", Toasty.LENGTH_SHORT).show();
            }
        }
        //Log.e(TAG, "onBindViewHolder: " + postRequests.get(position).toString());
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
                    Intent intent = new Intent(mContext, PostDetailsActivity.class);
                    intent.putExtra("userPostId", postRequests.get(position).getTblId());
                    mContext.startActivity(intent);
                    ((Activity) mContext).overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
                    viewed(requestModel.getTblId());

                } else {
                    Toasty.error(mContext, "Your are not Registered User", Toasty.LENGTH_SHORT).show();
                }
            }
        });


        holder.llLovesClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.iVLike.getTag().toString().equals("unliked")) {
                    liked(requestModel.getTblId(), holder.iVLike);
                } else {
                    unliken(requestModel.getTblId(), holder.iVLike);
                }
            }
        });

        FirebaseDatabase.getInstance().getReference().child("USER_PUBLIC_POST").child(requestModel.getTblId()).child("LOVES")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            holder.tVLikeCounter.setText(" " + dataSnapshot.getChildrenCount() + " LOVE");
                            DataSnapshot dataSnapshot1 = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            if(dataSnapshot1.exists()){
                                holder.iVLike.setImageResource(R.drawable.ic_favorite_cheak);
                                holder.iVLike.setTag("liked");

                            }else{
                                holder.iVLike.setImageResource(R.drawable.ic_favorite_border_un);
                                holder.iVLike.setTag("unliked");
                                Log.e(TAG, "onDataChange: " + holder.iVLike.getTag().toString());
                            }


                        } else {

                            holder.tVLikeCounter.setText(" 0 LOVE");
                            holder.iVLike.setTag("unliked");
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

    }

    //Liked
    private void unliken(String tblId, ImageView likeBtn) {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postRef = rootRef.child("USER_PUBLIC_POST").child(tblId).child("LOVES");
        postRef.child(currentUser).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                likeBtn.setImageResource(R.drawable.ic_favorite_border_un);
                likeBtn.setTag("unliked");

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
                likeBtn.setImageResource(R.drawable.ic_favorite_cheak);
                likeBtn.setTag("liked");
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
        }

    }


}
