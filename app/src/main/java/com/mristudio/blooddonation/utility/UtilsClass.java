package com.mristudio.blooddonation.utility;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mristudio.blooddonation.model.RequestModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class UtilsClass {

    private static String strViews = "0";
    private static String lovesCounter= "0";

    public static void getShareFromFaceBook(RequestModel requestModel) {

    }

    public static List<String> getAllLikes(String tblName) {
        List<String> stringList = new ArrayList<>();
        stringList.add(0, "0");
        stringList.add(1, "false");

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postRef = rootRef.child("USER_PUBLIC_POST").child(tblName).child("LOVES");
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot != null) {
                    stringList.add(0, String.valueOf(snapshot.getChildrenCount()));
                    Log.e(TAG,"Childen "+snapshot.getChildrenCount());
                    for (DataSnapshot snps : snapshot.getChildren()) {
                        if (tblName.equals(snps)) {
                            stringList.add(1, "true");
                        }
                    }

                }else {
                    stringList.add(0, "0");
                    stringList.add(1, "false");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                stringList.add(0, "0");
                stringList.add(1, "false");

            }
        });

        return stringList;
    }

    /**
     * Get all View By TblID
     */
    public static String getAllViews(String tblName) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postRef = rootRef.child("USER_PUBLIC_POST").child(tblName).child("VIEWS");


        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot != null) {
                    strViews = String.valueOf(snapshot.getChildrenCount());
                    Log.e(TAG, "getAllViews: "+tblName );
                    Log.e(TAG, "onDataChange View: "+snapshot.getChildrenCount());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e(TAG, "onCancelled: "+error.getMessage() );
            }
        });

        return strViews;
    }

    public static String updateUserLoves(String tblName, String loversUID) {

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postRef = rootRef.child("USER_PUBLIC_POST").child(tblName).child("LOVES");

        postRef.child(loversUID).setValue(loversUID).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                lovesCounter=getAllLikes(tblName).get(0);
               Log.e(TAG, "onSuccess: Update Loves: "+lovesCounter );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                lovesCounter = "0";
                Log.e(TAG, "onFailure: Update Loves: "+e.getMessage() );
            }
        });
        return lovesCounter;
    }
    public static String removesUserLoves(String tblName, String loversUID) {


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postRef = rootRef.child("USER_PUBLIC_POST").child(tblName).child("LOVES");
        postRef.child(loversUID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                lovesCounter=getAllLikes(tblName).get(0);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        return lovesCounter;
    }
    public static String updateTotalViews(String tblName, String loversUID) {

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postRef = rootRef.child("USER_PUBLIC_POST").child(tblName).child("VIEWS");
        
        postRef.child(loversUID).setValue(loversUID).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               strViews= getAllViews(tblName);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: Update Views: "+e.getMessage() );
            }
        });
        return strViews;
    }
}
