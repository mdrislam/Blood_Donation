package com.mristudio.blooddonation.adapter.firebase;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.ImageSliderData;
import com.mristudio.blooddonation.model.UserInformation;

public class MyDonateRequestAdapter extends FirebaseRecyclerAdapter<ImageSliderData, MyDonateRequestAdapter.MyDonateRequestViewHolder> {

    private static final String TAG = "MyDonateRequestAdapter";

    public MyDonateRequestAdapter(@NonNull FirebaseRecyclerOptions<ImageSliderData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyDonateRequestViewHolder holder, int position, @NonNull ImageSliderData model) {

       holder.tvBloodGroup.setText("B+");
        Log.e(TAG, "onBindViewHolder: "+position+model.getImgUrl() );

    }

    @NonNull
    @Override
    public MyDonateRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_blood_request_row, parent, false);
        return new MyDonateRequestViewHolder(view);
    }

    public class MyDonateRequestViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lL_ContactBackgound;
        ImageButton Ib_Share;
        TextView tvDonationType, tvContactButton, tvSheduledTime, tvBloodGroup, tvRequesterName;
        TextView tvLocationDistance, tvPostRequestTime, tvLocation, tvRelationShip;

        public MyDonateRequestViewHolder(@NonNull View itemView) {
            super(itemView);


            lL_ContactBackgound = itemView.findViewById(R.id.lL_ContactBackgound);
            Ib_Share = itemView.findViewById(R.id.Ib_Share);
            tvDonationType = itemView.findViewById(R.id.tvDonationType);
            tvContactButton = itemView.findViewById(R.id.tvContactButton);
            tvSheduledTime = itemView.findViewById(R.id.tvSheduledTime);
            tvBloodGroup = itemView.findViewById(R.id.tvBloodGroup);
            tvRequesterName = itemView.findViewById(R.id.tvRequesterName);
            tvLocationDistance = itemView.findViewById(R.id.tvLocationDistance);
            tvPostRequestTime = itemView.findViewById(R.id.tvPostRequestTime);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvRelationShip = itemView.findViewById(R.id.tvRelationShip);
            Log.e(TAG, "onBindViewHolder: " );
        }
    }
}
