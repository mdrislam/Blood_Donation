package com.mristudio.blooddonation.adapter.firebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.Donation;

public class MyDonationAdapter extends FirebaseRecyclerAdapter<Donation, MyDonationAdapter.MyDonationViewHoalder> {

    public MyDonationAdapter(@NonNull FirebaseRecyclerOptions<Donation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyDonationViewHoalder holder, int position, @NonNull Donation model) {

    }

    @NonNull
    @Override
    public MyDonationViewHoalder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_donation_row, parent, false);
        return new MyDonationViewHoalder(view);
    }

    public class MyDonationViewHoalder extends RecyclerView.ViewHolder {

        private TextView tvMonthName,tvDate,tvHospitalName,tvBloodQuantity,tvRemainingDate;
        public MyDonationViewHoalder(@NonNull View itemView) {
            super(itemView);
            tvMonthName =itemView.findViewById(R.id.tvMonthName);
            tvDate =itemView.findViewById(R.id.tvDate);
            tvHospitalName =itemView.findViewById(R.id.tvHospitalName);
            tvBloodQuantity =itemView.findViewById(R.id.tvBloodQuantity);
            tvRemainingDate =itemView.findViewById(R.id.tvRemainingDate);

        }
    }
}
