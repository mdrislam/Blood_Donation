package com.mristudio.blooddonation.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mristudio.blooddonation.R;

import java.util.ArrayList;
import java.util.List;

public class BloodGroupSelectedAdapter extends RecyclerView.Adapter<BloodGroupSelectedAdapter.BloodGroupSelectedViewHoalder> {
    private List<String> bloodNames = new ArrayList<>();
    private Context context;
    private BloodToogleButtonClick bloodToogleButtonClick;
    private int selsctPosition = -1;

    public BloodGroupSelectedAdapter(Context context, List<String> bloodNames) {
        this.context = context;
        this.bloodNames = bloodNames;
        bloodToogleButtonClick= (BloodToogleButtonClick) context;
    }

    @NonNull
    @Override
    public BloodGroupSelectedViewHoalder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_group_row, parent, false);
        return new BloodGroupSelectedViewHoalder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodGroupSelectedViewHoalder holder, int position) {
        holder.bloodNameTV.setText(bloodNames.get(position));
        holder.bloodNameRootLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selsctPosition = position;
                notifyDataSetChanged();
                bloodToogleButtonClick.seLectedBloodGroup(bloodNames.get(position));
            }
        });
        if (selsctPosition == position) {
            holder.bloodNameRootLyt.setBackground(context.getResources().getDrawable(R.drawable.button_circle__fill_shape));
            holder.bloodNameTV.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.bloodNameRootLyt.setBackground(context.getResources().getDrawable(R.drawable.button_circle_shape));
            holder.bloodNameTV.setTextColor(context.getResources().getColor(R.color.red_light));
        }
    }

    @Override
    public int getItemCount() {
        return bloodNames.size();
    }

    public class BloodGroupSelectedViewHoalder extends RecyclerView.ViewHolder {
        private RelativeLayout bloodNameRootLyt;
        private TextView bloodNameTV;

        public BloodGroupSelectedViewHoalder(@NonNull View itemView) {
            super(itemView);
            bloodNameRootLyt = itemView.findViewById(R.id.bloodNameRootLyt);
            bloodNameTV = itemView.findViewById(R.id.bloodNameTV);
        }
    }
    public interface BloodToogleButtonClick{
         void seLectedBloodGroup(String groupName);
    }
}
