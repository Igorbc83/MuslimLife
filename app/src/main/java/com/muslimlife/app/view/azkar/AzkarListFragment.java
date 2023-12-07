package com.muslimlife.app.view.azkar;

import static com.muslimlife.app.utils.Constants.AZKAR_AFTER_NAMAZ;
import static com.muslimlife.app.utils.Constants.AZKAR_EVENING;
import static com.muslimlife.app.utils.Constants.AZKAR_IMPORTANT;
import static com.muslimlife.app.utils.Constants.AZKAR_MORNING;
import static com.muslimlife.app.utils.Constants.MENU_OFF;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.muslimlife.app.R;
import com.muslimlife.app.data.model.azkars.AzkarModel;
import com.muslimlife.app.data.repository.AzkarsRepository;
import com.muslimlife.app.databinding.FragmentAzkarListBinding;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.adapters.OnItemSelectListener;
import com.muslimlife.app.view.azkar.adapters.AzkarListAdapter;
import com.muslimlife.app.view.base.BaseFragment;

public class AzkarListFragment extends BaseFragment implements OnItemSelectListener {

    private FragmentAzkarListBinding binding;

    @Inject
    AzkarsRepository azkarsRepository;

    List<AzkarModel> azkars;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAzkarListBinding.inflate(inflater, container, false);
        ((MainActivity) requireActivity()).setMenu(MENU_OFF);

        initView();

        return binding.getRoot();
    }

    private void initView(){
        binding.backButton.setOnClickListener(v-> Navigation.findNavController(requireView()).popBackStack());

        azkars = azkarsRepository.getAzkarsByType(requireArguments().getInt(Constants.BUNDLE_AZKAR));

        switch (requireArguments().getInt(Constants.BUNDLE_AZKAR)){
            case AZKAR_MORNING:
                binding.title.setText(getString(R.string.azkar_morning));
                break;
            case AZKAR_EVENING:
                binding.title.setText(getString(R.string.azkar_evening));
                break;
            case AZKAR_IMPORTANT:
                binding.title.setText(getString(R.string.azkar_important));
                break;
            case AZKAR_AFTER_NAMAZ:
                binding.title.setText(getString(R.string.azkar_after_namaz));
                break;
        }

        AzkarListAdapter adapter = new AzkarListAdapter(azkars, this);
        binding.azkarsRecycler.setAdapter(adapter);
    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void onItemSelect(int position) {
        Bundle arg = new Bundle();
        arg.putInt(Constants.BUNDLE_AZKAR , requireArguments().getInt(Constants.BUNDLE_AZKAR));
        arg.putInt(Constants.BUNDLE_AZKAR_POSITION , position);
        Navigation.findNavController(requireView()).navigate(R.id.azkarFragment, arg);
    }
}
