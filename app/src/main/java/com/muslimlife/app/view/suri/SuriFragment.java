package com.muslimlife.app.view.suri;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.viewmodel.main.MainViewModel;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentKoranSettingsBinding;
import com.muslimlife.app.databinding.FragmentSuriBinding;

import com.muslimlife.app.view.base.BaseFragment;

public class SuriFragment extends BaseFragment {

    @Inject
    UserRepository userRepository;

    private FragmentSuriBinding binding;
    SuriAdapter suriAdapter;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private MainViewModel viewModel;

    SharedPreferences sharedPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding =  FragmentSuriBinding.inflate(inflater, container, false);

        initViewModel();
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);
        binding.backButton.setOnClickListener(onClickListener);

        if(viewModel.userRepository.getSurahList()!=null && !viewModel.userRepository.getSurahList().isEmpty()){

            SuriAdapter.OnItemClickChildren clickChildren = (items) -> navigateToKoran(Integer.parseInt(items.getPageNumber()));

            suriAdapter = new SuriAdapter(viewModel.userRepository.getSurahList(), clickChildren, userRepository.getLanguage());
            LinearLayoutManager llm = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
            binding.suriRv.setLayoutManager(llm);
            binding.suriRv.setAdapter(suriAdapter);
        }

        return binding.getRoot();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel.class);
    }

    private void navigateToKoran(int page){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Constants.KORAN_SAVE_PAGE, page);
        editor.apply();

        Navigation.findNavController(requireView()).popBackStack(R.id.KoranFragment, false);
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
