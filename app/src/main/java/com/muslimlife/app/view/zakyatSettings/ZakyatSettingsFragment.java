package com.muslimlife.app.view.zakyatSettings;

import static com.muslimlife.app.utils.Constants.MENU_OFF;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentSettingsZakyatBinding;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

public class ZakyatSettingsFragment extends BaseFragment {

    private FragmentSettingsZakyatBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding =  FragmentSettingsZakyatBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(MENU_OFF);
        binding.backButton.setOnClickListener(onClickListener);

        binding.fonds.setOnClickListener(onClickListener);
        binding.currency.setOnClickListener(onClickListener);

        return binding.getRoot();
    }

    View.OnClickListener onClickListener = v -> {
        switch (v.getId()){
            case R.id.back_button:
                Navigation.findNavController(requireView()).popBackStack();
                break;
            case R.id.fonds:
                Navigation.findNavController(requireView()).navigate(R.id.zakyatSettingsFonds);
                break;
            case R.id.currency:
                Navigation.findNavController(requireView()).navigate(R.id.zakyatSettingsCurrency);
                break;
        }
    };

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }
}
