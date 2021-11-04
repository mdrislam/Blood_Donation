package com.mristudio.blooddonation.view.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.BloodGroup_SelectedAdapter;
import com.mristudio.blooddonation.adapter.Caouse_SelectedAdapter;
import com.mristudio.blooddonation.model.RequestModel;
import com.mristudio.blooddonation.view.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;


public class FindDonnerFragment extends Fragment {

    private static final int LOAD_IMAGE_RESULTS = 1;
    private RecyclerView bloodGroupRV, rvCaseList;
    private String bloodGroup, couseOfBlood, gender, finalDate, finalTime;
    private EditText etRequestMessage;
    public static EditText eTaddressOfHospital;
    private TextView tvAddImage, tvMale, tvFemale, tvSelectDateTime, tvUnits;
    private ImageButton ibminus, ibPluse;
    private CheckBox cbIsurgent;
    private Button btnPublish;
    private Integer units = 1;
    private Uri url = null;
    private String imagePath;
    public static String addressofHopital;
    public static Address addressArray;
    private ProgressDialog progress;

    private DatabaseReference postRef;
    private DatabaseReference notifRef;
    private String userID;
    private String userProfileName;
    private String userProfileImg;
    private Uri pickedImage;

    private static StorageReference mstorageReference;
    private static StorageTask storageTask;

    public FindDonnerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_donner, container, false);
        initView(view);
        provideBloodGroupToggleButton();
        provideCause();

        return view;
    }

    private void initView(View view) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            userProfileName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            userProfileImg = "" + FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();

        }
        postRef = FirebaseDatabase.getInstance().getReference().child("USER_PUBLIC_POST");

        bloodGroupRV = view.findViewById(R.id.bloodGroupRV);
        rvCaseList = view.findViewById(R.id.rvCaseList);

        eTaddressOfHospital = view.findViewById(R.id.eTaddressOfHospital);
        etRequestMessage = view.findViewById(R.id.etRequestMessage);
        tvAddImage = view.findViewById(R.id.tvAddImage);
        tvMale = view.findViewById(R.id.tvMale);
        tvFemale = view.findViewById(R.id.tvFemale);
        tvSelectDateTime = view.findViewById(R.id.tvSelectDateTime);
        tvUnits = view.findViewById(R.id.tvUnits);
        ibminus = view.findViewById(R.id.ibminus);
        ibPluse = view.findViewById(R.id.ibPluse);
        cbIsurgent = view.findViewById(R.id.cbIsurgent);
        btnPublish = view.findViewById(R.id.btnPublish);
        tvMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMale.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic__check_for_gender, 0, 0, 0);
                tvFemale.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                gender = "Male";
            }
        });
        tvFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvFemale.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic__check_for_gender, 0, 0, 0);
                tvMale.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                // tvFemale.setTextColor(getResources().getColor(R.color.green));
                gender = "Female";
            }
        });
        ibminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (units > 0) {
                    units--;
                    tvUnits.setText(units.toString());
                }
            }
        });
        ibPluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (units > 0) {
                    units++;
                    tvUnits.setText(units.toString());
                }
            }
        });
        eTaddressOfHospital.setFocusable(false);

        eTaddressOfHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LocationFragment()).addToBackStack(null).commit();

            }
        });

        tvSelectDateTime.setOnClickListener(new View.OnClickListener() {
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
                        CharSequence charSequence = DateFormat.format(" MMM d, yyyy", calendar2);
                        finalDate = String.valueOf(charSequence);
                        selectTime(finalDate);

                    }
                }, YEAR, MONTH, DATE);
                datePickerDialog.show();
            }
        });
        tvAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, LOAD_IMAGE_RESULTS); //LOAD_IMAGE_RESULTS
            }
        });

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressOfHospitalString = eTaddressOfHospital.getText().toString();
                String requestMessage = etRequestMessage.getText().toString().toString();
                boolean cheakBox = cbIsurgent.isChecked();
                if (userInputsValidation(bloodGroup, addressOfHospitalString, addressArray, requestMessage, imagePath, couseOfBlood, gender, finalDate, finalTime, units, cheakBox)) {
                    progressDialog();
                    java.text.DateFormat dateFormaet = new SimpleDateFormat("MMM d, yyyy hh:mm a");
                    android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
                    String postDateTime = dateFormaet.format(cal.getTime());

                    notifRef = FirebaseDatabase.getInstance().getReference().child("USER_NOTIFICATION").child(bloodGroup).child(addressArray.getLocality());

                    String notifID = notifRef.push().getKey();

                    RequestModel requestModel = new RequestModel(
                            notifID, userID,
                            notifID, bloodGroup,
                            addressofHopital,
                            addressOfHospitalString,
                            requestMessage,
                            addressArray.getLocality(), imagePath,
                            couseOfBlood, gender, finalDate,
                            finalTime, units, cheakBox,
                            0,
                            0, userProfileName,
                            userProfileImg, postDateTime);

                    mstorageReference = FirebaseStorage.getInstance().getReference("userSlidersImages");
                    final StorageReference imageRefarence = mstorageReference.child(System.currentTimeMillis() + "." + getFileExtension(pickedImage));

                    storageTask = imageRefarence.putFile(pickedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRefarence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //Update requestModel  Image Url
                                    requestModel.setImagesUrl(uri.toString());

                                    //Insert Notify Coloum
                                    notifRef.child(notifID).setValue(requestModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            String postID = postRef.push().getKey();
                                            requestModel.setTblId(postID);

                                            //Insert Post to request Donner.
                                            postRef.child(postID).setValue(requestModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    progress.dismiss();
                                                    Toasty.success(getActivity(), "Saved", Toasty.LENGTH_SHORT).show();

                                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progress.dismiss();
                                                    Toasty.success(getActivity(), e.getMessage(), Toasty.LENGTH_SHORT).show();

                                                }
                                            });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progress.dismiss();
                                            Log.e(TAG, "onFailure:Database:  " + e.getMessage());
                                            Toasty.success(getActivity(), e.getMessage(), Toasty.LENGTH_SHORT).show();

                                        }
                                    });

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progress.dismiss();
                            Log.e(TAG, "onFailure:Image:  " + e.getMessage());
                            Toasty.success(getActivity(), e.getMessage(), Toasty.LENGTH_SHORT).show();
                        }
                    });


                }

            }
        });
    }

    /**
     * Cheak Users Inputs Validations
     */
    private boolean userInputsValidation(String bloodGroup, String addressOfHospital, Address addressArray, String requestMessage, String imagePath, String couseOfBlood, String gender, String finalDate, String finalTime, Integer units, boolean cheakBox) {

        etRequestMessage.setError(null);
        if (bloodGroup == null || bloodGroup == "") {
            Toasty.warning(getActivity(), "Please ! Select Blood Group", Toasty.LENGTH_SHORT).show();
            return false;
        } else if (addressArray == null) {
            Toasty.warning(getActivity(), "Please ! Address Of Hospital .", Toasty.LENGTH_SHORT).show();
            return false;
        } else if (requestMessage.isEmpty()) {
            etRequestMessage.setError("Please ! Read Some Request Message .");
            return false;
        } else if (imagePath == null || imagePath == "") {
            Toasty.warning(getActivity(), "Please ! Select Image .", Toasty.LENGTH_SHORT).show();
            return false;
        } else if (couseOfBlood == null || couseOfBlood == "") {
            Toasty.warning(getActivity(), "Please ! Select Causes Of Blood Request .", Toasty.LENGTH_SHORT).show();
            return false;
        } else if (gender == null || gender == "") {
            Toasty.warning(getActivity(), "Please ! Select Patient Gender  .", Toasty.LENGTH_SHORT).show();
            return false;
        } else if (finalDate == null || finalDate == "" || finalTime == null || finalDate == "") {
            Toasty.warning(getActivity(), "Please ! Select Date & Time .", Toasty.LENGTH_SHORT).show();
            return false;
        } else if (units < 0) {
            Toasty.warning(getActivity(), "Please ! Select  Bloods Units ", Toasty.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == getActivity().RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            pickedImage = data.getData();
            Log.e(TAG, "uri data= " + pickedImage);
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            Log.e(TAG, "onActivityResult: " + imagePath);
            tvAddImage.setText(" " + imagePath);

            cursor.close();
        }
    }

    /**
     * Select Date from Date picker Dialog
     */
    private void selectTime(String date) {
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
                finalTime = String.valueOf(charSequence);
                tvSelectDateTime.setText(" " + date + " " + finalTime);
            }
        }, HOUR, MINUTE, is24Formate);
        timePickerDialog.show();

    }

    /**
     * Set Layout Three BloodGroup Data
     */
    private void provideBloodGroupToggleButton() {
        List<String> bloodNames = new ArrayList<>();
        bloodNames.add("A+");
        bloodNames.add("A-");
        bloodNames.add("B+");
        bloodNames.add("B-");
        bloodNames.add("O+");
        bloodNames.add("O-");
        bloodNames.add("AB+");
        bloodNames.add("AB-");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        bloodGroupRV.setLayoutManager(gridLayoutManager);
        BloodGroup_SelectedAdapter adapter = new BloodGroup_SelectedAdapter(getActivity(), bloodNames, new BloodGroup_SelectedAdapter.ClickButton() {
            @Override
            public void seLectedBloodGroup(String groupName) {
//                Toasty.success(getActivity(), "" + groupName, Toasty.LENGTH_SHORT).show();
                bloodGroup = groupName;
            }
        });
        adapter.setHasStableIds(true);
        bloodGroupRV.setAdapter(adapter);


    }

    /**
     * Set Layout Three BloodGroup Data
     */
    private void provideCause() {
        List<String> bloodNames = new ArrayList<>();
        bloodNames.add("Dengue");
        bloodNames.add("Accident");
        bloodNames.add("Cancer");
        bloodNames.add("Thalassemia");
        bloodNames.add("Delivery");
        bloodNames.add("Operation");
        bloodNames.add("Others");
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvCaseList.setLayoutManager(gridLayoutManager);
        Caouse_SelectedAdapter adapter = new Caouse_SelectedAdapter(getActivity(), bloodNames, new Caouse_SelectedAdapter.ClickButton() {
            @Override
            public void seLectedBloodGroup(String cause) {
                // Toasty.success(getActivity(), "" + groupName, Toasty.LENGTH_SHORT).show();
                couseOfBlood = cause;
            }
        });
        adapter.setHasStableIds(true);
        rvCaseList.setAdapter(adapter);


    }


    //get Making Image Url
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        Log.e(TAG, "Image Extention : " + mime.getExtensionFromMimeType(cR.getType(uri)));
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onResume() {
        super.onResume();

        if (addressArray != null)
            eTaddressOfHospital.setText(addressofHopital + " " + addressArray.getAddressLine(0));

        MainActivity.toolbar_profileLyt.setVisibility(View.GONE);
        MainActivity.toolbarHomePageLyt.setVisibility(View.GONE);

        MainActivity.toolbar.setTitle("Find Donner");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
       // MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();

        MainActivity.toolbar_profileLyt.setVisibility(View.GONE);
        MainActivity.toolbarHomePageLyt.setVisibility(View.GONE);

        MainActivity.toolbar.setTitle("Find Donner");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  MainActivity.bottomNavigationView.setVisibility(View.GONE);
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

    private void clearAll() {
        bloodGroup = null;
        couseOfBlood = null;
        gender = null;
        finalDate = null;
        finalTime = null;
        EditText etRequestMessage;
        eTaddressOfHospital.setText(null);
        tvAddImage.setText("add Image");

//          tvMale, tvFemale, tvSelectDateTime, tvUnits;
//        private ImageButton ibminus, ibPluse;
//        private CheckBox cbIsurgent;
//        private Button btnPublish;
//        private Integer units = 1;
//        private Uri url = null;
//        private String imagePath;
//        public static String addressofHopital;
//        public static Address addressArray;
//        private ProgressDialog progress;
//
//        private DatabaseReference postRef;
//        private DatabaseReference notifRef;
//        private String userID;
//        private String userProfileName;
//        private String userProfileImg;
//        private Uri pickedImage;

    }
}