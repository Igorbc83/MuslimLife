package com.muslimlife.app.view.settings;

import static android.content.Context.MODE_PRIVATE;
import static com.muslimlife.app.utils.Constants.MENU_OFF;
import static com.muslimlife.app.utils.Constants.OPEN_AUTH;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import com.muslimlife.app.R;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentSettingsBinding;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

public class SettingsFragment extends BaseFragment {

    private FragmentSettingsBinding binding;
    private SettingsAdapter settingsAdapter;
    @Inject
    UserRepository userRepository;
    private FirebaseUser firebaseUser;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        ((MainActivity) requireActivity()).setMenu(MENU_OFF);
        fillUserInfo();

        if(firebaseUser!= null && firebaseUser.isAnonymous()){
            binding.nested.setVisibility(View.GONE);
            binding.authBlock.setVisibility(View.VISIBLE);
            binding.mainLayout.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.light_white));
            binding.regButton.setOnClickListener(v-> {
                FirebaseAuth.getInstance().signOut();
                Bundle bundle = new Bundle();
                bundle.putBoolean(OPEN_AUTH, false);
                Navigation.findNavController(requireView()).navigate(R.id.newAuthFragment, bundle);
            });
        }else{
            initViewClickerListener();
            binding.settingsSub.subLayout.setOnClickListener(onClickListener);

            binding.settingsQrCode.subLayout.setOnClickListener(onClickListener);
            binding.settingsQrCode.settingsBlockIcon.setImageResource(R.drawable.qr_code_settings_icon);
            binding.settingsQrCode.settingsBlockText.setText(getString(R.string.qr_code_settings));

            binding.settingsMlcoin.subLayout.setOnClickListener(onClickListener);
            binding.settingsMlcoin.settingsBlockIcon.setImageResource(R.drawable.settings_mlcoin_icon);
            binding.settingsMlcoin.settingsBlockText.setText(getString(R.string.balance) + " MLCoin");

            sharedPreferences=requireActivity().getSharedPreferences("muslimlife.Services",MODE_PRIVATE);

            //binding.switchBlock.settingsSwitchNotification.setChecked(sharedPreferences.getBoolean("Notification",true));
            binding.switchBlock.settingsSwitchLocation.setChecked(sharedPreferences.getBoolean("Location", true));

            binding.switchBlock.notifications.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(R.id.pushtSettings));

            binding.switchBlock.settingsSwitchLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
                sharedPreferences.edit().putBoolean("Location", isChecked).apply();
            });

/*
            binding.switchBlock.settingsSwitchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
                sharedPreferences.edit().putBoolean("Notification", isChecked).apply();
            });
*/

            settingsAdapter = new SettingsAdapter(container, userRepository.isNotifReaded() || userRepository.isMessageReaded());
            LinearLayoutManager llm = new LinearLayoutManager(requireContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            binding.settingsBlockRv.setLayoutManager(llm);
            binding.settingsBlockRv.setAdapter(settingsAdapter);
        }
        binding.backButtonSettings.setOnClickListener(onClickListener);

        return binding.getRoot();
    }

    private void fillUserInfo() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null)
            return;

        if (firebaseUser.isAnonymous())
            binding.email.setVisibility(View.GONE);
        else {
            binding.email.setText(firebaseUser.getEmail());
//            SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
//            String id = sharedPref.getString(SP_KEY_USER_ID, "");
//            if (!id.isEmpty())
//                userRepository.getUserProfile(new LoadUserDataParameters(id)).subscribe(new SingleObserver<UserProfile>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                    }
//
//                    @SuppressLint("UseCompatLoadingForDrawables")
//                    @Override
//                    public void onSuccess(@NonNull UserProfile userProfile) {
//                        requireActivity().runOnUiThread(() -> {
//                            String phone = userProfile.getPhone();
//                            if (phone != null && !phone.isEmpty() && phone.length() > 11)
//                                binding.phoneNumber.setText(phone.substring(0, 2) + " (" + phone.substring(2, 5) + ") " + phone.substring(5, 8) + "-" + phone.substring(8, 10) + "-" + phone.substring(10, 12));
//                            else
//                                binding.phoneNumber.setText("");
//                        });
//
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                    }
//                });

        }
    }


    private void initViewClickerListener() {
        binding.settingsProfileData.setOnClickListener(v -> {
            if (firebaseUser == null)
                return;

            if (firebaseUser.isAnonymous()) {
                FirebaseAuth.getInstance().signOut();
                startAuthFragment();
            } else
                startProfileFragment();
        });
    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.back_button_settings:
                Navigation.findNavController(requireView()).navigate(R.id.mainFragment);
                break;
            case R.id.settings_sub:
                Navigation.findNavController(requireView()).navigate(R.id.subRuStoreFragment);
                break;
            case R.id.settings_mlcoin:
                Navigation.findNavController(requireView()).navigate(R.id .MLCoinBillingNewFragment);
                //Intent intent = new Intent(requireActivity(), MLCoinActivity.class);
                //startActivity(intent);
                break;
            case R.id.settings_qr_code:
                Navigation.findNavController(requireView()).navigate(R.id.qrCodeFragment);
                break;
        }
    };

    private void startProfileFragment() {
        NavDirections action = SettingsFragmentDirections.settingsFragmentToProfileFragment();
        Navigation.findNavController(requireView()).navigate(action);
    }

    private void startAuthFragment() {
        NavDirections action = SettingsFragmentDirections.settingsFragmentToAuthFragment();
        Navigation.findNavController(requireView()).navigate(action);
    }
}


