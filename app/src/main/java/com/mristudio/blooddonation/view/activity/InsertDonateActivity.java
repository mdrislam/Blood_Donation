package com.mristudio.blooddonation.view.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.Donation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class InsertDonateActivity extends AppCompatActivity {
    private static final String TAG = "InsertDonateActivity";
    private LinearLayout llInsert, llUpdate;
    private EditText etHospitalName, eTaddressOfHospital;
    private TextView tvSelectDateTime;
    private Button btnSave, btnDelete, btnUpdate;

    Toolbar toolbar;
    private String finalDate = null, finalTime = null, monthName, days;
    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference adminReference;
    private DatabaseReference userReference;
    private Donation donationModel = null;
    private boolean clickStatus = false;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_donate);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etHospitalName = findViewById(R.id.etHospitalName);
        eTaddressOfHospital = findViewById(R.id.eTaddressOfHospital);
        tvSelectDateTime = findViewById(R.id.tvSelectDateTime);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        llUpdate = findViewById(R.id.llUpdate);
        llInsert = findViewById(R.id.llInsert);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("generalUserTable").child(user.getUid()).child("DONATIONS");
        adminReference = FirebaseDatabase.getInstance().getReference().child("AdminNotification");
        userReference = FirebaseDatabase.getInstance().getReference().child("generalUserTable").child(user.getUid());

        if (getIntent().getExtras() != null) {
            donationModel = (Donation) getIntent().getSerializableExtra("model");
            llInsert.setVisibility(View.GONE);
            llUpdate.setVisibility(View.VISIBLE);
            etHospitalName.setText(donationModel.getHospitalName());
            eTaddressOfHospital.setText(donationModel.getAddressofHospital());
            tvSelectDateTime.setText(donationModel.getDonateDate());


            getSupportActionBar().setTitle("Update Donation Date ");
        } else {
            llInsert.setVisibility(View.VISIBLE);
            llUpdate.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Insert Donation Date ");
        }


        tvSelectDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int YEAR = calendar.get(Calendar.YEAR);
                int MONTH = calendar.get(Calendar.MONTH);
                int DATE = calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(InsertDonateActivity.this, new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int date) {
                        // EEEE, MMM d, yyyy
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.set(Calendar.YEAR, year);
                        calendar2.set(Calendar.MONTH, month);
                        calendar2.set(Calendar.DATE, date);

                        CharSequence charSequence = DateFormat.format(" MMM d, yyyy", calendar2);
                        finalDate = String.valueOf(charSequence);
                        monthName = new SimpleDateFormat("MMM").format(calendar2.getTime());
                        days = String.valueOf(date);
                        selectTime(finalDate);

                    }
                }, YEAR, MONTH, DATE);
                datePickerDialog.show();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameOfHospial = etHospitalName.getText().toString();
                String addressofHospital = eTaddressOfHospital.getText().toString();
                String date_time = finalDate + " " + finalTime;
                if (validation(nameOfHospial, addressofHospital, date_time)) {
                    insertDonation(nameOfHospial, addressofHospital, date_time);
                }
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameOfHospial = etHospitalName.getText().toString();
                String addressofHospital = eTaddressOfHospital.getText().toString();
                String date_time = finalDate + " " + finalTime;
                if (validation(nameOfHospial, addressofHospital, date_time)) {
                    donationModel.setAddressofHospital(addressofHospital);
                    donationModel.setHospitalName(nameOfHospial);
                    if (clickStatus) {
                        donationModel.setDonateDate(date_time);
                        donationModel.setMonthName(monthName);
                        donationModel.setDayOfMonth(days);
                        donationModel.setStatus(false);
                    }
                    updateDonation(donationModel);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog();
                reference.child(donationModel.getTableId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Map<String, Object> hashMap = new HashMap<>();
                        hashMap.put("last_donateDate","none");
                        userReference.updateChildren(hashMap);
                        adminReference.child(user.getUid()).removeValue();
                        progress.dismiss();

                        onBackPressed();
                        Toasty.success(InsertDonateActivity.this, "Update Pending Approval to Admin !", Toasty.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progress.dismiss();
                        Log.d(TAG, "onFailure: "+e.getMessage());
                    }
                });

            }
        });

    }

    private void updateDonation(Donation donationModel) {
        progressDialog();
        reference.child(donationModel.getTableId()).setValue(donationModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    adminReference.child(user.getUid()).setValue(donationModel);
                    progress.dismiss();

                    onBackPressed();
                    Toasty.success(InsertDonateActivity.this, "Update Pending Approval to Admin !", Toasty.LENGTH_SHORT).show();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {


            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();

            }
        });
    }

    private void insertDonation(String nameOfHospial, String addressofHospital, String date_time) {
        progressDialog();
        String tblId = reference.push().getKey();
        Donation donation = new Donation(false, user.getUid(), tblId, nameOfHospial, addressofHospital, date_time, days, monthName);
        assert tblId != null;
        reference.child(tblId).setValue(donation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Map<String, Object> hashMap = new HashMap<>();
                    hashMap.put("last_donateDate", date_time);
                    userReference.updateChildren(hashMap);
                    adminReference.child(user.getUid()).setValue(donation);
                    progress.dismiss();

                    onBackPressed();
                    Toasty.success(InsertDonateActivity.this, "Pending Approval to Admin !", Toasty.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
                progress.dismiss();
            }
        });

    }

    private boolean validation(String nameOfHospial, String addressofHospital, String date_time) {

        etHospitalName.setError(null);
        eTaddressOfHospital.setError(null);

        if (nameOfHospial.isEmpty()) {

            etHospitalName.setError("Required*");

        } else if (addressofHospital.isEmpty()) {

            eTaddressOfHospital.setError("Required*");

        } else if (finalTime == null) {

            if (getIntent().getExtras()!=null) {
                return true;

            } else {
                Toasty.error(InsertDonateActivity.this, "Please Select Date Time.", Toasty.LENGTH_SHORT).show();
            }


        } else {

            return true;
        }
        return false;
    }

    /**
     * Select Date from Date picker Dialog
     */
    private void selectTime(String date) {
        final Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        final Boolean is24Formate = DateFormat.is24HourFormat(InsertDonateActivity.this);
        TimePickerDialog timePickerDialog = new TimePickerDialog(InsertDonateActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                final Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                CharSequence charSequence = DateFormat.format("hh:mm a", calendar1);
                finalTime = String.valueOf(charSequence);
                tvSelectDateTime.setText(date + " " + finalTime);
                if (donationModel != null) {
                    clickStatus = true;
                }

            }
        }, HOUR, MINUTE, is24Formate);
        timePickerDialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public void onPause() {
        super.onPause();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        finish();
        return true;
    }

    /**
     * Load ProgressDialog
     */
    public void progressDialog() {
        progress = new ProgressDialog(InsertDonateActivity.this);
        progress.setMessage("Please Wait a Several Time.....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }
}