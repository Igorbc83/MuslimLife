package com.muslimlife.app.view.zakyatSettings;

import static com.muslimlife.app.utils.Constants.MENU_OFF;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentCurrencyBinding;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

public class ZakyatSettingsCurrencyFragment extends BaseFragment {

    private FragmentCurrencyBinding binding;
    SharedPreferences sharedPref;
    private ZakayatCurrencyAdapter adapter;
    public static final String key = "POSITION";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding =  FragmentCurrencyBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(MENU_OFF);
        binding.backButton.setOnClickListener(onClickListener);
        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);

        binding.redyButton.setOnClickListener(onClickListener);
        binding.backButton.setOnClickListener(onClickListener);

        setupRecycler();
        return binding.getRoot();
    }

    private void setupRecycler() {
        int selectedPosition = sharedPref.getInt(key, 0);

        adapter = new ZakayatCurrencyAdapter( getResources().getStringArray(R.array.currency),selectedPosition,(name, position) -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(key, position).apply();
        });
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.currencyAdapter.setLayoutManager(llm);
        binding.currencyAdapter.setAdapter(adapter);
    }

    View.OnClickListener onClickListener = v -> {
        switch (v.getId()){
            case R.id.redy_button:
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
