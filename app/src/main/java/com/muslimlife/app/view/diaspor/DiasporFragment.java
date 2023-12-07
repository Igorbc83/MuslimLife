package com.muslimlife.app.view.diaspor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.muslimlife.app.data.model.DiaspoarResponce;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.viewmodel.main.MainViewModel;

import javax.inject.Inject;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentDiasporBinding;

public class DiasporFragment extends BaseFragment {

    private FragmentDiasporBinding binding;

    @Inject
    UserRepository userRepository;

    DiaspoarResponce diaspoarResponce;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private MainViewModel viewModel;

    private boolean subState = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDiasporBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);

        initViewModel();
        initUserProfileObserve();

        binding.backButton.setOnClickListener(onClickListener);

        binding.callDiaspoar.setOnClickListener(onClickListener);
        binding.diaspoarInstagram.setOnClickListener(onClickListener);
        binding.diaspoarVk.setOnClickListener(onClickListener);
        binding.diaspoarTelegram.setOnClickListener(onClickListener);
        binding.diaspoarWhatsApp.setOnClickListener(onClickListener);
        binding.diaspoarSendMessageButton.setOnClickListener(onClickListener);

        diaspoarResponce = userRepository.getDiaspoarResponce();

        binding.diaspoarName.setText(diaspoarResponce.getName());
        Glide.with(requireContext()).load(diaspoarResponce.getImam_avatar()).into(binding.avatarIv);
        binding.diaspoarInfoName.setText(diaspoarResponce.getImam_name()+" "+ diaspoarResponce.getImam_lastname());
        binding.diaspoarBiography.setText(diaspoarResponce.getImam_bio());
        String phone = diaspoarResponce.getImam_phone();

        if(phone != null && phone.length()>11)
            binding.diaspoarPhoneNmber.setText(phone.substring(0,2) +" ("+phone.substring(2,5)+") "+phone.substring(5,8)+"-"+phone.substring(8,10)+"-"+phone.substring(10,12));

        if(diaspoarResponce.getVk()==null)
            binding.diaspoarVk.setVisibility(View.GONE);

        if(diaspoarResponce.getInstagram()==null)
            binding.diaspoarInstagram.setVisibility(View.GONE);

        if (binding.diaspoarBiography.getLineCount()>binding.diaspoarBiography.getMaxLines())
            binding.diaspoarAllInfo.setVisibility(View.GONE);
        else
            binding.diaspoarAllInfo.setOnClickListener(v->{
                binding.diaspoarBiography.setMaxLines(Integer.MAX_VALUE);
                binding.diaspoarAllInfo.setVisibility(View.GONE);
            });

        return binding.getRoot();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel.class);
    }

    private void initUserProfileObserve() {
        viewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), userProfile -> {
            subState = userProfile.isSubscribed();
        });
    }

    View.OnClickListener onClickListener = v -> {
        Intent intent;
        switch (v.getId()){
            case R.id.back_button:
                Navigation.findNavController(requireView()).popBackStack();
                break;
            case R.id.call_diaspoar:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + binding.diaspoarPhoneNmber.getText().toString()));
                startActivity(intent);
                break;
            case R.id.diaspoar_instagram:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(diaspoarResponce.getInstagram()));
                startActivity(intent);
                break;
            case R.id.diaspoar_vk:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(diaspoarResponce.getVk()));
                startActivity(intent);
                break;
            case R.id.diaspoar_telegram:
                //TODO: получить с бэка ссылку на телеграм диаспоры
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me"));
                startActivity(intent);
                break;
            case R.id.diaspoar_whats_app:
                //TODO: получить с бэка ссылку на whatsapp диаспоры
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.whatsapp.com/?lang=ru"));
                startActivity(intent);
                break;
            case R.id.diaspoar_send_message_button:
                if(subState){
                    SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
                    String id = sharedPref.getString(Constants.SP_KEY_USER_EMAIL, "");
                    if(id.equals("")){
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constants.OPEN_AUTH, false);
                        Navigation.findNavController(requireView()).navigate(R.id.newAuthFragment, bundle);
                        return;
                    }
                    NavDirections action = DiasporFragmentDirections.diasporFragmentToDiaspoarQuastionFragment();
                    Navigation.findNavController(requireView()).navigate(action);
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
                    alertDialog.setTitle(getString(R.string.sub));
                    alertDialog.setMessage(getString(R.string.alert_sub_desc));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.sub),
                            (dialog, which) ->
                                    Navigation.findNavController(requireView()).navigate(R.id.subRuStoreFragment)
                    );
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                            (dialog, which) ->
                                    dialog.dismiss()
                    );
                    alertDialog.show();
                }

                break;
        }
    };


    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }
}
