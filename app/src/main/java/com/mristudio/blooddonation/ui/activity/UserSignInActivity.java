package com.mristudio.blooddonation.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.mristudio.blooddonation.R;
import com.mristudio.blooddonation.model.AdminDataModel;
import com.mristudio.blooddonation.utility.Session;

import java.util.ArrayList;

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

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_in);
        initView();
        loadAdminData();
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

        /**
         * Set Signin Button  Lesenner
         * */
        sinIpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameEmail = signInEamiOruserNameET.getText().toString().trim();
                String password = signInPasswordET.getText().toString().trim();
                if (isOnline()) {

                    if (cheakValidation(userNameEmail, password)) {
                        loginWithEmailPassword(userNameEmail, password);
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

    /**
     * getuser Loging By Username and user Password
     */

    private void loginWithEmailPassword(final String email, final String password) {
        progressDialog();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    /**
                     * Cheak UserData is Here in ta table
                     * **/
                    if (isAdminOrNot(email, password, allAdminList)) {
                        openAlertDialogForAdmin();

                    } else {
                        session.saveLogingSessionData(true, FirebaseAuth.getInstance().getCurrentUser().getUid());
                        startActivity(new Intent(UserSignInActivity.this, MainActivity.class));
                        finish();
                    }

                    progress.dismiss();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(UserSignInActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT, true).show();

                progress.dismiss();
            }
        });

    }


    /**
     * Open Aler Dialog for admin to choice admin go to admin panel or not
     */
    private void openAlertDialogForAdmin() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserSignInActivity.this);
        alertDialog.setTitle("Admin Alert Message");
        alertDialog.setMessage("Are You sure Go to Admin Panel ? ");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(UserSignInActivity.this, AdminActivity.class));
                finish();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(UserSignInActivity.this, MainActivity.class));
                finish();
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.show();

    }

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
     * Load all Admin  Data
     **/
    private void loadAdminData() {
        rootRer = FirebaseDatabase.getInstance().getReference();
        adminRef = rootRer.child("userAdminTable");
        allAdminList.clear();
        adminRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    AdminDataModel data = ds.getValue(AdminDataModel.class);
                    allAdminList.add(data);
                    Log.e(TAG, "admin data Size " + allAdminList.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Exe" + databaseError.getMessage());
            }
        });

    }

    /**
     * cheak data form List Logged User Is Admin Or Not.
     */
    public Boolean isAdminOrNot(
            String email, String password, ArrayList<AdminDataModel> data) {
        if (data != null) {
            for (AdminDataModel admin : data) {
                Log.e(TAG, "status = " + admin.getName());
                /**
                 * when Match data form List then Save Session
                 * */
                if (admin.getEmail().equals(email) && admin.getPassword().equals(password)) {

                    /**
                     *  Save Session
                     * */
                    session.saveLogingSession(true, FirebaseAuth.getInstance().getCurrentUser().getUid(), admin.getUserType(),email);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Cheak Data Connection is ON or OFF
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
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