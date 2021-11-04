package com.mristudio.blooddonation.adapter;

import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.RequestModel;
import com.mristudio.blooddonation.model.UserInformation;
import com.mristudio.blooddonation.view.activity.DonnerProfileActivity;
import com.mristudio.blooddonation.view.activity.SendMessageActivity;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;

public class RecentRequestDemoAdapter extends RecyclerView.Adapter<RecentRequestDemoAdapter.OnViewHoalder> {


    private static final String TAG = "AllUserListAdapter";
    private static Context mContext;
    private RecentRequestClickLesenner recentRequestClickLesenner;
    private List<RequestModel> requestModelList = new ArrayList<>();

    public RecentRequestDemoAdapter(Context mContext) {
        this.mContext = mContext;
        this.recentRequestClickLesenner = (RecentRequestClickLesenner) mContext;
    }

    @NonNull
    @Override
    public OnViewHoalder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lyt_blood_request_row, viewGroup, false);

        return new OnViewHoalder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnViewHoalder hoalder, final int position) {
        RequestModel requestModel = requestModelList.get(position);


        if (requestModel.isUrgent()) {
            hoalder.tvDonationType.setText("Emargency");
        } else {
            hoalder.tvDonationType.setText("Scheduled");
        }
        hoalder.tvSheduledTime.setText("at " + requestModel.getDate() + " " + requestModel.getTime());
        hoalder.tvBloodGroup.setText(requestModel.getBloodGroup());

        if (requestModel.getHospitalName() != null)
            hoalder.tvHospitalName.setText(requestModel.getHospitalName());

        hoalder.tvPostRequestTime.setText(relaventTime(requestModel.getPostDateTime()));
        hoalder.tvLocation.setText(requestModel.getAddressofHospital());
        hoalder.tvRequesterName.setText(requestModel.getUserProfileName());


        hoalder.lL_Tittle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recentRequestClickLesenner.donnerPostClick(requestModel.getTblId());
            }
        });
        hoalder.lL_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestModel.getuId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    Toasty.warning(mContext, "Unable To Send Message To this User", Toasty.LENGTH_SHORT).show();
                } else {
                    recentRequestClickLesenner.chatProfile(requestModel.getuId());
                }
            }
        });
        hoalder.lL_ContactBackgound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Contacts", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestModelList.size();
    }

    public void setRecentRequestDemoAdapterData(List<RequestModel> requestModelList) {
        this.requestModelList = requestModelList;
    }

    public static class OnViewHoalder extends RecyclerView.ViewHolder {


        private TextView tvDonationType, tvContactButton, tvChat, tvSheduledTime, tvBloodGroup;
        private TextView tvHospitalName, tvLocationDistance, tvPostRequestTime, tvLocation, tvRequesterName;
        private LinearLayout lL_Tittle, lL_ContactBackgound, lL_Chat;


        public OnViewHoalder(@NonNull View itemView) {
            super(itemView);

            tvDonationType = itemView.findViewById(R.id.tvDonationType);
            lL_Tittle = itemView.findViewById(R.id.lL_Tittle);

            tvContactButton = itemView.findViewById(R.id.tvContactButton);
            tvChat = itemView.findViewById(R.id.tvChat);
            tvSheduledTime = itemView.findViewById(R.id.tvSheduledTime);
            tvBloodGroup = itemView.findViewById(R.id.tvBloodGroup);
            tvHospitalName = itemView.findViewById(R.id.tvHospitalName);
            tvLocationDistance = itemView.findViewById(R.id.tvLocationDistance);
            tvPostRequestTime = itemView.findViewById(R.id.tvPostRequestTime);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvRequesterName = itemView.findViewById(R.id.tvRequesterName);
            lL_ContactBackgound = itemView.findViewById(R.id.lL_ContactBackgound);
            lL_Chat = itemView.findViewById(R.id.lL_Chat);

        }

    }

    public interface RecentRequestClickLesenner {
        void donnerPostClick(String tblId);

        void chatProfile(String tblId);
    }

    public String relaventTime(String dateString) {
        //String dateString = "Aug 17, 2020 12:7 PM";
        String timeAgo = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy hh:mm a");

        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            long cuMillis = System.currentTimeMillis();

            timeAgo = (String) DateUtils.getRelativeTimeSpanString(convertedDate.getTime(), cuMillis, 1, FORMAT_ABBREV_RELATIVE);

            Log.e("LOG", "Time Ago==>" + timeAgo);

        } catch (ParseException e) {

            e.printStackTrace();
        }
        return timeAgo;
    }
}
