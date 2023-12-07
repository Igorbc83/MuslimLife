package com.muslimlife.app.view.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.viewmodel.auth.AuthViewModel;

import javax.inject.Inject;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;
import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentAuthBinding;

public class AuthFragment extends BaseFragment {

    private FragmentAuthBinding binding;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private AuthViewModel viewModel;

    private String phoneNumber;
    public static final Slot[] RUS_PHONE_NUMBER = {
            PredefinedSlots.hardcodedSlot('7'),
            PredefinedSlots.hardcodedSlot(' ').withTags(Slot.TAG_DECORATION),
            PredefinedSlots.hardcodedSlot('(').withTags(Slot.TAG_DECORATION),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.hardcodedSlot(')').withTags(Slot.TAG_DECORATION),
            PredefinedSlots.hardcodedSlot(' ').withTags(Slot.TAG_DECORATION),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.hardcodedSlot('-').withTags(Slot.TAG_DECORATION),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.hardcodedSlot('-').withTags(Slot.TAG_DECORATION),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAuthBinding.inflate(inflater, container, false);
        ((MainActivity) requireActivity()).setMenu(Constants.MENU_OFF);
        initViewModel();
        setViewClickListeners();
        MaskImpl mask = MaskImpl.createTerminated(RUS_PHONE_NUMBER);
        FormatWatcher watcher = new MaskFormatWatcher(mask);
        watcher.installOn(binding.editTextPhoneNumber);
        return binding.getRoot();
    }

    // requireActivity() позволяет взять один экземляр AuthViewModel для двух фрагментов.
    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(AuthViewModel.class);
    }

    private void initObservables() {
        // Моментальная авторизация, подтверждение с помощью кода не требуется, сразу уходим на основной экран
        viewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), userProfile -> {
            if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                if (userProfile != null && userProfile.getId() != null ) {
                    saveUserId(userProfile.getId(), phoneNumber);
                    launchMainFragment();
                } else {
                    launchFillDataProfile();
                }
            }
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), this::showError);
    }

    private void setViewClickListeners() {
        binding.buttonNext.setOnClickListener(v -> {
           setLoading(true);
            sendCode();
            initObservables();
        });
    }

    private void sendCode() {
        String editPhoneNumber = binding.editTextPhoneNumber.getText().toString().trim();
        if (editPhoneNumber.isEmpty()) {
            showError(getString(R.string.error_number_phone_empty));
            return;
        }
        //phoneNumber = String.format("+%s", editPhoneNumber);
        phoneNumber = String.format("+%s", binding.editTextPhoneNumber.getText().toString().replace("(", "")
                .replace(")", "")
                .replace(" ", "")
                .replace("-", ""));
        viewModel.checkUserInApi(phoneNumber, requireActivity());

    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            binding.buttonNext.setEnabled(false);
            binding.buttonNext.setAlpha(0.6f);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.buttonNext.setEnabled(true);
            binding.buttonNext.setAlpha(1f);
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(String error) {
        setLoading(false);
        Toast.makeText(getContext(),
                error,
                Toast.LENGTH_SHORT).show();
    }

    // TODO: 01.07.2021 Изменить на общее решение
    private void saveUserId(String userId, String email) {
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.SP_KEY_USER_ID, userId);
        editor.putString(Constants.SP_KEY_USER_EMAIL, email);
        editor.apply();
    }

    /**
     * Отдел отвечающий за навигацию
     */
    private void launchMainFragment() {
        NavDirections action = AuthFragmentDirections.authToMain();
        Navigation.findNavController(requireView()).navigate(action);
    }

    private void launchConfirmCodeFragment(String verificationId, String phoneNumber) {
        NavDirections action = AuthFragmentDirections.authToConfirmCode(verificationId, phoneNumber);
        Navigation.findNavController(requireView()).navigate(action);
    }

    private void launchFillDataProfile() {
//        NavDirections action = AuthFragmentDirections.actionAuthFragmentToRegProfileFragment();
//        Navigation.findNavController(requireView()).navigate(action);
    }
}
