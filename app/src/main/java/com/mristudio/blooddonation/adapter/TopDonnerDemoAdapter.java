package com.mristudio.blooddonation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.UserInformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TopDonnerDemoAdapter extends RecyclerView.Adapter<TopDonnerDemoAdapter.OnViewHoalder> {


    private static final String TAG = "AllUserListAdapter";
    private static Context mContext;
    private List<UserInformation> userInformationList = new ArrayList<>();
    private TopDonnerClickLesenner topDonnerClickLesenner;

    public TopDonnerDemoAdapter(Context mContext, List<UserInformation> userInformationList, TopDonnerClickLesenner topDonnerClickLesenner) {
        this.mContext = mContext;
        this.topDonnerClickLesenner = topDonnerClickLesenner;
        this.userInformationList = userInformationList;
    }

    @NonNull
    @Override
    public OnViewHoalder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lyt_top_donner_data_row, viewGroup, false);

        return new OnViewHoalder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnViewHoalder hoalder, final int position) {

        UserInformation userInformation = userInformationList.get(position);
        if (userInformation != null) {

            Picasso.get().load(userInformation.getUserProfilePicture()).into(hoalder.profile_image);
            hoalder.tvUserProfileName.setText(userInformation.getName());
            hoalder.tvUserAddress.setText(userInformation.getPolice_station() + " " + userInformation.getDistrict());
            if (userInformation.getRatingModelList() != null) {
                hoalder.tvUserTotalBloodunits.setText(userInformation.getRatingModelList().size()+" Units");
            } else {
                hoalder.tvUserTotalBloodunits.setText("0 Units");
            }


            hoalder.layoutClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    topDonnerClickLesenner.donnerPostClick(userInformation.getUserId());
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return userInformationList.size();
    }

    public class OnViewHoalder extends RecyclerView.ViewHolder {

        private ImageView profile_image;
        private TextView tvUserProfileName, tvUserAddress, profielNameTv, tvUserTotalBloodunits;
        private LinearLayout layoutClick;


        public OnViewHoalder(@NonNull View itemView) {
            super(itemView);

            layoutClick = itemView.findViewById(R.id.layoutClick);
            profile_image = itemView.findViewById(R.id.profile_image);
            tvUserProfileName = itemView.findViewById(R.id.tvUserProfileName);
            tvUserAddress = itemView.findViewById(R.id.tvUserAddress);
            profielNameTv = itemView.findViewById(R.id.profielNameTv);
            tvUserTotalBloodunits = itemView.findViewById(R.id.tvUserTotalBloodunits);
        }

    }

    public interface TopDonnerClickLesenner {
        void donnerPostClick(String userId);
    }

}
