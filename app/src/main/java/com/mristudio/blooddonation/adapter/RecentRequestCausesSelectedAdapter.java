package com.mristudio.blooddonation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mristudio.blooddonation.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RecentRequestCausesSelectedAdapter extends RecyclerView.Adapter<RecentRequestCausesSelectedAdapter.CauseSelectedViewHoalder> {
    private List<String> caouseNames = new ArrayList<>();
    private Context context;
    private ClickButton bloodToogleButtonClick;
    private int selsctPosition = 0;

    public RecentRequestCausesSelectedAdapter(Context context, List<String> caouseNames, ClickButton bloodToogleButtonClick) {
        this.context = context;
        this.caouseNames = caouseNames;
        this.bloodToogleButtonClick = bloodToogleButtonClick;
    }

    @NonNull
    @Override
    public CauseSelectedViewHoalder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cause_view_row, parent, false);
        return new CauseSelectedViewHoalder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CauseSelectedViewHoalder holder, int position) {
        holder.tvCauseSelectItem.setText(caouseNames.get(position));
        holder.rlCauseSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selsctPosition = position;
                notifyDataSetChanged();
                bloodToogleButtonClick.seLectedBloodGroup(caouseNames.get(position));
            }
        });
//        if (position == 0) {
//            holder.tvCauseSelectItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_done, 0, 0, 0);
//            holder.tvCauseSelectItem.setTextColor(context.getResources().getColor(R.color.red_light500));
//
//            Log.e(TAG, "onBindViewHolder: "+"  selected" );
//        } else {
//            holder.tvCauseSelectItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            holder.tvCauseSelectItem.setTextColor(context.getResources().getColor(R.color.dark_gray));
//
//        }
        if (selsctPosition == position) {
            holder.tvCauseSelectItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_done, 0, 0, 0);
            holder.tvCauseSelectItem.setTextColor(context.getResources().getColor(R.color.red_light500));

        } else {

            holder.tvCauseSelectItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            holder.tvCauseSelectItem.setTextColor(context.getResources().getColor(R.color.dark_gray));

        }
    }

    @Override
    public int getItemCount() {
        return caouseNames.size();
    }

    public class CauseSelectedViewHoalder extends RecyclerView.ViewHolder {
        private RelativeLayout rlCauseSelect;
        private TextView tvCauseSelectItem;

        public CauseSelectedViewHoalder(@NonNull View itemView) {
            super(itemView);
            rlCauseSelect = itemView.findViewById(R.id.rlCauseSelect);
            tvCauseSelectItem = itemView.findViewById(R.id.tvCauseSelectItem);
        }
    }

    public interface ClickButton {
        void seLectedBloodGroup(String groupName);
    }
}
