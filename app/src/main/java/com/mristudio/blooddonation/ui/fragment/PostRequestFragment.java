package com.mristudio.blooddonation.ui.fragment;

import android.animation.FloatEvaluator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.District_Name;
import com.mristudio.blooddonation.model.PostRequestModel;
import com.mristudio.blooddonation.model.Upazila_Name;
import com.mristudio.blooddonation.ui.activity.MainActivity;
import com.mristudio.blooddonation.ui.activity.PostARequestActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;
import static com.mristudio.blooddonation.ui.activity.MainActivity.bottomNavigationView;
import static com.mristudio.blooddonation.ui.activity.MainActivity.toolbar;
import static com.mristudio.blooddonation.ui.activity.MainActivity.toolbarHomePageLyt;

public class PostRequestFragment extends Fragment {
    private EditText eTrelationShip, eTHospiptalName, eTaddressOfHospital, etContactNumber;
    private Spinner bloodGroupSP, spAreaOfCity, spAreaOfCityUpazila;
    private CheckBox cheakBox;
    private TextView shedulledTVButton;
    private Button btnPulishButton;
    private ArrayAdapter<String> mainSpAdapter;
    private ArrayAdapter<String> districtSpAdapter;
    private ArrayAdapter<String> upazilaSpAdapter;
    private Toolbar toolbar;
    private TextView adminHomePageTittle, cscheduleTime;
    private ImageButton adminToolBarBack, selectDateIB;
    private String bloodname, areaOfCityName, areaOfUpazilaName;
    private ArrayList<District_Name> district_names = new ArrayList<>();
    private ArrayList<String> districtaNameString = new ArrayList<>();

    private ArrayList<Upazila_Name> upazila_names = new ArrayList<>();
    private ArrayList<String> upazilNameString = new ArrayList<>();

    private static AlertDialog buttonAlertDialog;
    private TextView tvDonationDate, tvSelectDateButton, tvDonationTime, tvSelectTimeButton, tvCancelButton, tvDoneButton;
    private String datefinal, timefinal;
    private ProgressDialog progress;

    public PostRequestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_request, container, false);
        MainActivity.toolbar.setTitle("Post blood  for Request");
        setHasOptionsMenu(true);
        initView(view);
        return view;
    }

    private void initView(View view) {
        /**
         * Initialize View
         * */
        eTHospiptalName = view.findViewById(R.id.eTHospiptalName);
        eTaddressOfHospital = view.findViewById(R.id.eTaddressOfHospital);
        eTrelationShip = view.findViewById(R.id.eTrelationShip);
        etContactNumber = view.findViewById(R.id.etContactNumber);
        spAreaOfCity = view.findViewById(R.id.spAreaOfCity);
        spAreaOfCityUpazila = view.findViewById(R.id.spAreaOfCityUpazila);
        bloodGroupSP = view.findViewById(R.id.bloodGroupSP);
        cheakBox = view.findViewById(R.id.cheakBox);
        shedulledTVButton = view.findViewById(R.id.shedulledTVButton);
        cscheduleTime = view.findViewById(R.id.cscheduleTime);
        selectDateIB = view.findViewById(R.id.selectDateIB);
        btnPulishButton = view.findViewById(R.id.btnPulishButton);

        provideDistrictName();
        List<String> bloodNames = new ArrayList<>();
        bloodNames.add("A+");
        bloodNames.add("A-");
        bloodNames.add("B+");
        bloodNames.add("B-");
        bloodNames.add("O+");
        bloodNames.add("O-");
        bloodNames.add("AB+");
        bloodNames.add("AB-");
        mainSpAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, bloodNames);
        mainSpAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        bloodGroupSP.setSelection(0);
        bloodGroupSP.setAdapter(mainSpAdapter);

        spAreaOfCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    Toasty.success(getActivity(), "" + districtaNameString.get(position), Toast.LENGTH_SHORT, true).show();

                areaOfCityName = districtaNameString.get(position);
                provideUpazila(district_names.get(position).getId());
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spAreaOfCityUpazila.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    Toasty.success(getActivity(), "" + upazila_names.get(position).getName(), Toast.LENGTH_SHORT, true).show();
                areaOfUpazilaName = upazila_names.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bloodGroupSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    Toasty.success(getActivity(), "" + bloodNames.get(position), Toast.LENGTH_SHORT, true).show();

                bloodname = bloodNames.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnPulishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = eTHospiptalName.getText().toString().trim();
                String addressOfHosptal = eTaddressOfHospital.getText().toString().trim();
                String relationShip = eTrelationShip.getText().toString().trim();
                String contact = etContactNumber.getText().toString().trim();
                boolean isUrgentCheaked = cheakBox.isChecked();
                if (userInputsValidation(name, addressOfHosptal, relationShip, contact, isUrgentCheaked)) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        progressDialog();
                        insertNewBloodRequestPost(name, addressOfHosptal, relationShip, contact, isUrgentCheaked);
                    } else {
                        Toasty.error(getActivity(), "Please Signin First !", Toasty.LENGTH_SHORT).show();
                    }
                }

            }
        });

        selectDateIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectDialog();
            }
        });
    }

    /***
     * Insert New Blood Request Post
     *
     * @param name
     * @param addressOfHosptal
     * @param relationShip
     * @param contact
     * @param isUrgentCheaked*/
    private void insertNewBloodRequestPost(String name, String addressOfHosptal, String relationShip, String contact, boolean isUrgentCheaked) {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference forAllRef = FirebaseDatabase.getInstance().getReference().child("BlOOD_REQUEST_FOR_ALL");
        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("NOTIFICATION");
        DatabaseReference myRequestRef = FirebaseDatabase.getInstance().getReference().child("MY_REQUEST");
        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        PostRequestModel modelforAll = new PostRequestModel(uId, name, timefinal, datefinal, bloodname, addressOfHosptal, relationShip, contact, false, areaOfCityName, areaOfUpazilaName, isUrgentCheaked);
        PostRequestModel modelforNOtification = new PostRequestModel(uId, name, timefinal, datefinal, bloodname, addressOfHosptal, relationShip, contact, false, areaOfCityName, areaOfUpazilaName, isUrgentCheaked);
        PostRequestModel modelforMyRequest = new PostRequestModel(uId, name, timefinal, datefinal, bloodname, addressOfHosptal, relationShip, contact, false, areaOfCityName, areaOfUpazilaName, isUrgentCheaked);

        String forAllUId = forAllRef.push().getKey();
        modelforAll.setuId(forAllUId);
        String myrequestUId = myRequestRef.push().getKey();
        modelforMyRequest.setuId(myrequestUId);

        forAllRef.child(uId).setValue(modelforAll).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    notificationRef.child(bloodname).child(uId).setValue(modelforNOtification);
                    myRequestRef.child(myrequestUId).setValue(modelforMyRequest);
                    progress.dismiss();
                    Toasty.success(getActivity(), "SucessFully Request Posted ", Toasty.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
                Log.e(TAG, "onFailure: " + e);
                Toasty.error(getContext(), "There was Some Problem Found .Please ! try Again .", Toasty.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Cheak UserTypy Validation
     */
    private boolean userInputsValidation(String name, String addressOfHosptal, String relationShip, String contact, boolean isUrgentCheaked) {


        eTHospiptalName.setError(null);
        eTaddressOfHospital.setError(null);
        eTrelationShip.setError(null);
        if (name.isEmpty()) {
            eTHospiptalName.setError("Hopital Name Field is Empty !");

            return false;
        } else if (addressOfHosptal.isEmpty()) {
            eTaddressOfHospital.setError("Address Field is Empty");

            return false;
        } else if (areaOfCityName == null) {
            Toasty.warning(getActivity(), "Please! Select Area of City", Toast.LENGTH_SHORT).show();

            return false;
        } else if (bloodname == null) {
            Toasty.warning(getActivity(), "Please! Select Blood Group .", Toast.LENGTH_SHORT).show();

            return false;
        } else if (relationShip.isEmpty()) {
            eTrelationShip.setError("Relationship field is Empty !");

            return false;
        }
        if (contact.isEmpty()) {
            etContactNumber.setError("Empty Not Allowed !");

            return false;

        } else if (contact.length() < 11 || contact.length() > 12 || !android.util.Patterns.PHONE.matcher("+88" + contact).matches()) {
            etContactNumber.setError("Invalid  Contact Number");

            return false;
        } else if (timefinal == null || datefinal == null) {
            Toasty.warning(getActivity(), "Please ! Select Sheduled Date and Time", Toasty.LENGTH_SHORT).show();

            return false;
        } else {

            return true;
        }


    }


    /***
     * get District name list from Firebase
     * */
    private void provideDistrictName() {
        district_names.clear();
        districtaNameString.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("district_name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()) {
                    District_Name district_name = data.getValue(District_Name.class);
                    district_names.add(district_name);
                    districtaNameString.add(district_name.getName());
                }
                districtSpAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, districtaNameString);
                districtSpAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                spAreaOfCity.setSelection(0);
                spAreaOfCity.setAdapter(districtSpAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**
     * get Upazila Name from Firebase
     */
    private void provideUpazila(String district_id) {
        upazila_names.clear();
        upazilNameString.clear();
        upazilaSpAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, upazilNameString);
        upazilaSpAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spAreaOfCityUpazila.setAdapter(upazilaSpAdapter);
        spAreaOfCityUpazila.setSelection(0);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("upazila_names").orderByChild("district_id").equalTo(district_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // dataSnapshot is the "issue" node with all children with id 0
                for (DataSnapshot data : snapshot.getChildren()) {
                    // do something with the individual "issues"
                    Upazila_Name upazila_name = data.getValue(Upazila_Name.class);
                    upazila_names.add(upazila_name);
                    upazilNameString.add(upazila_name.getName());
                    Log.e(TAG, "onDataChange:Upazila  " + upazila_name.getName());
                }

                upazilaSpAdapter.notifyDataSetChanged();
                Log.e(TAG, "onDataChange: " + snapshot.hasChildren());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error);
            }
        });
    }

    /**
     * Show Time & date Select Popup Dialog box
     */
    private void openSelectDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        final View mView = LayoutInflater.from(getActivity()).inflate(R.layout.custom_time_date_select_dialog, null);
        tvDonationDate = mView.findViewById(R.id.tvDonationDate);
        tvSelectDateButton = mView.findViewById(R.id.tvSelectDateButton);
        tvDonationTime = mView.findViewById(R.id.tvDonationTime);
        tvSelectTimeButton = mView.findViewById(R.id.tvSelectTimeButton);
        tvCancelButton = mView.findViewById(R.id.tvCancelButton);
        tvDoneButton = mView.findViewById(R.id.tvDoneButton);
        mBuilder.setView(mView);
        buttonAlertDialog = mBuilder.create();
        buttonAlertDialog.setCancelable(false);

        //cheak date is null or not
        if (datefinal == null) {
            tvDonationDate.setText("00/00/00");
        } else {
            tvDonationDate.setText(datefinal.toString());
        }
        //cheak time is null or not
        if (timefinal == null) {
            tvDonationTime.setText("00:00 PM/AM");
        } else {
            tvDonationTime.setText(timefinal.toString());
        }
        buttonAlertDialog.show();

        tvSelectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int YEAR = calendar.get(Calendar.YEAR);
                int MONTH = calendar.get(Calendar.MONTH);
                int DATE = calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int date) {
                        // EEEE, MMM d, yyyy

                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.set(Calendar.YEAR, year);
                        calendar2.set(Calendar.MONTH, month);
                        calendar2.set(Calendar.DATE, date);
                        CharSequence charSequence = DateFormat.format("E, MMM d, yyyy", calendar2);
                        datefinal = String.valueOf(charSequence);
                        tvDonationDate.setText(charSequence);

                    }
                }, YEAR, MONTH, DATE);
                datePickerDialog.show();
            }
        });

        tvSelectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int HOUR = calendar.get(Calendar.HOUR);
                int MINUTE = calendar.get(Calendar.MINUTE);
                final Boolean is24Formate = DateFormat.is24HourFormat(getActivity());
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        final Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.HOUR, hour);
                        calendar1.set(Calendar.MINUTE, minute);
                        CharSequence charSequence = DateFormat.format("hh:mm a", calendar1);
                        timefinal = String.valueOf(charSequence);
                        tvDonationTime.setText(charSequence);
                    }
                }, HOUR, MINUTE, is24Formate);
                timePickerDialog.show();
            }
        });
        tvDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datefinal == null) {
                    Toasty.warning(getActivity(), "Please ! Select Sheduled Date .", Toasty.LENGTH_SHORT).show();

                } else if (timefinal == null) {
                    Toasty.warning(getActivity(), "Please ! Select Sheduled Time .", Toasty.LENGTH_SHORT).show();

                } else {
                    cscheduleTime.setText(datefinal + " at " + timefinal);
                    buttonAlertDialog.dismiss();
                }
            }
        });

        tvCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAlertDialog.dismiss();
            }
        });


    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {

        menu.findItem(R.id.toolbar_settings).setVisible(true);
        MainActivity.toolbarHomePageLyt.setVisibility(View.GONE);

        MainActivity.bottomNavigationView.setVisibility(View.GONE);

        super.onPrepareOptionsMenu(menu);
    }

    /**
     * Cheak Data Connection is ON or OFF
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    /**
     * Load ProgressDialog
     */
    public void progressDialog() {
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Please Wait a Several Time.....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }
}