package com.mristudio.blooddonation.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.AdminDataModel;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class AdminTransectionAdapter extends RecyclerView.Adapter<AdminTransectionAdapter.OnViewHoalder> {


    private static final String TAG ="AdminTransectionAdapter";
    private List<AdminDataModel> alladmins;
    private static Context mContext;
    private static AdminViewClick rootLayoutClick;
    static AlertDialog buttonAlertDialog;


    public AdminTransectionAdapter(List<AdminDataModel> alladmins, Context mContext) {

        this.alladmins = alladmins;
        this.mContext = mContext;
        rootLayoutClick = (AdminViewClick) mContext;
        buttonAlertDialog = null;
    }

    @NonNull
    @Override
    public OnViewHoalder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lyt_admin_profile_data_row, viewGroup, false);

        return new OnViewHoalder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnViewHoalder hoalder, final int position) {

        Log.e(TAG, "Size  = " + alladmins.get(position).getName());

        hoalder.profielNameTv.setText(alladmins.get(position).getName() + "(" + alladmins.get(position).getUserType() + ")");
        hoalder.bloodGorupTV.setText(alladmins.get(position).getEmail());

        hoalder.layoutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootLayoutClick.adminLayoutClick(alladmins.get(position));
            }
        });
        hoalder.moreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    openPopUpMenu(alladmins.get(position));

            }
        });
    }

    /**
     * Open Poup Menu To Change Admin Acess
     **/
    private void openPopUpMenu(AdminDataModel adminDataModel) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);

        final View mView = LayoutInflater.from(mContext).inflate(R.layout.update_user_type, null);
        Button updateBtn = (Button) mView.findViewById(R.id.updateButton);
        Button deleteBtn = (Button) mView.findViewById(R.id.deletebutton);
        ImageButton cancelBtn = (ImageButton) mView.findViewById(R.id.cancel_button);
        final RadioGroup radioGroup = (RadioGroup) mView.findViewById(R.id.radioGroup);
        final RadioButton adminButton = (RadioButton) mView.findViewById(R.id.adminRadioButton);
        final RadioButton postBatton = (RadioButton) mView.findViewById(R.id.postManagerRadioButton);
        if (adminDataModel.getAdmin()) {
            adminButton.setSelected(true);

        } else {
            postBatton.setSelected(true);
        }

        mBuilder.setView(mView);

        buttonAlertDialog = mBuilder.create();
        buttonAlertDialog.show();
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAlertDialog.dismiss();
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.adminRadioButton) {
                    rootLayoutClick.adminLayoutClickUpdateAccess(adminDataModel, "admin", true, buttonAlertDialog);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.postManagerRadioButton) {
                    rootLayoutClick.adminLayoutClickUpdateAccess(adminDataModel, "postManager", false, buttonAlertDialog);


                }

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonAlertDialog.dismiss();
                rootLayoutClick.adminLayoutClickDeleteUser(adminDataModel, buttonAlertDialog);

            }
        });
    }

    @Override
    public int getItemCount() {
        return alladmins.size();
    }

    public class OnViewHoalder extends RecyclerView.ViewHolder {

        private RelativeLayout rootProfileLayout;
        private LinearLayout layoutClick;
        private TextView profielNameTv, bloodGorupTV;
        private ImageButton moreOption;


        public OnViewHoalder(@NonNull View itemView) {
            super(itemView);

            profielNameTv = itemView.findViewById(R.id.profielNameTv);
            bloodGorupTV = itemView.findViewById(R.id.bloodGorupTV);
            moreOption = (ImageButton) itemView.findViewById(R.id.moreOptionID);
            layoutClick = (LinearLayout) itemView.findViewById(R.id.layoutClick);
        }

    }

    public interface AdminViewClick {
        void adminLayoutClick(AdminDataModel adminDataModel);

        void adminLayoutClickUpdateAccess(AdminDataModel adminDataModel, String userType, Boolean isAdmin, AlertDialog buttonAlertDialog);

        void adminLayoutClickDeleteUser(AdminDataModel adminDataModel, AlertDialog buttonAlertDialog);
    }
}
