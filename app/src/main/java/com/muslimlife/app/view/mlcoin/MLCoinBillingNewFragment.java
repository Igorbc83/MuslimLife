package com.muslimlife.app.view.mlcoin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import javax.inject.Inject;

import all.muslimlife.data.repository.WebPayRepository;
import all.muslimlife.view.webpay.WebPayActivity;
import com.muslimlife.app.data.model.UserProfile;
import com.muslimlife.app.databinding.FragmentMlcoinBillingBinding;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.viewmodel.main.MainViewModel;

public class MLCoinBillingNewFragment extends BaseFragment {

    @Inject
    WebPayRepository webPayRepository;

    private FragmentMlcoinBillingBinding binding;

    private String selectedProductId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentMlcoinBillingBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);

        initView();
        initObserve();

        return binding.getRoot();
    }

    void initView(){
        binding.backButton.setOnClickListener(view ->
                Navigation.findNavController(requireView()).popBackStack());

        binding.mlc5.setText("5 MLC");
        binding.mlc10.setText("10 MLC");
        binding.mlc25.setText("25 MLC");
        binding.mlc100.setText("100 MLC");

        coinViewClick(binding.mlc10, MLCoinBillingModule.Companion.getPRODUCT_MLC_10());

        binding.mlc5.setOnClickListener(view -> coinViewClick(binding.mlc5, MLCoinBillingModule.Companion.getPRODUCT_MLC_5()));
        binding.mlc10.setOnClickListener(view -> coinViewClick(binding.mlc10, MLCoinBillingModule.Companion.getPRODUCT_MLC_10()));
        binding.mlc25.setOnClickListener(view -> coinViewClick(binding.mlc25, MLCoinBillingModule.Companion.getPRODUCT_MLC_25()));
        binding.mlc100.setOnClickListener(view -> coinViewClick(binding.mlc100, MLCoinBillingModule.Companion.getPRODUCT_MLC_100()));
    }

    private void coinViewClick(SelectCoinView coinView, String productId){
        binding.mlc5.setSelected(false);
        binding.mlc10.setSelected(false);
        binding.mlc25.setSelected(false);
        binding.mlc100.setSelected(false);

        selectedProductId = productId;
        coinView.setSelected(true);
    }

    private void initObserve() {
        webPayRepository.getMlCoinsLiveData().observe(getViewLifecycleOwner(), this::init);
    }

    void init(int count) {
        binding.coinCount.setText(String.valueOf(count));

        binding.buy.setOnClickListener(view -> {
            ((MainActivity) requireActivity()).startBuyCoin(selectedProductId);
        });
    }

    @Override
    public void showError(String error) {

    }
}
