package com.muslimlife.app.view.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.viewmodel.auth.AuthViewModel;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentConfirmCodeBinding;

import com.muslimlife.app.view.base.BaseFragment;

public class ConfirmCodeFragment extends BaseFragment {

    private FragmentConfirmCodeBinding binding;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private AuthViewModel viewModel;

    private String verificationId;
    private String phoneNumber;

    private CountDownTimer timer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentConfirmCodeBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);
        parseArguments();
        startTimer();
        initViewModel();
        initObservables();
        setViewListeners();

        return binding.getRoot();
    }

    private void parseArguments() {
        if (getArguments() != null) {
            verificationId = ConfirmCodeFragmentArgs.fromBundle(getArguments()).getVerificationId();
            phoneNumber = ConfirmCodeFragmentArgs.fromBundle(getArguments()).getPhoneNumber();
        }
    }

    // requireActivity() позволяет взять один экземляр AuthViewModel для двух фрагментов.
    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(AuthViewModel.class);
    }

    private void initObservables() {
        // Авторизация подтверждена, уходим на основной экран
        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
            if(firebaseUser.isAnonymous()) {
                launchMainFragment();
            } else {
                viewModel.checkUserInApi(phoneNumber, requireActivity());
            }
        });

        viewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), userProfile -> {
            if(userProfile != null && userProfile.getId() != null) {
                saveUserId(userProfile.getId());
                launchMainFragment();
            } else {
                launchFillDataProfile();
            }
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), this::showError);
    }

    private void setViewListeners() {
        binding.editCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && !s.toString().isEmpty() && s.length() == 6) {
                    viewModel.verifySmsCode(verificationId, s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.textSendNewCode.setOnClickListener(v -> {
            binding.textSendNewCode.setVisibility(View.GONE);
            resendCode();
        });

        binding.backButton.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());

        binding.layoutSkip.setOnClickListener(v -> viewModel.skipAuth());
    }

    private void startTimer() {
        timer = new CountDownTimer(TimeUnit.MINUTES.toMillis(3), TimeUnit.SECONDS.toMillis(1)) {
            @Override
            public void onTick(long milliseconds) {
                int seconds = (int) (milliseconds / 1000 % 60);
                int minutes = (int) (milliseconds / 1000 / 60);
                String formattedNumber = phoneNumber.substring(0, 2) + " " + "(" + phoneNumber.substring(2, 5) + ") " + phoneNumber.substring(5, 8) + "-" + phoneNumber.substring(8, 10) + "-" + phoneNumber.substring(10, 12);
                // "<font color='black'>" не работает через getString. Тоесть нужно харкодить здесь
                binding.textTimer.setText(
                        Html.fromHtml(getString(R.string.sms_text, "<font color='black'>" + formattedNumber + "</font>",
                                String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)),
                                Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.EDITABLE);
            }

            @Override
            public void onFinish() {
                binding.textSendNewCode.setVisibility(View.VISIBLE);

                // "<font color='black'>" не работает через getString. Тоесть нужно харкодить здесь
                binding.textTimer.setText(Html.fromHtml(getString(R.string.sms_text_without_timer, "<font color='black'>" + phoneNumber + "</font>"), Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.EDITABLE);
            }
        }.start();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(),
                error,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    private void resendCode() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(requireActivity())
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                viewModel.signInWithCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                showError(e.getLocalizedMessage());
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                startTimer();
                            }
                        }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // TODO: 01.07.2021 Изменить на общее решение
    private void saveUserId(String userId) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.SP_KEY_USER_ID, userId);
        editor.apply();
    }

    /**
     * Отдел отвечающий за навигацию
     */
    private void launchMainFragment() {
        NavDirections action = ConfirmCodeFragmentDirections.confirmToMain();
        Navigation.findNavController(requireView()).navigate(action);
    }

    private void launchFillDataProfile() {
//        NavDirections action = ConfirmCodeFragmentDirections.confirmToProfile();
//        Navigation.findNavController(requireView()).navigate(action);
    }
}
