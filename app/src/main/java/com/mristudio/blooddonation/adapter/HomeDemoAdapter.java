package com.mristudio.blooddonation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.RequestModel;
import com.mristudio.blooddonation.utility.UtilsClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class HomeDemoAdapter extends RecyclerView.Adapter<HomeDemoAdapter.OnViewHoalder> {


    private static final String TAG = "AllUserListAdapter";
    private static Context mContext;
    private HomePostClickLesenner homePostClickLesenner;
    List<RequestModel> postRequests = new ArrayList<>();


    public HomeDemoAdapter(Context mContext, List<RequestModel> postRequests, HomePostClickLesenner homePostClickLesenner) {
        this.mContext = mContext;
        this.homePostClickLesenner = homePostClickLesenner;
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
        holder.tVReqAccepted.setText("0 Accepted");
        holder.tVDonated.setText("0 Donated");

        Log.e(TAG, "onBindViewHolder: " + requestModel);

        /**
         * Cheak Total Loves List is Empty or not
         * */
        if (requestModel.getLovesList() != null) {
            holder.tVLikeCounter.setText(" " + requestModel.getLovesList().size() + " LOVE");
        } else {
            holder.tVLikeCounter.setText(" 0 LOVE");
        }
        /**
         * Cheak Total Views List is Empty or not
         * */
        if (requestModel.getViewsList() != null) {
            holder.tvViewsCounter.setText(" " + requestModel.getViewsList().size() + " VIEWS");
        } else {
            holder.tvViewsCounter.setText(" 0 VIEWS");
        }

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
        Log.e(TAG, "onBindViewHolder: " + postRequests.get(position).toString());

        holder.ivPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    homePostClickLesenner.homePostClick(postRequests.get(position).getTblId());
                    holder.tvViewsCounter.setText(UtilsClass.updateTotalViews(postRequests.get(position).getTblId(), FirebaseAuth.getInstance().getCurrentUser().getUid()) + " VIEWS");
                    notifyDataSetChanged();
                } else {
                    Toasty.error(mContext, "Your are not Registered User", Toasty.LENGTH_SHORT).show();
                }
            }
        });

        holder.llLovesClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                    if (requestModel.getLovesList() != null) {

                        if (requestModel.getLovesList().contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            holder.iVLike.setImageResource(R.drawable.ic_favorite_border_un);
                            UtilsClass.removesUserLoves(postRequests.get(position).getTblId(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                            notifyDataSetChanged();
                        } else {
                            holder.iVLike.setImageResource(R.drawable.ic_favorite_cheak);
                            UtilsClass.updateUserLoves(postRequests.get(position).getTblId(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                            notifyDataSetChanged();
                        }

                    } else {
                        holder.iVLike.setImageResource(R.drawable.ic_favorite_cheak);
                        UtilsClass.updateUserLoves(postRequests.get(position).getTblId(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                        notifyDataSetChanged();
                    }
                } else {
                    Toasty.error(mContext, "Your are not Registered User", Toasty.LENGTH_SHORT).show();
                }

            }

        });

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

    public interface HomePostClickLesenner {
        void homePostClick(String tblName);
    }

}
