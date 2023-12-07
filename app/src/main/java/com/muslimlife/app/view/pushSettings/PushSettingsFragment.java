package com.muslimlife.app.view.pushSettings;

import static com.muslimlife.app.utils.Constants.MENU_OFF;
import static com.muslimlife.app.utils.Constants.SELECTED_PUSH;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;
import com.muslimlife.app.data.model.NotificationReceiveReq;
import com.muslimlife.app.data.model.NotificationSoundReq;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentPushBinding;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

public class PushSettingsFragment extends BaseFragment{

    private static final String TAG = "PushSettingsFragment";

    private FragmentPushBinding binding;

    private SharedPreferences sharedPreferences;
    private  PushAdapter pushAdapter;

    @Inject
    UserRepository userRepository;

    int[] sounds={R.raw.first,R.raw.second,R.raw.thirt_push_sound,R.raw.forth_push_sound,R.raw.fith_push_sound};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPushBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(MENU_OFF);

        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        binding.settingsSwitchNotification.setChecked(sharedPreferences.getBoolean(Constants.IS_NOTIFICATION_RECEIVE,true));//TODO не обрабатывается на сервере

        binding.backButton.setOnClickListener(v-> Navigation.findNavController(requireView()).popBackStack());


        pushAdapter = new PushAdapter(requireContext(),sounds, sharedPreferences.getInt(SELECTED_PUSH, 1));
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.notificationsRv.setLayoutManager(llm);
        binding.notificationsRv.setAdapter(pushAdapter);

        binding.redyButton.setOnClickListener(v -> {
            int id = Integer.parseInt(sharedPreferences.getString(Constants.SP_KEY_USER_ID, "0"));
            if (id != 0) {
                userRepository.updateNotificationsSetting(
                        new NotificationSoundReq(pushAdapter.getChoosedSoundName(), id),
                        new NotificationReceiveReq(binding.settingsSwitchNotification.isChecked(), id)
                ).subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        Log.d(TAG,"Отправлено = " + aBoolean );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(SELECTED_PUSH, pushAdapter.getChoosedSound());
                        editor.putBoolean(Constants.IS_NOTIFICATION_RECEIVE, binding.settingsSwitchNotification.isChecked());
                        editor.apply();
                        Navigation.findNavController(requireView()).popBackStack();
                    }
                });
            }
        });

        return binding.getRoot();
    }

    @Override
    public void showError(String error) {
        Log.d("fragmentError", error);
    }
}
