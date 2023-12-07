package com.muslimlife.app.viewmodel.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import com.muslimlife.app.app.SchedulersFacade;
import com.muslimlife.app.data.model.UserLocation;
import com.muslimlife.app.data.model.UserProfile;
import com.muslimlife.app.data.model.parameters.LoadUserDataParameters;
import com.muslimlife.app.data.repository.UserRepository;

public class ProfileViewModel extends ViewModel {

    private CompositeDisposable compositeDisposable;
    private SchedulersFacade schedulersFacade;

    private UserRepository userRepository;
    private MutableLiveData<UserProfile> userProfileLiveData;
    private MutableLiveData<String> errorLiveData;
    private MutableLiveData<String> successLiveData;

    private final MutableLiveData<List<UserLocation>> countriesLiveData;
    private final MutableLiveData<List<UserLocation>> citiesLiveData;

    @Inject
    public ProfileViewModel(SchedulersFacade schedulersFacade, UserRepository userRepository) {
        this.compositeDisposable = new CompositeDisposable();
        this.schedulersFacade = schedulersFacade;
        this.userRepository = userRepository;

        this.userProfileLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
        this.successLiveData = new MutableLiveData<>();
        this.countriesLiveData = new MutableLiveData<>();
        this.citiesLiveData = new MutableLiveData<>();
    }

    public void loadUserProfile(String id) {
        compositeDisposable.add(
                userRepository.getUserProfile(new LoadUserDataParameters(id))
                        .subscribeOn(schedulersFacade.io())
                        .observeOn(schedulersFacade.ui())
                        .subscribe(userProfile -> {
                            userProfileLiveData.postValue(userProfile);
                        }, throwable -> {
                            errorLiveData.postValue(throwable.getLocalizedMessage());
                        })
        );
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

    public void updateProfileData(UserProfile userProfile) {
        if(userProfile == null) {
            return;
        }

        compositeDisposable.add(userRepository.updateProfile(userProfile)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(response -> {
                    if(!response.getId().isEmpty()) {
                        successLiveData.postValue("Yea!");
                        userProfile.setId(response.getId());
                    }
                }, throwable -> errorLiveData.postValue(throwable.getLocalizedMessage())));

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    public MutableLiveData<UserProfile> getUserProfileLiveData() {
        return userRepository.getUserProfileMutableLiveData();
    }

    public MutableLiveData<List<UserLocation>> getCountriesLiveData() {
        return countriesLiveData;
    }

    public MutableLiveData<List<UserLocation>> getCitiesLiveData() {
        return citiesLiveData;
    }
}
