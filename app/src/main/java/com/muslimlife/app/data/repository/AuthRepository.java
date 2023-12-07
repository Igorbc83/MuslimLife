package com.muslimlife.app.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import com.muslimlife.app.data.network.MuslimApi;

public class AuthRepository {

    private final MuslimApi api;
    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<String> errorLiveData;


    public AuthRepository(MuslimApi api) {
        this.api = api;
        firebaseAuth = FirebaseAuth.getInstance();

        userLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
        }
    }

    public void verifyCode(String verificationId, String code) {
        PhoneAuthCredential authCredential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(authCredential);
    }

    public void signInWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && task.getResult().getUser() != null)
                            userLiveData.postValue(task.getResult().getUser());
                    } else {
                        if (task.getException() != null && task.getException().getLocalizedMessage() != null)
                            errorLiveData.postValue(task.getException().getLocalizedMessage());
                    }
                });
    }

    public void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && task.getResult().getUser() != null)
                            userLiveData.postValue(task.getResult().getUser());
                        Log.e("AuthRepository", "signInWithCredential:success" + task.getResult().getUser());
                    } else {
                        if (task.getException() != null && task.getException().getLocalizedMessage() != null) {
                            Log.e("AuthRepository", "signInWithCredential:error" + task.getException().getLocalizedMessage());
                            errorLiveData.postValue(task.getException().getLocalizedMessage());
                        }
                    }
                });
    }


    public void skipAuth() {
        firebaseAuth.signInAnonymously().addOnSuccessListener(authResult -> userLiveData.postValue(authResult.getUser()));
    }

    public void logOut() {
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
