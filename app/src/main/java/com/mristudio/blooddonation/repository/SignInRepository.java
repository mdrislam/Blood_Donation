package com.mristudio.blooddonation.repository;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mristudio.blooddonation.model.AdminDataModel;
import com.mristudio.blooddonation.view.activity.MainActivity;
import com.mristudio.blooddonation.view.activity.UserSignInActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SignInRepository {
    public Application application;
    public FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public DatabaseReference rootRer;
    public DatabaseReference adminRef;

    public SignInRepository(Application application) {
        this.application = application;
    }

    // SignIn Authentication with Email and Password
    public MutableLiveData<Boolean> signInWithEmailPassAuthentication(String email, String pass) {

        MutableLiveData<Boolean> signInLiveData = new MutableLiveData<>();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                if (task.isSuccessful()) {
                    // Get new FCM registration token
                    final String token = task.getResult();

                    firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                Map<String, Object> tokenMap = new HashMap<>();
                                tokenMap.put("token", token);

                                FirebaseDatabase.getInstance().getReference()
                                        .child("generalUserTable")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .updateChildren(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                        if (task.isSuccessful()) {

                                            signInLiveData.setValue(true);
                                            Log.e(TAG, "onComplete: Sucessfully Updated Token " + task.isSuccessful());

                                        } else {

                                            signInLiveData.setValue(false);
                                            Log.e(TAG, "onComplete:  not Update Token" + task.isSuccessful());
                                            return;
                                        }
                                    }
                                });


                            }else {

                                signInLiveData.setValue(false);
                                Log.e(TAG, "onComplete: Auth is not Sucessfully" );
                                return;
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            signInLiveData.setValue(false);
                            Log.e(TAG, "onFailure: Auth Error: "+e.getMessage() );

                        }
                    });
                } else {
                    signInLiveData.setValue(false);
                    Log.e(TAG, "onComplete: Generate Token is False !");
                    return;
                }
            }
        });


        return signInLiveData;
    }

    //Get All Data From Admin Data Table In Firebase
    public MutableLiveData<Boolean> getUserTypedCheakInFirebase(String email, String pass) {

        MutableLiveData<Boolean> userTypedChaek = new MutableLiveData<>();
        rootRer = FirebaseDatabase.getInstance().getReference();
        adminRef = rootRer.child("userAdminTable");


        adminRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    AdminDataModel data = ds.getValue(AdminDataModel.class);
                    if (data.getEmail().equals(email) && data.getPassword().equals(pass)) {
                        userTypedChaek.setValue(true);
                        return;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                userTypedChaek.setValue(false);
            }
        });
        return userTypedChaek;
    }
}
