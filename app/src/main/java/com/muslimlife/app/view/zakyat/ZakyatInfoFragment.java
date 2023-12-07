package com.muslimlife.app.view.zakyat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

import com.muslimlife.app.databinding.FragmentZakyatInfoBinding;

public class ZakyatInfoFragment  extends BaseFragment {

    private FragmentZakyatInfoBinding binding;

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentZakyatInfoBinding.inflate(inflater, container, false);

        binding.backButton.setOnClickListener(view ->
                Navigation.findNavController(requireView()).popBackStack());
        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);

        return binding.getRoot();
    }
}
