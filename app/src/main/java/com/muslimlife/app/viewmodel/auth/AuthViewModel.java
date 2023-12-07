package com.muslimlife.app.viewmodel.auth;

import static com.muslimlife.app.utils.Constants.IS_NOTIFICATION_RECEIVE;
import static com.muslimlife.app.utils.Constants.IS_USER;
import static com.muslimlife.app.utils.Constants.SELECTED_PUSH;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;

import java.io.EOFException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import com.muslimlife.app.app.SchedulersFacade;
import com.muslimlife.app.data.model.UserLocation;
import com.muslimlife.app.data.model.UserProfile;
import com.muslimlife.app.data.model.parameters.CheckUserParameters;
import com.muslimlife.app.data.model.parameters.RegistrationParameters;
import com.muslimlife.app.data.repository.AuthRepository;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.utils.Constants;

public class AuthViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable;
    private final SchedulersFacade schedulersFacade;

    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<UserProfile> userProfileLiveData;
    private final MutableLiveData<String> idLiveData;
    private final MutableLiveData<String> errorLiveData;

    private final MutableLiveData<List<UserLocation>> countriesLiveData;
    private final MutableLiveData<List<UserLocation>> citiesLiveData;

    private UserProfile userProfile;

    @Inject
    public AuthViewModel(AuthRepository authRepository, UserRepository userRepository, SchedulersFacade schedulersFacade) {
        this.schedulersFacade = schedulersFacade;
        this.compositeDisposable = new CompositeDisposable();

        this.authRepository = authRepository;
        this.userRepository = userRepository;

        // init new liveData
        this.userProfileLiveData = new MutableLiveData<>();
        this.countriesLiveData = new MutableLiveData<>();
        this.citiesLiveData = new MutableLiveData<>();
        this.idLiveData = new MutableLiveData<>();

        this.userLiveData = authRepository.getUserLiveData();
        this.errorLiveData = authRepository.getErrorLiveData();


    }

    public void verifySmsCode(String verificationId, String code) {
        authRepository.verifyCode(verificationId, code);
    }

    public void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {
        authRepository.signInWithCredential(phoneAuthCredential);
    }

    public void checkUserInApi(String email, Activity activity) {
        compositeDisposable.add(
                userRepository.checkUserInApi(new CheckUserParameters(email))
                        .subscribeOn(schedulersFacade.io())
                        .observeOn(schedulersFacade.ui())
                        .subscribe(checkUserResponse -> {
                            if (checkUserResponse.getEmail()!=null) {
                                userProfile = new UserProfile();
                                userProfile.setId(checkUserResponse.getId());
                                userProfile.setEmail(checkUserResponse.getEmail());

                                SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
                                sharedPref.edit().putString(Constants.SP_KEY_USER_ID, userProfile.getId()).apply();
                                sharedPref.edit().putString(Constants.SP_KEY_USER_EMAIL,checkUserResponse.getEmail()).apply();
                                userProfileLiveData.postValue(userProfile);
                                if (checkUserResponse.getIs_user() != null && !checkUserResponse.getIs_user().equals("1"))
                                    sharedPref.edit().putBoolean(IS_USER, true).apply();
                                else
                                    sharedPref.edit().putBoolean(IS_USER, false).apply();
                                sharedPref.edit().putInt(SELECTED_PUSH, Constants.positionSound(checkUserResponse.getNoti_sound())).apply();
                                sharedPref.edit().putBoolean(IS_NOTIFICATION_RECEIVE, checkUserResponse.isPush_receive()).apply();
                            }
                        }, throwable -> {
                            if (throwable instanceof EOFException) {
                                userProfileLiveData.postValue(new UserProfile());
                            } else {
                                errorLiveData.postValue(throwable.getLocalizedMessage());
                            }
                        })
        );
    }

    public void registerUserInApi(String phone, String name, String uid, Activity activity, String secondName, String country, String city, String avatar64, String email) {
        compositeDisposable.add(
                userRepository.registrationUserInApi(new RegistrationParameters(email, name, uid))
                        .subscribeOn(schedulersFacade.io())
                        .observeOn(schedulersFacade.ui())
                        .subscribe(userProfile -> {
                            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(Constants.SP_KEY_USER_ID, userProfile.getId());
                            editor.putString(Constants.SP_KEY_USER_PHONE, phone);
                            editor.putString(Constants.SP_KEY_USER_EMAIL, email);
                            editor.apply();
                            userProfileLiveData.postValue(userProfile);
                            updateProfileData(phone, name, secondName, email, country, city, avatar64, userProfile);
                        }, throwable -> errorLiveData.postValue(throwable.getLocalizedMessage()))
        );
    }

    public void firebaseAuthWithGoogle(String idToken) {
        authRepository.firebaseAuthWithGoogle(idToken);
    }

    public void updateProfileData(String phone, String name, String secondName, String email, String country, String city, String avatar64, UserProfile userProfile) {
        userProfile.setName(name);
        userProfile.setLastName(secondName);
        userProfile.setEmail(email);
        userProfile.setCountry(country);
        userProfile.setCity(city);
        userProfile.setPhone(phone);
        userProfile.setAvatar(avatar64);

        compositeDisposable.add(userRepository.updateProfile(userProfile)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(response -> {
                    if(!response.getId().isEmpty()) {
                        idLiveData.postValue(response.getId());
                    }
                }, throwable -> errorLiveData.postValue(throwable.getLocalizedMessage())));
    }

    public void loadCountry() {
        compositeDisposable.add(
                userRepository.loadCountries()
                        .subscribeOn(schedulersFacade.io())
                        .observeOn(schedulersFacade.ui())
                        .subscribe(countriesLiveData::postValue,
                                throwable -> {
                                    errorLiveData.postValue(throwable.getLocalizedMessage());
                                })
        );
    }

    public void loadCities(String countryId) {
        compositeDisposable.add(
                userRepository.loadCities(countryId)
                        .subscribeOn(schedulersFacade.io())
                        .observeOn(schedulersFacade.ui())
                        .subscribe(citiesLiveData::postValue,
                                throwable -> {
                                    errorLiveData.postValue(throwable.getLocalizedMessage());
                                })
        );
    }

    public void skipAuth() {
        authRepository.skipAuth();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public MutableLiveData<UserProfile> getUserProfileLiveData() {
        return userProfileLiveData;
    }

    public MutableLiveData<List<UserLocation>> getCountriesLiveData() {
        return countriesLiveData;
    }

    public MutableLiveData<List<UserLocation>> getCitiesLiveData() {
        return citiesLiveData;
    }

    public MutableLiveData<String> getIdLiveData() {
        return idLiveData;
    }
}
