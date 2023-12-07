package com.muslimlife.app.view.choosedWallpaper;

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
import com.muslimlife.app.databinding.FragmentChoosedWallpaperBinding;
import com.muslimlife.app.databinding.FragmentNotificationBinding;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

public class ChoosedWallpaperFragment extends BaseFragment {
    FragmentChoosedWallpaperBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChoosedWallpaperBinding.inflate(inflater, container, false);
        binding.backButton.setOnClickListener(onClickListener);
        ((MainActivity)requireActivity()).setMenu(MENU_OFF);

        return binding.getRoot();
    }

    View.OnClickListener onClickListener = v -> {
        switch (v.getId()){
            case R.id.back_button:
                Navigation.findNavController(requireView()).popBackStack();
                break;

        }
    };


    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }
}
