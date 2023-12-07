package com.muslimlife.app.view;

import static android.content.Context.MODE_PRIVATE;
import static com.muslimlife.app.utils.Constants.MENU_ANSWER;
import static com.muslimlife.app.utils.Constants.MENU_OFF;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.muslimlife.app.data.model.azkars.AzkarsRes;
import com.muslimlife.app.data.repository.AzkarsRepository;
import com.muslimlife.app.utils.CacheHelper;
import com.muslimlife.app.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;
import com.muslimlife.app.data.model.DiaspoarResponce;
import com.muslimlife.app.data.model.GetAnswerReq;
import com.muslimlife.app.data.model.GetAnswerRes;
import com.muslimlife.app.data.model.GetQuastionsReq;
import com.muslimlife.app.data.model.GetQuastionsRes;
import com.muslimlife.app.data.model.ImamResMain;
import com.muslimlife.app.data.model.NotificationReq;
import com.muslimlife.app.data.model.NotificationRes;
import com.muslimlife.app.data.model.PlacesRes;
import com.muslimlife.app.data.model.ReadersRes;
import com.muslimlife.app.data.model.UserLocation;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentSplashBinding;

import com.muslimlife.app.view.base.BaseFragment;

public class SplashFragment extends BaseFragment {

    private FragmentSplashBinding binding;

    @Inject
    UserRepository userRepository;

    @Inject
    AzkarsRepository azkarsRepository;

    boolean flagImam, flagDiaspoar, flagPlaces, flagReaders, flagCites, flagCountries,
            flagQuastions, flagNotifications, flagMessages, flagAzkars;
    String email;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        ((MainActivity) requireActivity()).setMenu(MENU_OFF);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        animateProgress();
        getUserEmail();
        getPlaces();
        getReaders();
        getImams();
        getDiaspoars();
        getCountries();
        getCities();
        getNotifications();
        getMessages();
        getAzkars();
    }

    private String getUserEmail() {

        if (email!= null){
            return email;
        }else {
            SharedPreferences sharedPref = requireActivity().getPreferences(MODE_PRIVATE);
            String pref = sharedPref.getString(Constants.SP_KEY_USER_EMAIL, null);
            email = pref;
            return pref;
        }
    }

    public void checkQuastions(){
        SharedPreferences sharedPref = requireActivity().getPreferences(MODE_PRIVATE);
        GetQuastionsReq getQuastionsReq = new GetQuastionsReq(sharedPref.getString(Constants.SP_KEY_USER_ID, "0"));
        userRepository.getQuastions(getQuastionsReq).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull GetQuastionsRes[] getQuastionsRes) {
                flagQuastions = true;

                launch();

                flagQuastions = true;

                for (GetQuastionsRes quastionsRes : getQuastionsRes)
                    if (quastionsRes.getAnswered().equals("0")) {
                        ((MainActivity)requireActivity()).bnm.getOrCreateBadge(MENU_ANSWER).setVisible(true);
                        return;
                    } else
                        ((MainActivity)requireActivity()).bnm.getOrCreateBadge(MENU_ANSWER).setVisible(false);

                flagQuastions = true;
            }

            @Override
            public void onError(@NonNull Throwable e) {
                ((MainActivity)requireActivity()).bnm.getOrCreateBadge(MENU_ANSWER).setVisible(false);
                flagQuastions = true;

                launch();

                Log.e("checkQuastions", e.getMessage());
            }
        });
    }

    void getImams() {
        userRepository.getImams(requireActivity().getPreferences(MODE_PRIVATE).getString(Constants.SP_KEY_USER_ID, "0")).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull ImamResMain[] imamResMains) {
                flagImam = true;
                userRepository.setImamResMains(imamResMains);
                launch();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                flagImam = true;
                launch();
            }
        });
    }

    void getDiaspoars(){
        userRepository.getDiaspoars().subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull DiaspoarResponce[] diaspoarResponces) {
                flagDiaspoar = true;
                userRepository.setDiaspoarResponces(diaspoarResponces);

                launch();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                flagDiaspoar = true;
                Log.e("getDiaspoars", "onError: "+e.getMessage() );
                launch();
            }
        });
    }

    void getReaders(){
        userRepository.getReaders().subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull ReadersRes[] readersRes) {
                flagReaders = true;
                userRepository.setKoranReaders(readersRes);
                launch();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                flagReaders = true;
                Log.e("getReaders", "onError: "+e.getMessage() );
                launch();
            }
        });
    }

    void getPlaces(){
        userRepository.getPlaces().subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onSuccess(@NonNull PlacesRes[] placesResp) {
                flagPlaces=true;
                userRepository.setPlacesRes(placesResp);
                launch();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                flagPlaces=true;
                Log.e("getPlaces", e.getMessage());
                launch();
            }
        });
    }

    private void getCountries(){
        userRepository.loadCountries().subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<UserLocation> userLocations) {
                flagCountries = true;
                userRepository.setCountriesList(userLocations);
                launch();
            }

            @Override
            public void onError(Throwable e) {
                flagCountries=true;
                Log.e("loadCountriesError", e.getMessage());
                launch();
            }
        });
    }

    private void getCities(){
        userRepository.loadCities(requireActivity().getPreferences(MODE_PRIVATE)
                .getString(Constants.COUNTRY,"0")).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<UserLocation> userLocations) {
                flagCites = true;
                userRepository.setCitiesList(userLocations);
                launch();
            }

            @Override
            public void onError(Throwable e) {
                flagCites = true;
                Log.e("loadCountriesError", e.getMessage());
                launch();
            }
        });
    }

    private void getNotifications(){
        SharedPreferences sharedPref;
        try{
            sharedPref = requireActivity().getPreferences(MODE_PRIVATE);
        }catch (Exception e){
            sharedPref = ((MainActivity)requireActivity()).sharedPref;
        }
        NotificationReq notificationReq = new NotificationReq(sharedPref.getString(Constants.SP_KEY_USER_ID, "0"));

        userRepository.getNotifications(notificationReq).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {}

            @Override
            public void onSuccess(@NonNull NotificationRes[] notificationRes) {
                ArrayList<NotificationRes> notifications = new ArrayList<>(Arrays.asList(notificationRes));

                notifications.removeIf(notification -> notification.getRead().equals("1"));

                userRepository.setNotifReaded(notifications.size() > 0);

                flagNotifications=true;

                launch();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                flagNotifications=true;
                Log.e("getNotifications",e.getMessage());

                launch();
            }
        });
    }

    private void getMessages(){
        SharedPreferences sharedPref = getActivity().getPreferences(MODE_PRIVATE);
        GetAnswerReq getAnswerReq = new GetAnswerReq(sharedPref.getString(Constants.SP_KEY_USER_ID, "0"));

        if(getAnswerReq.getUser_id().equals("0")){
            flagMessages = true;
            return;
        }

        userRepository.getAnswer(getAnswerReq).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull GetAnswerRes[] getAnswerRes) {
                ArrayList<GetAnswerRes> messages = new ArrayList<>(Arrays.asList(getAnswerRes));

                messages.removeIf(answer -> answer.getAnswer_status().equals("0"));

                userRepository.setMessageReaded(messages.size() > 0);

                flagMessages = true;

                launch();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                flagMessages = true;
                Log.e("getAnswers",e.getMessage());

                launch();
            }
        });
    }

    void getAzkars() {
        azkarsRepository.setLanguage(userRepository.getLanguage());

        AzkarsRes azkars = CacheHelper.loadAzkars(requireActivity());

        if(azkars != null && azkars.getAfterNamaz() != null && azkars.getEvening() != null &&
        azkars.getImportant() != null && azkars.getMorning() != null){
            flagAzkars = true;
            azkarsRepository.setAzkarsRes(azkars);
            launch();
        }else {
            azkarsRepository.getAzkars().subscribe(new SingleObserver<>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onSuccess(@NonNull AzkarsRes azkarsRes) {
                    flagAzkars = true;
                    CacheHelper.saveAzkars(requireActivity(), azkarsRes);
                    azkarsRepository.setAzkarsRes(azkarsRes);
                    launch();
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    flagAzkars = true;
                    launch();
                }
            });
        }
    }

    private void launchAuthFragment() {
        requireActivity().runOnUiThread(()-> {
            try {
                Navigation.findNavController(requireView()).navigate(R.id.splash_to_auth);
            }catch (Exception e){
                Log.e("launchMainFragment", "launchMainFragment: "+e.getLocalizedMessage() );
            }
        });
    }

    private void launchMainFragment() {
       requireActivity().runOnUiThread(()-> {
           try {
               Navigation.findNavController(requireView()).navigate(R.id.splash_to_main);
           }catch (Exception e){
               Log.e("launchMainFragment", "launchMainFragment: "+e.getLocalizedMessage() );
           }
       });
    }

    private void launchLanguageFragment() {
        requireActivity().runOnUiThread(()-> {
            try {
                Navigation.findNavController(requireView()).navigate(R.id.splash_to_lang);
            }catch (Exception e){
                Log.e("launchMainFragment", "launchMainFragment: "+e.getLocalizedMessage() );
            }
        });
    }

    private void launch(){
        if(!check())
            return;

        try {
            SharedPreferences sharedPref = requireActivity().getSharedPreferences("muslimLife.lang", MODE_PRIVATE);
            if (!sharedPref.getBoolean(Constants.SP_KEY_LAUNCH_LANG, false)) {
                launchLanguageFragment();
            } else {
                if (FirebaseAuth.getInstance().getCurrentUser() != null && getUserEmail() != null) {
                    launchMainFragment();
                } else {
                    launchAuthFragment();
                }
            }
        }catch (Exception ignored){}
    }

    private boolean check(){
        if(!flagImam)
            return false;

        if(!flagDiaspoar)
            return false;

        if(!flagPlaces)
            return false;

        if(!flagReaders)
            return false;

        if(!flagCountries)
            return false;

        if(!flagCites)
            return false;

        /*if(!flagQuastions){
            checkQuastions();
            return false;
        }*/

        if(!flagNotifications)
            return false;

        if(!flagMessages)
            return false;

        if(!flagAzkars)
            return false;

        return true;
    }

    private void animateProgress(){
        ObjectAnimator.ofInt(binding.progress, "progress", 100)
                .setDuration(1600)
                .start();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }
}
