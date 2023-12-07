package com.muslimlife.app.view.language;

import static android.content.Context.MODE_PRIVATE;
import static com.muslimlife.app.utils.Constants.LANGUAGE;
import static com.muslimlife.app.utils.Constants.MENU_OFF;

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
import com.muslimlife.app.databinding.FragmentSupportBinding;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.utils.MyContextWrapper;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

public class LanguageFragment extends BaseFragment {

    private FragmentLanguageBinding binding;

    LanguageAdapter languageAdapter;

    int choosedPos=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLanguageBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(MENU_OFF);
        binding.backButton.setOnClickListener(view-> Navigation.findNavController(requireView()).popBackStack());

        String[] languages={
                requireContext().getString(R.string.english),
                requireContext().getString(R.string.russian),
                requireContext().getString(R.string.uzbek),
                requireContext().getString(R.string.turk),
                requireContext().getString(R.string.tadzhik),
                requireContext().getString(R.string.kirg),
                requireContext().getString(R.string.franch)};

        String[] languagesCode={
                "en",
                "ru",
                "uz",
                "tr",
                "tg",
                "ky",
                "fr"};

        Context context = MyContextWrapper.wrap(requireContext(), requireActivity().getSharedPreferences("muslimLife.lang", MODE_PRIVATE).getString(LANGUAGE,"en"));
        getResources().updateConfiguration(context.getResources().getConfiguration(), context.getResources().getDisplayMetrics());

        binding.languageRedyButton.setOnClickListener(view->{
            choosedPos = languageAdapter.choosed_pos;
            Locale locale = new Locale(languagesCode[choosedPos]);
            /*Locale.setDefault(locale);
            Resources resources = requireActivity().getResources();
            Configuration config = resources.getConfiguration();
            config.setLocale(locale);
            resources.updateConfiguration(config, resources.getDisplayMetrics());*/
            SharedPreferences sharedPref = requireActivity().getSharedPreferences("muslimLife.lang", MODE_PRIVATE);
            sharedPref.edit().putString(LANGUAGE, locale.getLanguage()).apply();
            ((MainActivity)requireActivity()).changeLanguage(true);
            //Navigation.findNavController(requireView()).navigate(R.id.splashFragment);
            //requireActivity().recreate();
        });

        String language = requireActivity().getSharedPreferences("muslimLife.lang", MODE_PRIVATE).getString(LANGUAGE,Constants.DEFAULT_LANG);

        setChoosedPos(languagesCode, language);

        languageAdapter = new LanguageAdapter(languages,choosedPos);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.languageRv.setLayoutManager(llm);
        binding.languageRv.setAdapter(languageAdapter);

        return binding.getRoot();
    }

    private void setChoosedPos(String[] languagesCode, String currentLang){
        for(int i = 0; i < languagesCode.length; i++)
            if(currentLang.equals(languagesCode[i])){
                choosedPos = i;
                return;
            }
    }

    @Override
    public void showError(String error) {
        Log.e("LanguageFragment",error);
    }
}
