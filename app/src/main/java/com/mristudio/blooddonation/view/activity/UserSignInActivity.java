package com.mristudio.blooddonation.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.AdminDataModel;
import com.mristudio.blooddonation.utility.Session;
import com.mristudio.blooddonation.utility.UtilMethod;
import com.mristudio.blooddonation.viewmodel.SignInViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class UserSignInActivity extends AppCompatActivity {
    private static final String TAG = "Login";
    private ArrayList<AdminDataModel> allAdminList = new ArrayList<>();
    private EditText signInEamiOruserNameET, signInPasswordET;
    private Button sinIpButton;
    private TextView forgotPasswordTxtButton, signUpTxtButton;
    private Session session;
    private ProgressDialog progress;

    private DatabaseReference rootRer;
    private DatabaseReference adminRef;
    private UtilMethod utilMethod;

    private SignInViewModel signInViewModel;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_in);
        utilMethod = new UtilMethod(this);
        initView();

    }

    /**
     * Initialize UserSignIn View and set all Lesenner
     */
    private void initView() {
        session = new Session(this);
        signInEamiOruserNameET = findViewById(R.id.signInEamiOruserNameET);
        signInPasswordET = findViewById(R.id.signInPasswordET);
        sinIpButton = findViewById(R.id.sinIpButton);
        forgotPasswordTxtButton = findViewById(R.id.forgotPasswordTxtButton);
        signUpTxtButton = findViewById(R.id.signUpTxtButton);

        //signInViewModel = ViewModelProviders.of(this).get(SignInViewModel.class);
        signInViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SignInViewModel.class);


        /**
         * Set Signin Button  Lesenner
         * */
        sinIpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameEmail = signInEamiOruserNameET.getText().toString().trim();
                String password = signInPasswordET.getText().toString().trim();
                if (utilMethod.isOnline()) {

                    if (cheakValidation(userNameEmail, password)) {
                        signInWithEmailPassAuthentication(userNameEmail, password);
                    }

                } else {
                    Toast.makeText(UserSignInActivity.this, "Please Cheak your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /**
         * Set SignUp TextButton  Lesenner
         * */
        signUpTxtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserSignInActivity.this, UserSignUpActivity.class));
            }
        });
    }

    private void signInWithEmailPassAuthentication(final String email, final String password) {
        progressDialog();

        signInViewModel.getSignInWithEmailPass(email, password);
        signInViewModel.signInWithEmailPassLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean status) {


                if (status) {

                    progress.dismiss();
                    startActivity(new Intent(UserSignInActivity.this, MainActivity.class));
                    finish();

                } else {

                    progress.dismiss();
                    Toasty.error(UserSignInActivity.this, " Incorrect Email or Password !", Toast.LENGTH_SHORT, true).show();

                }
            }
        });

    }


    /**
     * Open Aler Dialog for admin to choice admin go to admin panel or not
     */
//    private void openAlertDialogForAdmin() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserSignInActivity.this);
//        alertDialog.setTitle("Admin Alert Message");
//        alertDialog.setMessage("Are You sure Go to Admin Panel ? ");
//
//        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                startActivity(new Intent(UserSignInActivity.this, AdminActivity.class));
//                finish();
//            }
//        });
//        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(UserSignInActivity.this, MainActivity.class));
//                finish();
//            }
//        });
//        AlertDialog dialog = alertDialog.create();
//        dialog.show();
//
//    }

    /**
     * Cheak loging Validation
     */
    private boolean cheakValidation(String emailorUsername, String password) {

        boolean valid = true;

        signInEamiOruserNameET.setError(null);
        signInPasswordET.setError(null);

        if (emailorUsername.isEmpty()) {

            signInEamiOruserNameET.setError("Invalid Email Or Username !");
            valid = false;

        } else if (password.isEmpty()) {

            signInPasswordET.setError("Password field is empty !");
            signInEamiOruserNameET.requestFocus();
            valid = false;

        } else if (password.length() < 6) {

            signInEamiOruserNameET.requestFocus();
            signInPasswordET.setError("Please ! Enter At last 6 charecter !");
            valid = false;
        }

        return valid;
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
}