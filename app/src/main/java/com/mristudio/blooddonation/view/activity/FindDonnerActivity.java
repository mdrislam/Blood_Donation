package com.mristudio.blooddonation.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.BloodGroup_SelectedAdapter;
import com.mristudio.blooddonation.adapter.Caouse_SelectedAdapter;
import com.mristudio.blooddonation.model.RequestModel;
import com.mristudio.blooddonation.model.UserInformation;
import com.mristudio.blooddonation.notification.APIService;
import com.mristudio.blooddonation.notification.Client;
import com.mristudio.blooddonation.notification.Data;
import com.mristudio.blooddonation.notification.MyResponse;
import com.mristudio.blooddonation.notification.Sender;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class FindDonnerActivity extends AppCompatActivity {

    private Toolbar toolbar;

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
    private APIService apiService;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_donner);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
        loadUserData(FirebaseAuth.getInstance().getCurrentUser().getUid());
        provideBloodGroupToggleButton();
        provideCause();

    }


    private void initView() {

        postRef = FirebaseDatabase.getInstance().getReference().child("USER_PUBLIC_POST");

        bloodGroupRV = findViewById(R.id.bloodGroupRV);
        rvCaseList = findViewById(R.id.rvCaseList);
        eTaddressOfHospital = findViewById(R.id.eTaddressOfHospital);
        etRequestMessage = findViewById(R.id.etRequestMessage);
        tvAddImage = findViewById(R.id.tvAddImage);
        tvMale = findViewById(R.id.tvMale);
        tvFemale = findViewById(R.id.tvFemale);
        tvSelectDateTime = findViewById(R.id.tvSelectDateTime);
        tvUnits = findViewById(R.id.tvUnits);
        ibminus = findViewById(R.id.ibminus);
        ibPluse = findViewById(R.id.ibPluse);
        cbIsurgent = findViewById(R.id.cbIsurgent);
        btnPublish = findViewById(R.id.btnPublish);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

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

                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LocationFragment()).addToBackStack(null).commit();

                Intent intent = new Intent(FindDonnerActivity.this, AutoLocationActivity.class);
                startActivity(intent);
                Log.e(TAG, "onClick: ");
                overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
            }
        });

        tvSelectDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int YEAR = calendar.get(Calendar.YEAR);
                int MONTH = calendar.get(Calendar.MONTH);
                int DATE = calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(FindDonnerActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                            notifID, currentUser.getUid(),
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

                                                    String tittle = "Need " + bloodGroup + " Blood for" + couseOfBlood + " Patient";
                                                    //    Data(String tittle, String type, String imageurl, String description, String address, String postId)
                                                    /**
                                                     * Send Notification
                                                     * */
                                                    senNotification(bloodGroup, tittle, uri.toString(), requestMessage, "" + addressOfHospitalString, postID);

                                                    progress.dismiss();

                                                    Toasty.success(FindDonnerActivity.this, "Saved", Toasty.LENGTH_SHORT).show();

                                                    startActivity(new Intent(FindDonnerActivity.this, MainActivity.class));
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progress.dismiss();
                                                    Toasty.success(FindDonnerActivity.this, e.getMessage(), Toasty.LENGTH_SHORT).show();

                                                }
                                            });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progress.dismiss();
                                            Log.e(TAG, "onFailure:Database:  " + e.getMessage());
                                            Toasty.success(FindDonnerActivity.this, e.getMessage(), Toasty.LENGTH_SHORT).show();

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
                            Toasty.success(FindDonnerActivity.this, e.getMessage(), Toasty.LENGTH_SHORT).show();
                        }
                    });


                }

            }
        });
    }

    private void loadUserData(String receiverId) {
        Log.e(TAG, "chatProfile: " + receiverId);
        FirebaseDatabase.getInstance().getReference().child("generalUserTable").child(receiverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInformation userInformation = snapshot.getValue(UserInformation.class);
                if (userInformation != null) {
                    userProfileName = userInformation.getName();
                    userProfileImg = userInformation.getUserProfilePicture();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Toast.makeText(RecentRequestActivity.this, ""+tblId, Toast.LENGTH_SHORT).show();
    }

    /**
     * Cheak Users Inputs Validations
     */
    private boolean userInputsValidation(String bloodGroup, String addressOfHospital, Address addressArray, String requestMessage, String imagePath, String couseOfBlood, String gender, String finalDate, String finalTime, Integer units, boolean cheakBox) {

        etRequestMessage.setError(null);
        if (bloodGroup == null || bloodGroup == "") {
            Toasty.warning(FindDonnerActivity.this, "Please ! Select Blood Group", Toasty.LENGTH_SHORT).show();
            return false;
        } else if (addressArray == null) {
            Toasty.warning(FindDonnerActivity.this, "Please ! Address Of Hospital .", Toasty.LENGTH_SHORT).show();
            return false;
        } else if (requestMessage.isEmpty()) {
            etRequestMessage.setError("Please ! Read Some Request Message .");
            return false;
        } else if (imagePath == null || imagePath == "") {
            Toasty.warning(FindDonnerActivity.this, "Please ! Select Image .", Toasty.LENGTH_SHORT).show();
            return false;
        } else if (couseOfBlood == null || couseOfBlood == "") {
            Toasty.warning(FindDonnerActivity.this, "Please ! Select Causes Of Blood Request .", Toasty.LENGTH_SHORT).show();
            return false;
        } else if (gender == null || gender == "") {
            Toasty.warning(FindDonnerActivity.this, "Please ! Select Patient Gender  .", Toasty.LENGTH_SHORT).show();
            return false;
        } else if (finalDate == null || finalDate == "" || finalTime == null || finalDate == "") {
            Toasty.warning(FindDonnerActivity.this, "Please ! Select Date & Time .", Toasty.LENGTH_SHORT).show();
            return false;
        } else if (units < 0) {
            Toasty.warning(FindDonnerActivity.this, "Please ! Select  Bloods Units ", Toasty.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    private String concateString(String blood) {
        if (blood.contains("+")) {
            return blood.replace("+", "_Plus");
        } else if (blood.contains("-")) {
            return blood.replace("-", "_Minus");
        } else {
            return "none";
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            pickedImage = data.getData();
            Log.e(TAG, "uri data= " + pickedImage);
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
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
        final Boolean is24Formate = DateFormat.is24HourFormat(FindDonnerActivity.this);
        TimePickerDialog timePickerDialog = new TimePickerDialog(FindDonnerActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(FindDonnerActivity.this, 4);
        bloodGroupRV.setLayoutManager(gridLayoutManager);
        BloodGroup_SelectedAdapter adapter = new BloodGroup_SelectedAdapter(FindDonnerActivity.this, bloodNames, new BloodGroup_SelectedAdapter.ClickButton() {
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
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(FindDonnerActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvCaseList.setLayoutManager(gridLayoutManager);
        Caouse_SelectedAdapter adapter = new Caouse_SelectedAdapter(FindDonnerActivity.this, bloodNames, new Caouse_SelectedAdapter.ClickButton() {
            @Override
            public void seLectedBloodGroup(String cause) {
                // Toasty.success(getActivity(), "" + groupName, Toasty.LENGTH_SHORT).show();
                couseOfBlood = cause;
            }
        });
        adapter.setHasStableIds(true);
        rvCaseList.setAdapter(adapter);


    }

    //Send Notify Specypic User
    private void senNotification(String topic, String tittle, String imageurl, String description, String address, String postId) {

        //Data(String tittle, String type, String imageurl, String description, String address, String postId) {
        Data data = new Data(tittle, "topic", imageurl, description, address, postId);

        Sender sender = new Sender(data, "/topics/" + concateString(topic));

        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toasty.error(FindDonnerActivity.this, "Faild", Toasty.LENGTH_SHORT).show();
                        Log.e(TAG, "onResponse: " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });


    }

    //get Making Image Url
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        Log.e(TAG, "Image Extention : " + mime.getExtensionFromMimeType(cR.getType(uri)));
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onResume() {
        super.onResume();

        if (addressArray != null)
            eTaddressOfHospital.setText(addressofHopital + " " + addressArray.getAddressLine(0));

        getSupportActionBar().setTitle("Find Donner");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

    }

    @Override
    public void onPause() {
        super.onPause();

        getSupportActionBar().setTitle("Find Donner");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    /**
     * Load ProgressDialog
     */
    public void progressDialog() {
        progress = new ProgressDialog(FindDonnerActivity.this);
        progress.setMessage("Please Wait a Several Time.....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
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
}