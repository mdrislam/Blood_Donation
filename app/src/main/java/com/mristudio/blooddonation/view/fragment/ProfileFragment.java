package com.mristudio.blooddonation.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.view.activity.MainActivity;
import com.mristudio.blooddonation.view.activity.UserSignInActivity;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ProfileFragment extends Fragment {

    private TextView tvUserProfileName, tvUserCurrentLocation, tvUserBloodGroup;
    private ImageButton ibEditProfile;
    private ImageView ivWantToDonate, ivNearByDonner, ivSocialGroupLink, ivDonnerCard, ivHistory, ivInViteFriends, ivPrivacypolicy, ivRateUs, ivReportToDeveloper, ivLogout;
    private LabeledSwitch swButton;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);
        initView(view);

        view.findViewById(R.id.ibEditProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.success(getActivity(), "Edit Button Click ", Toasty.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void initView(View view) {
        tvUserProfileName = view.findViewById(R.id.tvUserProfileName);
        tvUserCurrentLocation = view.findViewById(R.id.tvUserCurrentLocation);
        tvUserBloodGroup = view.findViewById(R.id.tvUserBloodGroup);
        ibEditProfile = view.findViewById(R.id.ibEditProfile);
        ivWantToDonate = view.findViewById(R.id.ivWantToDonate);
        swButton = view.findViewById(R.id.swButton);
        ivDonnerCard = view.findViewById(R.id.ivDonnerCard);
        ivHistory = view.findViewById(R.id.ivHistory);
        ivInViteFriends = view.findViewById(R.id.ivInViteFriends);
        ivPrivacypolicy = view.findViewById(R.id.ivPrivacypolicy);
        ivRateUs = view.findViewById(R.id.ivRateUs);
        ivReportToDeveloper = view.findViewById(R.id.ivReportToDeveloper);
        ivLogout = view.findViewById(R.id.ivLogout);
        ivNearByDonner = view.findViewById(R.id.ivNearByDonner);
        ivSocialGroupLink = view.findViewById(R.id.ivSocialGroupLink);
        swButton.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn)
                    Toasty.success(getActivity(), "ON", Toasty.LENGTH_SHORT).show();
                else
                    Toasty.success(getActivity(), "OFF", Toasty.LENGTH_SHORT).show();
            }
        });

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {

                    Map<String, Object> tokenMap = new HashMap<>();
                    tokenMap.put("token", "");

                    FirebaseDatabase.getInstance().getReference()
                            .child("generalUserTable")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .updateChildren(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                FirebaseAuth.getInstance().signOut();
                                Toasty.success(getActivity(), "Sucessfully Signout !", Toasty.LENGTH_SHORT).show();

                                startActivity(new Intent(getActivity(), UserSignInActivity.class));

                            }
                        }
                    });


                }
            }
        });

        ivDonnerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.CustomAlertDialog));

                ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.lyt_member_card, viewGroup, false);

                TextView buttonOk = dialogView.findViewById(R.id.messageButton);

                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });


                //e.g. top + right margins:
//                alertDialog.getWindow().setGravity(Gravity.CENTER);
//                WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
//               layoutParams.verticalMargin = 30; // right margin
//               // layoutParams.y = 50; // top margin
//                alertDialog.getWindow().setAttributes(layoutParams);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.success(getActivity(), "History", Toasty.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public void onResume() {
        super.onResume();

        TextView tittleTv = MainActivity.toolbar.findViewById(R.id.toolbar_Hompage_titleTV);
        tittleTv.setText("LPI BLOOD BANK");
//        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
       // MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();

//        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        //MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
    }
}