package com.muslimlife.app.view.koranSettings;

import static com.muslimlife.app.utils.Constants.KORAN_CURRENT_READER;
import static com.muslimlife.app.utils.Constants.KORAN_TRANSLATE;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.muslimlife.app.viewmodel.main.MainViewModel;

import javax.inject.Inject;

import com.muslimlife.app.R;
import com.muslimlife.app.data.model.ReadersRes;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentKoranSettingsBinding;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

public class KoranSettingsFragment extends BaseFragment {

    private FragmentKoranSettingsBinding binding;
    SharedPreferences sharedPref;

    @Inject
    UserRepository userRepository;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding =  FragmentKoranSettingsBinding.inflate(inflater, container, false);

        initViewModel();
        initUserProfileObserve();

        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);

        ((MainActivity)requireActivity()).setMenu(MENU_OFF);
        binding.backButton.setOnClickListener(onClickListener);
        binding.koranSettingsSuri.setOnClickListener(onClickListener);
        binding.koranSettingsTranslate.setOnClickListener(onClickListener);
        binding.koranSettingsReader.setOnClickListener(onClickListener);
        binding.koranSettingsBookmark.setOnClickListener(onClickListener);

        int currentReader = sharedPref.getInt(KORAN_CURRENT_READER, 0);
        ReadersRes reader = userRepository.getKoranReaders()[currentReader];

        if(reader != null) {
            if(reader.getName() != null)
                binding.readerName.setText(reader.getName());
            if(reader.getAvatar() != null)
                Glide.with(requireContext()).load(reader.getAvatar()).into(binding.imageViewReader);
        }
        String currentTranslate = sharedPref.getString(KORAN_TRANSLATE, "");
        binding.translateLanguage.setText(currentTranslate);
        //TODO: Получить из сохранений Чтеца и перевод
        return binding.getRoot();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel.class);
    }

    private void initUserProfileObserve() {
        viewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), userProfile -> {
            if(userProfile.isSubscribed()){
                binding.koranSettingsReader.setVisibility(View.VISIBLE);
            }else{
                binding.koranSettingsReader.setVisibility(View.GONE);
            }
        });
    }

    View.OnClickListener onClickListener = v -> {
        NavDirections action=null;
        switch (v.getId()){
            case R.id.back_button:
                Navigation.findNavController(requireView()).popBackStack();
                break;
            case R.id.koran_settings_suri:
                action = KoranSettingsFragmentDirections.actionKoranSettingsFragmentToSuriFragment();
                break;
            case R.id.koran_settings_translate:
                action = KoranSettingsFragmentDirections.actionKoranSettingsFragmentToTranslateFragment();
                break;
            case R.id.koran_settings_reader:
                action = KoranSettingsFragmentDirections.actionKoranSettingsFragmentToReaderFragment();
                break;
            case R.id.koran_settings_bookmark:
                action = KoranSettingsFragmentDirections.actionKoranSettingsFragmentToBookmarkFragment();
                break;
        }
        if(action!=null)
            Navigation.findNavController(requireView()).navigate(action);
    };

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }
}
