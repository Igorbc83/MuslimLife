package com.muslimlife.app.view.translate;

import static com.muslimlife.app.utils.Constants.KORAN_TRANSLATE;
import static com.muslimlife.app.utils.Constants.KORAN_TRANSLATE_POSITION;
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

import com.muslimlife.app.view.language.LanguageAdapter;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentTranslateBinding;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

public class TranslateFragment extends BaseFragment {

    private FragmentTranslateBinding binding;
    LanguageAdapter languageAdapter;
    SharedPreferences sharedPref;
    View.OnClickListener onClickListener = v -> {
        switch (v.getId()){
            case R.id.back_button:
                Navigation.findNavController(requireView()).popBackStack();
                break;
            case R.id.redyTranslateButton:
                Navigation.findNavController(requireView()).popBackStack();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(KORAN_TRANSLATE_POSITION, languageAdapter.getCurrentLanguagePosition()).apply();
                editor.putString(KORAN_TRANSLATE, languageAdapter.getCurrentLanguage()).apply();

                break;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding =  FragmentTranslateBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(MENU_OFF);
        binding.backButton.setOnClickListener(onClickListener);
        binding.redyTranslateButton.setOnClickListener(onClickListener);
        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);

        String[] languages={requireContext().getString(R.string.translate_emil)};
        int currentPosition = sharedPref.getInt(KORAN_TRANSLATE_POSITION, 0);
        languageAdapter = new LanguageAdapter(languages,currentPosition);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.languageRv.setLayoutManager(llm);
        binding.languageRv.setAdapter(languageAdapter);
        return binding.getRoot();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }
}
