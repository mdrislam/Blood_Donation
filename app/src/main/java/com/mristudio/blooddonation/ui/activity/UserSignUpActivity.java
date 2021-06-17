package com.mristudio.blooddonation.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.BloodGroupSelectedAdapter;
import com.mristudio.blooddonation.model.UserInformation;
import com.mristudio.blooddonation.utility.Session;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class UserSignUpActivity extends AppCompatActivity implements BloodGroupSelectedAdapter.BloodToogleButtonClick {

    private static final String TAG = "UserSignUpActivity";
    private int counter = 1;
    private Button sinUpNextButton;

    /**
     * Layout One
     */
    private LinearLayout userNameEmailPasswordLytOne;
    private EditText signUpuserNameET, signUpEamilET, signUpPasswordET;

    /**
     * Layout Two
     */
    private LinearLayout userPhonALTPhoneSocialLInksLayoutTwo;
    private EditText signUpPhoneNumberET, signUpAlternativePhoneNoET, signUpSocialET;

    /**
     * Layout Three
     */
    private LinearLayout userSignUpBloodGroupSelectLayoutThree;
    private RecyclerView bloodGroupRV;
    private String bloodGroupName = null;

    /**
     * Layout Four
     */
    private LinearLayout userSignUpWeaightSelectLayoutFour;
    private LinearLayout weaightPerfectLyt, weaightNotPerfectLyt;
    private Boolean weightStatus = true;
    private Boolean selectWeightStatus = false;

    /**
     * Layout Five
     */
    private LinearLayout userSignUpGenderSelectLayoutFive;
    private LinearLayout isMaleLyt, isFemaleLyt;
    private String genderStatus = null;
    private Boolean selectGenderStatus = false;

    /**
     * Layout Six
     */
    private LinearLayout userSignupAddressLayoutSix;
    private EditText signUpStreetAddressET, userSignUpPoliceStationET, signUpDistrictET;

    /**
     * Layout Seven Variables
     */
    private LinearLayout userSignupBirthdaySelectLayoutSeven;
    private NumberPicker userBirthDaysPicker, userBirthMonthPicker, userBirthYearPicker;
    private String birthDay = null;
    private String birthMonth = null;
    private String birthYear = null;

    /**
     * Layout Eight Variables
     */
    private LinearLayout userSignupImageSelectLayoutEight, imageUploadbuttonLayout;
    private ImageView uploadImageButton;
    private CircleImageView profile_image;
    private Uri url = null;
    private static UserInformation userInformation = new UserInformation();

    /**
     * firebase Raferance
     */
    private Session session;
    private DatabaseReference rootRer;
    private DatabaseReference generalUserRef;
    private FirebaseUser user;
    private ProgressDialog progress;
    private static StorageReference mstorageReference;
    private static StorageTask storageTask;
    private String downloadImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        /**
         * Initialize Custom Toolbar and set OnBackPress Method when user click OnNavigationUp Button
         * */
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        getSupportActionBar().setElevation(0);

        View view = getSupportActionBar().getCustomView();
        ImageButton backButton = view.findViewById(R.id.action_bar_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        initView();

    }

    /**
     * Initialize All View In SignUp Activity and Set Lesenner
     */
    private void initView() {

        session = new Session(this);
        sinUpNextButton = findViewById(R.id.sinUpNextButton);


        /**
         * layout One InitView
         * */
        userNameEmailPasswordLytOne = findViewById(R.id.userNameEmailPasswordLytOne);
        signUpuserNameET = findViewById(R.id.signUpuserNameET);
        signUpEamilET = findViewById(R.id.signUpEamilET);
        signUpPasswordET = findViewById(R.id.signUpPasswordET);
        userNameEmailPasswordLytOne.setVisibility(View.VISIBLE);

        /**
         * layout Two InitView
         * */
        userPhonALTPhoneSocialLInksLayoutTwo = findViewById(R.id.userPhonALTPhoneSocialLInksLayoutTwo);
        signUpPhoneNumberET = findViewById(R.id.signUpPhoneNumberET);
        signUpAlternativePhoneNoET = findViewById(R.id.signUpAlternativePhoneNoET);
        signUpSocialET = findViewById(R.id.signUpSocialET);

        /**
         * layout Three InitView
         * */
        userSignUpBloodGroupSelectLayoutThree = findViewById(R.id.userSignUpBloodGroupSelectLayoutThree);
        bloodGroupRV = findViewById(R.id.bloodGroupRV);
        margeBloodGroupToggleButton();

        /**
         * layout Four InitView
         * */
        userSignUpWeaightSelectLayoutFour = findViewById(R.id.userSignUpWeaightSelectLayoutFour);
        weaightPerfectLyt = findViewById(R.id.weaightPerfectLyt);
        weaightNotPerfectLyt = findViewById(R.id.weaightNotPerfectLyt);
        /**
         * Weignt button Lesenner
         * */
        isWeaightPerfectOrNotCheak();

        /**
         * layout Five InitView
         * */
        userSignUpGenderSelectLayoutFive = findViewById(R.id.userSignUpGenderSelectLayoutFive);
        isMaleLyt = findViewById(R.id.isMaleLyt);
        isFemaleLyt = findViewById(R.id.isFemaleLyt);
        /**Gender button layout
         * */
        genderButtonClickLisenner();

        /**
         * layout Six InitView
         * */
        userSignupAddressLayoutSix = findViewById(R.id.userSignupAddressLayoutSix);
        signUpStreetAddressET = findViewById(R.id.signUpStreetAddressET);
        userSignUpPoliceStationET = findViewById(R.id.userSignUpPoliceStationET);
        signUpDistrictET = findViewById(R.id.signUpDistrictET);


        /**
         * layout Seven InitView
         * */
        userSignupBirthdaySelectLayoutSeven = findViewById(R.id.userSignupBirthdaySelectLayoutSeven);
        userBirthDaysPicker = findViewById(R.id.userBirthDaysPicker);
        userBirthMonthPicker = findViewById(R.id.userBirthMonthPicker);
        userBirthYearPicker = findViewById(R.id.userBirthYearPicker);
        // userSignupBirthdaySelectLayoutSeven.setVisibility(View.VISIBLE);
        /** Load Layout Seven data*/
        loaddataInBirthdate();

        /**
         * layout Seven InitView
         * */
        userSignupImageSelectLayoutEight = findViewById(R.id.userSignupImageSelectLayoutEight);
        imageUploadbuttonLayout = findViewById(R.id.imageUploadbuttonLayout);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        profile_image = findViewById(R.id.profile_image);
        imageUploadbuttonLayout.setVisibility(View.VISIBLE);
        /** Layout Eignt Upload Image lisenner */
        uploadImageallLesenner();


/**
 * Next Button lesenner
 */
        sinUpNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter == 1) {

                    String username = signUpuserNameET.getText().toString().trim();
                    String email = signUpEamilET.getText().toString().trim();
                    String password = signUpPasswordET.getText().toString().trim();

                    if (cheakSignupValidationOne(username, email, password)) {

                        counter++;
                        userNameEmailPasswordLytOne.setVisibility(View.GONE);
                        userPhonALTPhoneSocialLInksLayoutTwo.setVisibility(View.VISIBLE);

                        userInformation.setName(username);
                        userInformation.setemail(email);
                        userInformation.setPassword(password);

                    }
                } else if (counter == 2) {

                    String phoneNo = signUpPhoneNumberET.getText().toString().trim();
                    String altPhoneNo = signUpAlternativePhoneNoET.getText().toString().trim();
                    String socialLinks = signUpSocialET.getText().toString().trim();

                    if (cheakSignUpValidationTwo(phoneNo, altPhoneNo, socialLinks)) {

                        counter++;
                        userNameEmailPasswordLytOne.setVisibility(View.GONE);
                        userPhonALTPhoneSocialLInksLayoutTwo.setVisibility(View.GONE);
                        userSignUpBloodGroupSelectLayoutThree.setVisibility(View.VISIBLE);

                        userInformation.setPhonNo(phoneNo);
                        userInformation.setAltPhoneNo(altPhoneNo);
                        userInformation.setSocialLink(socialLinks);

                    }

                } else if (counter == 3) {

                    if (bloodGroupName != null) {

                        counter++;
                        userNameEmailPasswordLytOne.setVisibility(View.GONE);
                        userPhonALTPhoneSocialLInksLayoutTwo.setVisibility(View.GONE);
                        userSignUpBloodGroupSelectLayoutThree.setVisibility(View.GONE);
                        userSignUpWeaightSelectLayoutFour.setVisibility(View.VISIBLE);
                        userInformation.setBloodGroup(bloodGroupName);

                    } else {

                        Toast.makeText(UserSignUpActivity.this, "Please Select Your Blood Group !", Toast.LENGTH_SHORT).show();
                    }

                } else if (counter == 4) {

                    if (selectWeightStatus) {

                        counter++;
                        userNameEmailPasswordLytOne.setVisibility(View.GONE);
                        userPhonALTPhoneSocialLInksLayoutTwo.setVisibility(View.GONE);
                        userSignUpBloodGroupSelectLayoutThree.setVisibility(View.GONE);
                        userSignUpWeaightSelectLayoutFour.setVisibility(View.GONE);
                        userSignUpGenderSelectLayoutFive.setVisibility(View.VISIBLE);
                        userInformation.setWeaight(weightStatus);
                    } else {

                        Toast.makeText(UserSignUpActivity.this, "Please Select Your Weaight !", Toast.LENGTH_SHORT).show();
                    }

                } else if (counter == 5) {

                    if (selectGenderStatus) {

                        counter++;
                        userNameEmailPasswordLytOne.setVisibility(View.GONE);
                        userPhonALTPhoneSocialLInksLayoutTwo.setVisibility(View.GONE);
                        userSignUpBloodGroupSelectLayoutThree.setVisibility(View.GONE);
                        userSignUpWeaightSelectLayoutFour.setVisibility(View.GONE);
                        userSignUpGenderSelectLayoutFive.setVisibility(View.GONE);
                        userSignupAddressLayoutSix.setVisibility(View.VISIBLE);
                        userInformation.setUserGender(genderStatus);
                    } else {
                        Toast.makeText(UserSignUpActivity.this, "Please Select Your Gender !", Toast.LENGTH_SHORT).show();

                    }
                } else if (counter == 6) {

                    String address = signUpStreetAddressET.getText().toString().trim();
                    String police_station = userSignUpPoliceStationET.getText().toString().trim();
                    String district = signUpDistrictET.getText().toString().trim();

                    if (cheakSignUpAddressValidation(address, police_station, district)) {
                        counter++;
                        userNameEmailPasswordLytOne.setVisibility(View.GONE);
                        userPhonALTPhoneSocialLInksLayoutTwo.setVisibility(View.GONE);
                        userSignUpBloodGroupSelectLayoutThree.setVisibility(View.GONE);
                        userSignUpWeaightSelectLayoutFour.setVisibility(View.GONE);
                        userSignUpGenderSelectLayoutFive.setVisibility(View.GONE);
                        userSignupAddressLayoutSix.setVisibility(View.GONE);
                        userSignupBirthdaySelectLayoutSeven.setVisibility(View.VISIBLE);

                        userInformation.setAddress(address);
                        userInformation.setPolice_station(police_station);
                        userInformation.setDistrict(district);

                    }
                } else if (counter == 7) {
                    if (cheakBirthdayValidation(birthDay, birthMonth, birthYear)) {
                        counter++;
                        Log.e(TAG, "counter " + counter);
                        userNameEmailPasswordLytOne.setVisibility(View.GONE);
                        userPhonALTPhoneSocialLInksLayoutTwo.setVisibility(View.GONE);
                        userSignUpBloodGroupSelectLayoutThree.setVisibility(View.GONE);
                        userSignUpWeaightSelectLayoutFour.setVisibility(View.GONE);
                        userSignUpGenderSelectLayoutFive.setVisibility(View.GONE);
                        userSignupAddressLayoutSix.setVisibility(View.GONE);
                        userSignupBirthdaySelectLayoutSeven.setVisibility(View.GONE);
                        userSignupImageSelectLayoutEight.setVisibility(View.VISIBLE);
                        sinUpNextButton.setText("Start Journey");

//                        DateFormat fmt = new SimpleDateFormat("MMMM dd, yyyy");
//                        Date d = fmt.parse("June 27, 2007");
                        userInformation.setBirthday(birthMonth + " " + birthDay + ", " + birthYear);
                    }

                } else if (counter == 8) {

                    if (url != null) {
                        /**
                         * Upload all Data in database
                         * Save data in database
                         * */
                        progressDialog();;
                        userInformation.setUserType("admin");
                        userInformation.setAdmin(true);
                        Calendar calendar = Calendar.getInstance();
                        Date date = new Date();
                        userInformation.setLast_donateDate(DateFormat.format("MMMM d, yyyy ", date.getTime()).toString());
                        userInformation.setReligion("");
                        getUserLoging(userInformation,url);

                    } else {
                        Toast.makeText(UserSignUpActivity.this, "Please Select A Beautiful Image !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Firebase user Loging and Save Data
     *
     * @param informationData
     * @param url
     */
    private void getUserLoging(UserInformation informationData, Uri url) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(informationData.getemail(), informationData.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    session.saveLogingSessionData(true, FirebaseAuth.getInstance().getCurrentUser().getUid());
                    informationData.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    /** save General UserData*/
                    saveImageIntoStorage(informationData,url);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error Loging :" + e.getMessage());
                progress.dismiss();
            }
        });

    }

    /**
     * Save Image File from Storage
     */
    private String saveImageIntoStorage(UserInformation userInformation,Uri path) {
        if (path != null) {
            mstorageReference = FirebaseStorage.getInstance().getReference("userFrofileimage");
            final StorageReference imageRefarence = mstorageReference.child(System.currentTimeMillis() + "." + getFileExtension(path));

            storageTask = imageRefarence.putFile(path)
                    .addOnSuccessListener(this, taskSnapshot -> {
                        imageRefarence.getDownloadUrl().addOnSuccessListener(uri -> {

                            Log.e(TAG, "Firebase Storage Url " + uri.toString());

                            downloadImageUrl = uri.toString();
                            userInformation.setUserProfilePicture(downloadImageUrl);
                            saveUserSignUpData(userInformation);
                        });

                    })
                    .addOnFailureListener(this, e -> {
                        // Toast.makeText(AddHomeCatagory.this, "Error: "+e, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "erro:" + e.getMessage());
                        progress.dismiss();

                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getTask().isSuccessful()) {
                                Log.e(TAG, "error task :" + taskSnapshot.toString());
                            }
                        }
                    });


        }
        return downloadImageUrl;
    }
    //get Making Image Url
    private String getFileExtension(Uri uri) {
        ContentResolver cR = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        Log.e(TAG, "Image Extention : " + mime.getExtensionFromMimeType(cR.getType(uri)));
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    /**
     * save General UserData in Firebase RealTime DataBase
     */
    private void saveUserSignUpData(UserInformation informationData) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        rootRer = FirebaseDatabase.getInstance().getReference();
        generalUserRef = rootRer.child("generalUserTable");

        if (user != null) {
            generalUserRef.child(informationData.getUserId()).setValue(informationData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progress.dismiss();
                    startActivity(new Intent(UserSignUpActivity.this, MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toasty.error(UserSignUpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error: " + e.getMessage());
                    progress.dismiss();
                }
            });
        }

    }

    /**
     * Encode Image To Based64 String
     */
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        //encode image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.circle_book);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.e(TAG, "" + imageString);
        //getDecoded64StringFromBitmapImage(imageString);
        return imageString;
    }


    /**
     * Decode Based64 String TO Image
     */
    public Bitmap getDecoded64StringFromBitmapImage(String imageString) {
        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        profile_image.setImageBitmap(decodedImage);
        return decodedImage;
    }


    /**
     * Cheak Birthday Date is Select Or not.
     */
    private boolean cheakBirthdayValidation(String birthDay, String birthMonth, String birthYear) {
        if (birthDay == null) {
            Toasty.warning(UserSignUpActivity.this, "Select Birth Days !", Toast.LENGTH_SHORT).show();
            return false;
        } else if (birthMonth == null) {
            Toasty.warning(UserSignUpActivity.this, "Select Birth Month !", Toast.LENGTH_SHORT).show();
            return false;
        } else if (birthYear == null) {
            Toasty.warning(UserSignUpActivity.this, "Select Birth year !", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    /**
     * Layout Eignt Lesenner
     */
    private void uploadImageallLesenner() {
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(UserSignUpActivity.this);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                url = result.getUri();
                Log.e(TAG, "Selected Image Url = =" + result.getUri());
                imageUploadbuttonLayout.setVisibility(View.GONE);
                profile_image.setVisibility(View.VISIBLE);
                Picasso.get().load(result.getUri()).into(profile_image);

            }
        }
    }

    /**
     * Layout Seven Lisenner set and set Birthday Number Picker Data
     */
    private void loaddataInBirthdate() {
        Calendar cal = Calendar.getInstance();
        userBirthDaysPicker.setMinValue(1);
        userBirthDaysPicker.setMaxValue(31);

        String[] monthList = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        userBirthMonthPicker.setMinValue(1);
        userBirthMonthPicker.setMaxValue(monthList.length - 1);
        userBirthMonthPicker.setMinValue(0);
        userBirthMonthPicker.setWrapSelectorWheel(true);
        userBirthMonthPicker.setDisplayedValues(monthList);

        userBirthYearPicker.setMinValue(1970);
        userBirthYearPicker.setMaxValue(cal.get(Calendar.YEAR));

        userBirthDaysPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                birthDay = String.valueOf(newVal);
            }
        });

        userBirthMonthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                birthMonth = String.valueOf(monthList[newVal]);
            }
        });
        userBirthYearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                birthYear = String.valueOf(newVal);
            }
        });


    }


    /**
     * cheak Weight Button Click or not  and init Lisenner
     */
    private void genderButtonClickLisenner() {

        isMaleLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderStatus = "Male";
                selectGenderStatus = true;
                isMaleLyt.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.select_ractangle_shape));
                isFemaleLyt.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.unselect_ractangle_shape));
            }
        });
        isFemaleLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderStatus = "Female";
                selectGenderStatus = true;
                isMaleLyt.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.unselect_ractangle_shape));
                isFemaleLyt.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.select_ractangle_shape));
            }
        });

    }

    /**
     * cheak Weight Button Click or not  and init Lisenner
     */
    private void isWeaightPerfectOrNotCheak() {

        weaightPerfectLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weightStatus = true;
                selectWeightStatus = true;
                weaightPerfectLyt.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.select_ractangle_shape));
                weaightNotPerfectLyt.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.unselect_ractangle_shape));
            }
        });
        weaightNotPerfectLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weightStatus = false;
                selectWeightStatus = true;
                weaightPerfectLyt.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.unselect_ractangle_shape));
                weaightNotPerfectLyt.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.select_ractangle_shape));
            }
        });

    }

    /**
     * Set Layout Three BloodGroup Data
     */
    private void margeBloodGroupToggleButton() {
        List<String> bloodNames = new ArrayList<>();
        bloodNames.add("A+");
        bloodNames.add("A-");
        bloodNames.add("B+");
        bloodNames.add("B-");
        bloodNames.add("O+");
        bloodNames.add("O-");
        bloodNames.add("AB+");
        bloodNames.add("AB-");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        bloodGroupRV.setLayoutManager(gridLayoutManager);
        BloodGroupSelectedAdapter adapter = new BloodGroupSelectedAdapter(this, bloodNames);
        adapter.setHasStableIds(true);
        bloodGroupRV.setAdapter(adapter);


    }

    /**
     * Cheak loging Address Validation
     */
    private boolean cheakSignUpAddressValidation(String streetAddress, String polisce_sation, String district) {

        boolean valid = true;

        signUpStreetAddressET.setError(null);
        userSignUpPoliceStationET.setError(null);
        signUpDistrictET.setError(null);

        if (streetAddress == null || streetAddress.isEmpty()) {
            signUpStreetAddressET.setError("Address field is Empty !");
            valid = false;

        } else if (polisce_sation == null || polisce_sation.isEmpty()) {
            userSignUpPoliceStationET.setError("PoliceStation Field Is Empty !");
            valid = false;
        } else if (district == null || district.isEmpty()) {
            userSignUpPoliceStationET.setError("District Field Is Empty !");
            valid = false;
        }

        return valid;
    }

    /**
     * Cheak loging Validation Phone No,Alternative Phone No, Social Links
     */
    private boolean cheakSignUpValidationTwo(String phoneNo, String altPhoneNo, String socialLinks) {

        boolean valid = true;

        signUpPhoneNumberET.setError(null);
        signUpAlternativePhoneNoET.setError(null);

        if (phoneNo.length() < 11 || phoneNo.length() > 12 || !android.util.Patterns.PHONE.matcher("+88" + phoneNo).matches()) {
            signUpPhoneNumberET.setError("Invalid Phone Number");
            valid = false;

        } else if (altPhoneNo.length() < 11 || altPhoneNo.length() > 12 || !android.util.Patterns.PHONE.matcher("+88" + phoneNo).matches()) {
            signUpAlternativePhoneNoET.setError("Invalid  Phone Number");
            valid = false;
        }

        return valid;
    }

    /**
     * Cheak loging Validation UserName,Email PassWord
     */
    private boolean cheakSignupValidationOne(String username, String email, String password) {

        boolean valid = true;

        signUpuserNameET.setError(null);
        signUpEamilET.setError(null);
        signUpPasswordET.setError(null);

        if (username.isEmpty()) {

            signUpuserNameET.setError(" UserName field is Empty  !");
            valid = false;

        } else if (email.isEmpty()) {

            signUpEamilET.setError("Email field is empty  !");
            signUpuserNameET.requestFocus();
            valid = false;

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            signUpEamilET.setError(" InValid Email Address !");
            signUpuserNameET.requestFocus();
            valid = false;

        } else if (password.isEmpty()) {

            signUpPasswordET.setError("Password field is empty !");
            signUpuserNameET.requestFocus();
            signUpEamilET.requestFocus();
            valid = false;

        } else if (password.length() < 6) {

            signUpuserNameET.requestFocus();
            signUpEamilET.requestFocus();
            signUpPasswordET.setError("Please ! Enter At last 6 charecter !");
            valid = false;
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    /**
     * Load ProgressDialog
     * */
    public void progressDialog() {
        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait a Several Time.....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    public void seLectedBloodGroup(String groupName) {
        this.bloodGroupName = groupName;
    }
}