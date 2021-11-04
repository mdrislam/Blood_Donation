package com.mristudio.blooddonation.adapter.firebase;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.HomeDemoAdapter;
import com.mristudio.blooddonation.model.PostRequestModel;
import com.mristudio.blooddonation.model.RequestModel;
import com.mristudio.blooddonation.utility.UtilsClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindRequestAdapter extends FirebaseRecyclerAdapter<RequestModel, FindRequestAdapter.MyDonateRequestViewHolder> {

    private static final String TAG = "FindRequestAdapter";
    private HomePostClickLesenner homePostClickLesenner;
    private List<String> lovesList = new ArrayList<>();


    public FindRequestAdapter(@NonNull FirebaseRecyclerOptions<RequestModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyDonateRequestViewHolder holder, int position, @NonNull RequestModel model) {
        if (model != null) {

            lovesList = UtilsClass.getAllLikes(model.getTblId());

            holder.tVProfileName.setText(model.getUserProfileName());
            holder.tvLokingFor.setText(" Loking for " + model.getBloodGroup());
            // holder.tvAddressAndMinutes.settext("");
            holder.tVDetails.setText(model.getRequestMessage());
            holder.tVBloodName.setText(model.getBloodGroup());
            holder.tVReqAccepted.setText("");
            holder.tVDonated.setText("");
            holder.tVLikeCounter.setText(lovesList.get(0) + " Love");
            holder.tvViewsCounter.setText("");

            holder.ll_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            Picasso.get().load(model.getImagesUrl()).into(holder.ivPostImage);
            Picasso.get().load(model.getUserProfileImageUrl()).into(holder.ivProfileImage);

            if (lovesList.get(1) == "true") {
                Picasso.get().load(R.drawable.ic_favorite_cheak).into(holder.iVLike);

            } else {
                Picasso.get().load(R.drawable.ic_favorite_border_un).into(holder.iVLike);
            }

            holder.ivPostImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homePostClickLesenner.homePostClick(model.getTblId());
                }
            });

            holder.iVLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UtilsClass.getAllLikes(model.getTblId()).get(1) == "true") {
                        Picasso.get().load(R.drawable.ic_favorite_border_un).into(holder.iVLike);
                        holder.tVLikeCounter.setText(UtilsClass.updateUserLoves(model.getTblId(), null)+ " LOVE");
                    } else {
                        Picasso.get().load(R.drawable.ic_favorite_cheak).into(holder.iVLike);

                        holder.tVLikeCounter.setText(UtilsClass.updateUserLoves(model.getTblId(), model.getuId())+ " LOVE");
                    }
                }
            });

        }

    }

    @NonNull
    @Override
    public MyDonateRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_post_view_row, parent, false);
        return new MyDonateRequestViewHolder(view);
    }

    public void setOnCickLesenner(HomePostClickLesenner homePostClickLesenner) {
        this.homePostClickLesenner = homePostClickLesenner;
    }

    public class MyDonateRequestViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPostImage, ivProfileImage, iVLike;
        private TextView tVProfileName, tvLokingFor, tvAddressAndMinutes, tVDetails;
        private TextView tVBloodName, tVReqAccepted, tVDonated, tVLikeCounter, tvViewsCounter;
        private LinearLayout ll_share;

        public MyDonateRequestViewHolder(@NonNull View itemView) {
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
            ll_share = itemView.findViewById(R.id.ll_share);
        }
    }

    public interface HomePostClickLesenner {
        void homePostClick(String tblId);
    }
}
