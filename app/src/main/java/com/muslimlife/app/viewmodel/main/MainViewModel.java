package com.muslimlife.app.viewmodel.main;

import static com.muslimlife.app.utils.Constants.IS_USER;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.EOFException;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import com.muslimlife.app.app.SchedulersFacade;
import com.muslimlife.app.data.model.UserProfile;
import com.muslimlife.app.data.model.parameters.CheckUserParameters;
import com.muslimlife.app.data.model.parameters.LoadUserDataParameters;
import com.muslimlife.app.data.repository.UserRepository;

public class MainViewModel extends ViewModel {

    private CompositeDisposable compositeDisposable;
    private SchedulersFacade schedulersFacade;

    public UserRepository userRepository;

    private MutableLiveData<UserProfile> userProfileLiveData;
    private MutableLiveData<String> errorLiveData;

    @Inject
    public MainViewModel(SchedulersFacade schedulersFacade, UserRepository userRepository) {
        this.schedulersFacade = schedulersFacade;
        this.userRepository = userRepository;
        this.compositeDisposable = new CompositeDisposable();

        this.userProfileLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
    }

    public void loadUserProfile(String id) {
        if(!id.equals("0")) {
            compositeDisposable.add(
                    userRepository.getUserProfile(new LoadUserDataParameters(id))
                            .subscribeOn(schedulersFacade.io())
                            .observeOn(schedulersFacade.ui())
                            .subscribe(userProfile -> userProfileLiveData.postValue(userProfile),
                                    throwable -> errorLiveData.postValue(throwable.getLocalizedMessage()))
            );
        }
    }

    public MutableLiveData<UserProfile> getUserProfileLiveData() {
        return userRepository.getUserProfileMutableLiveData();
    }

    public void checkUserInApi(String phoneNumber, Activity activity) {
        compositeDisposable.add(
                userRepository.checkUserInApi(new CheckUserParameters(phoneNumber))
                        .subscribeOn(schedulersFacade.io())
                        .observeOn(schedulersFacade.ui())
                        .subscribe(checkUserResponse -> {
                            UserProfile userProfile = new UserProfile();
                            userProfile.setId(checkUserResponse.getId());
                            userProfileLiveData.postValue(userProfile);
                            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
                            if(!checkUserResponse.getIs_user().equals("1"))
                                sharedPref.edit().putBoolean(IS_USER, true).apply();
                        }, throwable -> {
                            if (throwable instanceof EOFException) {
                                userProfileLiveData.postValue(new UserProfile());
                            } else {
                                errorLiveData.postValue(throwable.getLocalizedMessage());
                            }
                        })
        );
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
