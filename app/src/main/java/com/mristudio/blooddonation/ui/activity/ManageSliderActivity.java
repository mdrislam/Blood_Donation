package com.mristudio.blooddonation.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.adapter.SliderUpdateAdapter;
import com.mristudio.blooddonation.model.ImageSliderData;
import com.mristudio.blooddonation.utility.Session;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class ManageSliderActivity extends AppCompatActivity implements SliderUpdateAdapter.SliderImgesClick {

    private static final String TAG = "ManageSliderActivity";
    List<ImageSliderData> sliderDataList = new ArrayList<>();
    private Toolbar toolbar;
    private ImageButton backButton;
    private Button updateSaveButton, selectButton;
    private ImageView sliderImageView;
    private EditText sliderHeadingET;
    private TextView campaignDateTV;
    private ImageButton selectDateIB;
    private RecyclerView sliderRechyclerView;
    private Uri uri = null;
    private Bitmap bitmap;
    private Boolean buttonStatus = false;
    private Session session;
    private ProgressDialog progress;

    private static StorageReference mstorageReference;
    private static StorageTask storageTask;
    private DatabaseReference rootRer;
    private boolean updateImageButtonClick = false;
    private boolean deleteStatus = false;
    private DatabaseReference sliderImageRef;
    private String downloadImageUrl;
    private static ImageSliderData sliderData = new ImageSliderData();
    private GridLayoutManager gridLayoutManager;
    private SliderUpdateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_slider);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new Session(this);
        initView();
        loadSliderimages();
    }


    /**
     * Initialize View and Set All Lesenner
     */
    private void initView() {
        rootRer = FirebaseDatabase.getInstance().getReference();
        sliderImageRef = rootRer.child("userSliderImages");

        TextView tittle = toolbar.findViewById(R.id.adminHomePageTittle);
        backButton = toolbar.findViewById(R.id.adminToolBarBack);
        tittle.setText("Manage Sleder");
        updateSaveButton = findViewById(R.id.updateSaveButton);
        selectButton = findViewById(R.id.selectButton);
        sliderImageView = findViewById(R.id.sliderImageView);
        sliderHeadingET = findViewById(R.id.sliderHeadingET);
        campaignDateTV = findViewById(R.id.campaignDateTV);
        selectDateIB = findViewById(R.id.selectDateIB);
        clearAll();

        sliderRechyclerView = findViewById(R.id.sliderRechyclerView);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(ManageSliderActivity.this);
            }
        });
        selectDateIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });

        updateSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSliderData imageSliderData = new ImageSliderData();
                String headingText = sliderHeadingET.getText().toString().trim();
                String createBy = "create by " + session.getUserType();
                String createAt = campaignDateTV.getText().toString();
                imageSliderData.setHeadline(headingText);
                imageSliderData.setCreateBy(createBy);
                imageSliderData.setCreateAt(createAt);
                if (buttonStatus) {

                    /**
                     * when user Click Select Image button And Select image*/
                    if (updateImageButtonClick) {
                        if (cheakuserInputValidation(headingText, createAt, uri)) {

                            progressDialog();
                            deleteexistingImage(sliderData.getImgUrl(), imageSliderData);
                        }

                    } else {
                        progressDialog();
                        imageSliderData.setImgUrl(sliderData.getImgUrl());
                        updateSliderData(imageSliderData);
                        Log.e(TAG, "button Update");
                    }

                } else {

                    if (cheakuserInputValidation(headingText, createAt, uri)) {
                        progressDialog();
                        saveImageIntoStorage(uri, imageSliderData);
                        Log.e(TAG, "Save Method Called");
                    }
                }

            }
        });

        /**
         * navigation Up Button Lesenner
         * */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    /**
     * Update Slider Images Data
     */
    private void updateSliderData(ImageSliderData imageSliderData) {
        if (imageSliderData != null) {
            imageSliderData.setTablesId(sliderData.getTablesId());

            sliderImageRef.child(sliderData.getTablesId()).setValue(imageSliderData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toasty.success(ManageSliderActivity.this, "Sucessfully Updated Slider Imaage !", Toast.LENGTH_SHORT).show();
                    clearAll();
                    loadSliderimages();
                    progress.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toasty.error(ManageSliderActivity.this, "Something is wrong try again !", Toast.LENGTH_SHORT).show();
                    clearAll();
                    progress.dismiss();
                }
            });

        } else {
            Toasty.error(ManageSliderActivity.this, "Something is wrong try again !", Toast.LENGTH_SHORT).show();
            clearAll();
            progress.dismiss();
        }

    }

    /**
     * save Slider Images Data
     */
    private void saveSliderImagesData(ImageSliderData imageSliderData) {
        String tableId = sliderImageRef.push().getKey();
        imageSliderData.setTablesId(tableId);

        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {

            sliderImageRef.child(tableId).setValue(imageSliderData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toasty.success(ManageSliderActivity.this, "Sucessfully Save Slider Imaage !", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                    clearAll();
                    loadSliderimages();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toasty.error(ManageSliderActivity.this, "Something is wrong try again !", Toast.LENGTH_SHORT).show();
                    progress.dismiss();


                }
            });
        } else {
            startActivity(new Intent(ManageSliderActivity.this, UserSignInActivity.class));
        }

    }
    /**
     * Cheak User Validation
     */
    private boolean cheakuserInputValidation(String headingText, String createAt, Uri uri) {


        Log.e(TAG, "Txt");
        sliderHeadingET.setError(null);
        if (uri == null) {
            Toasty.error(ManageSliderActivity.this, "Please Select Slider Image ", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Txt");
            return false;
        } else if (headingText.isEmpty()) {
            sliderHeadingET.setError("Empty Field not Allowed !");
            return false;
        } else if (createAt == null) {
            Toasty.error(ManageSliderActivity.this, "Please Select Campaign Date ! ", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    /**
     * Cheak User Validation
     */
    private boolean cheakuserInputUpdateValidation(String headingText, String createAt, String uri) {

        Log.e(TAG, "Txt");
        sliderHeadingET.setError(null);
        if (uri == null) {
            Toasty.error(ManageSliderActivity.this, "Please Select Slider Image ", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Txt");
            return false;
        } else if (headingText.isEmpty()) {
            sliderHeadingET.setError("Empty Field not Allowed !");
            return false;
        } else if (createAt == null) {
            Toasty.error(ManageSliderActivity.this, "Please Select Campaign Date ! ", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    /**
     * Open Date Picker When User Select Button and set text in TextView
     */
    public void openDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM, yyyy", Locale.US);
        DatePickerDialog datePickerDialog = new DatePickerDialog(ManageSliderActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                System.out.print(dateFormatter.format(newDate.getTime()));
                campaignDateTV.setText(dateFormatter.format(newDate.getTime()));
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                uri = result.getUri();
                updateImageButtonClick = true;

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "error: " + e.getMessage());
                }
                Log.e(TAG, "Selected Image Url = =" + result.getUri());

                Picasso.get().load(result.getUri()).into(sliderImageView);

            }
        }
    }

    private void loadSliderimages() {
        sliderDataList.clear();
        gridLayoutManager = new GridLayoutManager(this, 1);
        sliderRechyclerView.setLayoutManager(gridLayoutManager);
        adapter = new SliderUpdateAdapter(this, sliderDataList);
        adapter.setHasStableIds(true);

        sliderImageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ImageSliderData imageSliderData = ds.getValue(ImageSliderData.class);
                    sliderDataList.add(imageSliderData);
                    Log.e(TAG, "Data: " + ds.getValue(ImageSliderData.class).getCreateBy());
                }
                sliderRechyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Log.e(TAG, "Size: " + sliderDataList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error: " + error.getMessage());
            }
        });


    }

    /**
     * Save Image File from Storage
     */
    private String saveImageIntoStorage(Uri path, ImageSliderData imageSliderData) {
        if (path != null) {
            mstorageReference = FirebaseStorage.getInstance().getReference("userSlidersImages");
            final StorageReference imageRefarence = mstorageReference.child(System.currentTimeMillis() + "." + getFileExtension(path));

            storageTask = imageRefarence.putFile(path)
                    .addOnSuccessListener(this, taskSnapshot -> {
                        imageRefarence.getDownloadUrl().addOnSuccessListener(uri -> {

                            Log.e(TAG, "Firebase Storage Url " + uri.toString());

                            downloadImageUrl = uri.toString();
                            imageSliderData.setImgUrl(downloadImageUrl);
                            if (buttonStatus) {
                                updateSliderData(imageSliderData);
                            } else {
                                saveSliderImagesData(imageSliderData);
                            }
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

    //delete existing Image
    private void deleteexistingImage(String imageurl, ImageSliderData imageSliderData) {

        FirebaseStorage imageReference = FirebaseStorage.getInstance();
        StorageReference imageRef = imageReference.getReferenceFromUrl(imageurl);
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (deleteStatus) {
                    deleteDataFromDatabase(imageSliderData);
                } else {
                    saveImageIntoStorage(uri, imageSliderData);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "" + e.getMessage());
                if (deleteStatus)
                progress.dismiss();
            }
        });
    }

    //Delete Data from Firebase RealTime Database
    private void deleteDataFromDatabase(ImageSliderData imageSliderData) {
        sliderImageRef.child(imageSliderData.getTablesId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toasty.success(ManageSliderActivity.this, "Sucessfully Deleted Slider Imaage !", Toast.LENGTH_SHORT).show();
                clearAll();
                loadSliderimages();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(ManageSliderActivity.this, "Something is wrong try again !", Toast.LENGTH_SHORT).show();
                loadSliderimages();
            }
        });
    }

    //get Making Image Url
    private String getFileExtension(Uri uri) {
        ContentResolver cR = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        Log.e(TAG, "Image Extention : " + mime.getExtensionFromMimeType(cR.getType(uri)));
        return mime.getExtensionFromMimeType(cR.getType(uri));
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
    public Bitmap etDecoded64StringFromBitmapImage(String imageString) {
        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        sliderImageView.setImageBitmap(decodedImage);
        return decodedImage;
    }

    private void clearAll() {
        campaignDateTV.setText(" ");
        sliderHeadingET.setText("");
        sliderImageView.setImageDrawable(getResources().getDrawable(R.drawable.unnamed));
        uri = null;
        bitmap = null;
        buttonStatus = false;
        updateSaveButton.setText("Save");
        sliderData = null;
        updateImageButtonClick = false;
        deleteStatus = false;

    }

    /**
     * Load ProgressDialog
     */
    public void progressDialog() {
        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait a Several Time.....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    public void clickSliderImages(ImageSliderData imageSliderData) {
        this.sliderData = imageSliderData;
        campaignDateTV.setText(imageSliderData.getCreateAt());
        sliderHeadingET.setText(imageSliderData.getHeadline());
        Picasso.get().load(imageSliderData.getImgUrl()).into(sliderImageView);
        updateSaveButton.setText("Update");
        buttonStatus = true;
        downloadImageUrl = null;
    }

    @Override
    public void clickSliderImagesDelete(ImageSliderData imageSliderData) {
        progressDialog();
        deleteexistingImage(imageSliderData.getImgUrl(), imageSliderData);
        progress.dismiss();
    }
}