package com.muslimlife.app.view.language;

import static android.content.Context.MODE_PRIVATE;
import static com.muslimlife.app.utils.Constants.LANGUAGE;
import static com.muslimlife.app.utils.Constants.MENU_OFF;
import static com.muslimlife.app.utils.Constants.SP_KEY_LAUNCH_LANG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Locale;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentLanguageBinding;
import com.muslimlife.app.databinding.FragmentLanguageStartBinding;
import com.muslimlife.app.utils.MyContextWrapper;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

public class LanguageStartFragment extends BaseFragment {

    private FragmentLanguageStartBinding binding;

    LanguageAdapter languageAdapter;

    int choosedPos = 0;

    private String[] btnText = {
            "Continue",
            "Продолжить",
            "Davom eting",
            "Devam etmek",
            "Давом додан",
            "Улантуу",
            "Continuer"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLanguageStartBinding.inflate(inflater, container, false);
        ((MainActivity) requireActivity()).setMenu(MENU_OFF);

        String[] languages = {
                "English",
                "Русский",
                "O'zbek",
                "Türkçe",
                "Тоҷикӣ",
                "Кыргызча",
                "Français"};

        String[] languagesCode = {
                "en",
                "ru",
                "uz",
                "tr",
                "tg",
                "ky",
                "fr"};

        Context context = MyContextWrapper.wrap(requireContext(), requireActivity().getSharedPreferences("muslimLife.lang", MODE_PRIVATE).getString(LANGUAGE, "en"));
        getResources().updateConfiguration(context.getResources().getConfiguration(), context.getResources().getDisplayMetrics());

        binding.languageRedyButton.setOnClickListener(view -> {
            choosedPos = languageAdapter.choosed_pos;
            Locale locale = new Locale(languagesCode[choosedPos]);
            /*Locale.setDefault(locale);
            Resources resources = requireActivity().getResources();
            Configuration config = resources.getConfiguration();
            config.setLocale(locale);
            resources.updateConfiguration(config, resources.getDisplayMetrics());*/
            SharedPreferences sharedPref = requireActivity().getSharedPreferences("muslimLife.lang", MODE_PRIVATE);
            sharedPref.edit().putString(LANGUAGE, locale.getLanguage()).apply();
            sharedPref.edit().putBoolean(SP_KEY_LAUNCH_LANG, true).apply();
            ((MainActivity) requireActivity()).changeLanguage(true);
            //Navigation.findNavController(requireView()).navigate(R.id.splashFragment);
            //requireActivity().recreate();
        });

        String language = requireActivity().getSharedPreferences("muslimLife.lang", MODE_PRIVATE).getString(LANGUAGE, "en");

        setChoosedPos(languagesCode, language);

        languageAdapter = new LanguageAdapter(languages, choosedPos, pos -> {
            binding.languageRedyButton.setText(btnText[pos]);
        });
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.languageRv.setLayoutManager(llm);
        binding.languageRv.setAdapter(languageAdapter);

        return binding.getRoot();
    }

    private void setChoosedPos(String[] languagesCode, String currentLang) {
        for (int i = 0; i < languagesCode.length; i++)
            if (currentLang.equals(languagesCode[i])) {
                choosedPos = i;

                binding.languageRedyButton.setText(btnText[choosedPos]);
                return;
            }
    }

    @Override
    public void showError(String error) {
        Log.e("LanguageFragment", error);
    }
}
