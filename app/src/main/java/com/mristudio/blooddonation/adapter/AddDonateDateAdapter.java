package com.mristudio.blooddonation.adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.UserInformation;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class AddDonateDateAdapter extends RecyclerView.Adapter<AddDonateDateAdapter.OnViewHoalder> {


    private static final String TAG = "AdminTransectionAdapter";
    private List<UserInformation> userInformationList;
    private static Context mContext;
    private static AdminViewClick rootLayoutClick;
    static AlertDialog buttonAlertDialog;


    public AddDonateDateAdapter(List<UserInformation> userInformationList, Context mContext) {

        this.userInformationList = userInformationList;
        this.mContext = mContext;
        rootLayoutClick = (AdminViewClick) mContext;
        buttonAlertDialog = null;
    }

    @NonNull
    @Override
    public OnViewHoalder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lyt_donate_date_data_row, viewGroup, false);

        return new OnViewHoalder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnViewHoalder hoalder, final int position) {

        Log.e(TAG, "Size  = " + userInformationList.get(position).getName());

        hoalder.profielNameTv.setText(userInformationList.get(position).getName());
        hoalder.bloodGorupTV.setText(userInformationList.get(position).getBloodGroup());
        hoalder.lastBloodDonateDateTV.setText(userInformationList.get(position).getLast_donateDate());
        Picasso.get().load(userInformationList.get(position).getUserProfilePicture()).into(hoalder.profiel_Picture);


        hoalder.moreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openPopUpMenu(userInformationList.get(position));

            }
        });
    }

    /**
     * Open Poup Menu To Change Admin Acess
     **/
    private void openPopUpMenu(UserInformation userInformationList) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);

        final View mView = LayoutInflater.from(mContext).inflate(R.layout.update_user_blood_date, null);
        Button updateBtn = (Button) mView.findViewById(R.id.updateButton);
        Button cancelButton = (Button) mView.findViewById(R.id.cancelButton);
        TextView donateDateTV = (TextView) mView.findViewById(R.id.donateDateTV);
        ImageButton selectDateIB = (ImageButton) mView.findViewById(R.id.selectDateIB);

        mBuilder.setView(mView);

        buttonAlertDialog = mBuilder.create();
        buttonAlertDialog.show();
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAlertDialog.dismiss();
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!donateDateTV.getText().toString().isEmpty()) {
                    userInformationList.setLast_donateDate(donateDateTV.getText().toString());
                    rootLayoutClick.updateUserDonateBloodDate(userInformationList, buttonAlertDialog);
                }else {
                    Toasty.error(mContext, "Please Select Donate Date !", Toast.LENGTH_SHORT).show();

                }
            }
        });
        selectDateIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(donateDateTV);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userInformationList.size();
    }

    public class OnViewHoalder extends RecyclerView.ViewHolder {

        private RelativeLayout rootProfileLayout;
        private LinearLayout layoutClick;
        private TextView profielNameTv, bloodGorupTV, lastBloodDonateDateTV;
        private ImageButton moreOption;
        private CircleImageView profiel_Picture;


        public OnViewHoalder(@NonNull View itemView) {
            super(itemView);

            profielNameTv = itemView.findViewById(R.id.profielNameTv);
            bloodGorupTV = itemView.findViewById(R.id.bloodGorupTV);
            moreOption = (ImageButton) itemView.findViewById(R.id.moreOptionID);
            layoutClick = (LinearLayout) itemView.findViewById(R.id.layoutClick);
            profiel_Picture = itemView.findViewById(R.id.profile_image);
            lastBloodDonateDateTV = itemView.findViewById(R.id.lastBloodDonateDateTV);
        }

    }

    /**
     * Open Date Picker When User Select Button and set text in TextView
     *
     * @param donateDateTV
     */
    public void openDatePickerDialog(TextView donateDateTV) {
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM, yyyy", Locale.US);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                System.out.print(dateFormatter.format(newDate.getTime()));
                donateDateTV.setText(dateFormatter.format(newDate.getTime()));
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public interface AdminViewClick {

        void updateUserDonateBloodDate(UserInformation userInformation, AlertDialog buttonAlertDialog);

    }
}
