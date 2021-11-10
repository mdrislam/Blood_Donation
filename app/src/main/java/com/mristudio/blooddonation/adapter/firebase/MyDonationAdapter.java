package com.mristudio.blooddonation.adapter.firebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.Donation;
import com.mristudio.blooddonation.utility.Time_Utils;
import com.mristudio.blooddonation.view.activity.HistoryActivity;
import com.mristudio.blooddonation.view.activity.InsertDonateActivity;

import java.util.ArrayList;
import java.util.List;

public class MyDonationAdapter extends RecyclerView.Adapter<MyDonationAdapter.ViewHoalder> {
    private List<Donation> donationList = new ArrayList<>();
    private Context context;

    public MyDonationAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHoalder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_donation_row, parent, false);
        return new ViewHoalder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoalder holder, int position) {
        Donation donation = donationList.get(position);

        if (donation != null) {

            holder.tvHospitalName.setText(donation.getHospitalName());
            holder.tvMonthName.setText(donation.getMonthName());
            holder.tvDate.setText(donation.getDayOfMonth());
            holder.tvRemainingDate.setText(Time_Utils.getTimesAgo(donation.getDonateDate().trim()));

        }

        holder.ll_Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InsertDonateActivity.class);
                intent.putExtra("model", donation);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_up, R.anim.no_animation);

            }
        });


    }

    @Override
    public int getItemCount() {
        return donationList.size();
    }

    public void setDonationData(List<Donation> donationList) {
        this.donationList = donationList;
        notifyDataSetChanged();
    }

    public class ViewHoalder extends RecyclerView.ViewHolder {

        private TextView tvMonthName, tvDate, tvHospitalName, tvBloodQuantity, tvRemainingDate;
        private LinearLayout ll_Click;

        public ViewHoalder(@NonNull View itemView) {
            super(itemView);
            ll_Click = itemView.findViewById(R.id.ll_Click);
            tvMonthName = itemView.findViewById(R.id.tvMonthName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvHospitalName = itemView.findViewById(R.id.tvHospitalName);
            tvBloodQuantity = itemView.findViewById(R.id.tvBloodQuantity);
            tvRemainingDate = itemView.findViewById(R.id.tvRemainingDate);

        }
    }
}
