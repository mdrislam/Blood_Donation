package com.mristudio.blooddonation.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.mristudio.blooddonation.R;

public class AdminActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RelativeLayout toolbarHomePageLyt;
    private TextView toolbar_Hompage_titleTV;
    private ImageButton adminToolBarNext, adminToolBarExit;
    private RelativeLayout lytManageSlider, lytManagedonation, lytManageDonners, lytAdminProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();

    }

    /**
     * Initialize all Views
     * Set All Lesenner
     */
    private void initView() {

        /**
         * Toolbar View Initialize
         * */
        toolbarHomePageLyt = toolbar.findViewById(R.id.toolbarHomePageLyt);
        toolbar_Hompage_titleTV = toolbar.findViewById(R.id.toolbar_Hompage_titleTV);
        adminToolBarExit = toolbar.findViewById(R.id.adminToolBarExit);
        adminToolBarNext = toolbar.findViewById(R.id.adminToolBarNext);

        lytManageSlider = findViewById(R.id.lytManageSlider);
        lytManagedonation = findViewById(R.id.lytManagedonation);
        lytManageDonners = findViewById(R.id.lytManageDonners);
        lytAdminProfile = findViewById(R.id.lytAdminProfile);

        /**Toolbar On SignOut and Logout Lesener
         * */
        adminToolBarExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminActivity.this, UserSignInActivity.class));
            }
        });
        /**Toolbar On Mainactivity Lesenner
         * */
        adminToolBarNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, MainActivity.class));
            }
        });

        /**
         * main admin lytManageSlider Lesenner
         * */
        lytManageSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, ManageSliderActivity.class));

            }
        });
        /**
         * main admin lytManagedonation Lesenner
         * */
        lytManagedonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, ManageDonationActivity.class));

            }
        });
        /**
         * main admin lytManageDonners Lesenner
         * */
        lytManageDonners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, ManageUserActivity.class));

            }
        });
        /**
         * main admin lytAdminProfile Lesenner
         * */
        lytAdminProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}