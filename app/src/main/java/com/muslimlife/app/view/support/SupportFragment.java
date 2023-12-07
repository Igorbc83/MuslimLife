package com.muslimlife.app.view.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.muslimlife.app.data.model.SendSupportRes;
import com.muslimlife.app.data.model.SendSupprotReq;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentSettingsBinding;
import com.muslimlife.app.databinding.FragmentSupportBinding;

public class SupportFragment extends BaseFragment {

    private FragmentSupportBinding binding;

    @Inject
    UserRepository userRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSupportBinding.inflate(inflater, container, false);
        binding.backButton.setOnClickListener(onClickListener);
        binding.supportSendButton.setOnClickListener(onClickListener);
        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);

        return binding.getRoot();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    View.OnClickListener onClickListener = v -> {

        switch (v.getId()){
            case R.id.back_button:
                Navigation.findNavController(requireView()).popBackStack();
                break;
            case R.id.support_send_button:
                if(binding.supportText.getText().toString().isEmpty())
                    Toast.makeText(requireContext(),requireContext().getString(R.string.enterText),Toast.LENGTH_SHORT).show();
                else
                    sendSupport();
                break;
        }

    };

    public void sendSupport(){
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        String id = sharedPref.getString(Constants.SP_KEY_USER_ID, "");

        if(id.equals("")){
            Toast.makeText(requireContext(), requireContext().getString(R.string.regPlease), Toast.LENGTH_SHORT).show();
            return;
        }

        SendSupprotReq sendSupprotReq = new SendSupprotReq(id, binding.supportText.getText().toString());
        userRepository.sendSupport(sendSupprotReq).subscribe(new SingleObserver<SendSupportRes[]>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull SendSupportRes[] sendSupportRes) {
                Toast.makeText(requireContext(),requireContext().getString(R.string.taskOk),Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigate(R.id.SettingsFragment);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
