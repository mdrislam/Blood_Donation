package com.mristudio.blooddonation.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.UserInformation;

import java.util.ArrayList;
import java.util.List;

public class AllUserListAdapter extends RecyclerView.Adapter<AllUserListAdapter.OnViewHoalder> {


    private static final String TAG ="AllUserListAdapter";
    private List<UserInformation> alladmins=new ArrayList<>();
    private static Context mContext;
    private static AdminViewClick rootLayoutClick;
    static AlertDialog buttonAlertDialog;


    public AllUserListAdapter(List<UserInformation> alladmins, Context mContext) {

        this.alladmins = alladmins;
        this.mContext = mContext;
        rootLayoutClick = (AdminViewClick) mContext;
        buttonAlertDialog = null;
    }

    @NonNull
    @Override
    public OnViewHoalder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lyt_profile_data_row, viewGroup, false);

        return new OnViewHoalder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnViewHoalder hoalder, final int position) {


        hoalder.profielNameTv.setText(alladmins.get(position).getName() + "(" + alladmins.get(position).getUserType() + ")");
        hoalder.bloodGorupTV.setText(alladmins.get(position).getBloodGroup());

        hoalder.layoutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootLayoutClick.adminLayoutClickAccess(alladmins.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return alladmins.size();
    }

    public class OnViewHoalder extends RecyclerView.ViewHolder {

        private LinearLayout layoutClick;
        private TextView profielNameTv, bloodGorupTV;



        public OnViewHoalder(@NonNull View itemView) {
            super(itemView);

            profielNameTv = itemView.findViewById(R.id.profielNameTv);
            bloodGorupTV = itemView.findViewById(R.id.bloodGorupTV);

            layoutClick = (LinearLayout) itemView.findViewById(R.id.layoutClick);
        }

    }

    public interface AdminViewClick {


        void adminLayoutClickAccess(UserInformation adminDataModel);

    }
}
