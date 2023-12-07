package com.muslimlife.app.view.tasbih;

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

import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.utils.ServicesUtil;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentTasbikhBinding;

public class TasbihFragment extends BaseFragment implements TasbihListener{

    private FragmentTasbikhBinding binding;

    TasbihControl tasbihControl;

    //todo remove test shared
    SharedPreferences sharedPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTasbikhBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);
        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);

        binding.backButton.setOnClickListener(v -> Navigation.findNavController(requireView()).popBackStack());

        initTasbih();

        ServicesUtil.updateScore(sharedPref, Constants.SERVICE_TASBIH);

        return binding.getRoot();
    }

    private void initTasbih(){

        tasbihControl = new TasbihControl(this,
                sharedPref.getInt(Constants.PREFS_TASBIH_ROUND, 0),
                sharedPref.getInt(Constants.PREFS_TASBIH_COUNT, 0));
        binding.tasbihView.initMain(tasbihControl);

        binding.tasbihReload.setOnClickListener(v -> tasbihControl.reset());
    }

    @Override
    public void onDestroy() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Constants.PREFS_TASBIH_ROUND, tasbihControl.round);
        editor.putInt(Constants.PREFS_TASBIH_COUNT, tasbihControl.currentCount);
        editor.apply();
        super.onDestroy();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void update(int round, int count) {
        binding.tasbihCount.setText(String.valueOf(count));
        binding.tasbihRound.setText(String.format("%s %s", getString(R.string.tasbih_fragment_round), (round + 1)));
        binding.tasbihText.setText(getResources().getStringArray(R.array.tasbih_round)[round]);
    }
}
