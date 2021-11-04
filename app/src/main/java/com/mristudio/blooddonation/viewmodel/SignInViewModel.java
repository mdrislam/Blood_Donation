package com.mristudio.blooddonation.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mristudio.blooddonation.model.AdminDataModel;
import com.mristudio.blooddonation.repository.SignInRepository;

import java.util.List;

public class SignInViewModel extends AndroidViewModel {

    public SignInRepository repository;
    public LiveData<Boolean> signInWithEmailPassLiveData;
    public LiveData<Boolean> cheakUserTypedLiveData;


    public SignInViewModel(@NonNull Application application) {
        super(application);
        repository = new SignInRepository(application);
    }

    public void getSignInWithEmailPass(String email, String pass) {
        signInWithEmailPassLiveData = repository.signInWithEmailPassAuthentication(email, pass);
    }

    public void getUserTypedCheakInFirebase(String email, String pass) {
        cheakUserTypedLiveData = repository.getUserTypedCheakInFirebase(email, pass);

    }
}
