package com.muslimlife.app.view.sub;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.muslimlife.app.data.model.UserProfile;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.viewmodel.main.MainViewModel;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;

import com.muslimlife.app.data.model.parameters.LoadUserDataParameters;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentSettingsBinding;
import com.muslimlife.app.databinding.FragmentSubTypeBinding;

import com.muslimlife.app.view.base.BaseFragment;

public class SubFragment extends BaseFragment {

    private FragmentSubTypeBinding binding;

    @Inject
    UserRepository userRepository;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSubTypeBinding.inflate(inflater, container, false);
        binding.backButton.setOnClickListener(onClickListener);
        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);

        initViewModel();
        initUserProfileObserve();

        binding.premisions.setText(requireContext().getString(R.string.subText1)+"\n"+requireContext().getString(R.string.subText2)+"\n"+requireContext().getString(R.string.subText3));


        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        String id = sharedPref.getString(Constants.SP_KEY_USER_ID, "");
        if(!id.isEmpty())
            userRepository.getUserProfile(new LoadUserDataParameters(id) ).subscribe(new SingleObserver<UserProfile>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {}

                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onSuccess(@NonNull UserProfile userProfile) {
                    requireActivity().runOnUiThread(()->{
                        init(userProfile);
                    });
                }

                @Override
                public void onError(@NonNull Throwable e) {}
            });

        return binding.getRoot();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel.class);
    }

    private void initUserProfileObserve() {
        viewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), this::init);
    }
    private void init(UserProfile userProfile){
        binding.nameTv.setText(userProfile.getFullName());
        if(userProfile.getAvatar() == null || userProfile.getAvatar().isEmpty())
            binding.avatarIv.setImageDrawable(requireContext().getDrawable(R.drawable.ic_empty_photo));
        else
            Glide.with(requireContext()).load(userProfile.getAvatar()).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(binding.avatarIv);

        if(userProfile.isSubscribed()) {
            binding.supportSendButton.setBackgroundColor(requireContext().getColor(R.color.gray));
            binding.supportSendButton.setText(R.string.sub_cancle);
            binding.subStatus.setVisibility(View.GONE);

            //todo test hide sub logic
            binding.supportSendButton.setOnClickListener(v -> {
                try {
                    requireActivity().startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/account/subscriptions")));
                } catch (ActivityNotFoundException e) {
                    Log.e("NavigationError", e.getMessage());
                }
            });

            //test
            /*binding.supportSendButton.setOnClickListener(view -> {
                ((MainActivity) requireActivity()).setSubscribe(false);
            });*/
        }else{
            binding.supportSendButton.setBackgroundColor(requireContext().getColor(R.color.dark_blue));
            binding.supportSendButton.setText(R.string.subPrice);

            //todo test hide sub logic
            binding.supportSendButton.setOnClickListener(v->((MainActivity)requireActivity()).billingUtil.startPurchase());
            //test
            /*binding.supportSendButton.setOnClickListener(view -> {
                ((MainActivity) requireActivity()).setSubscribe(true);
            });*/
        }
    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    View.OnClickListener onClickListener = v->{
        switch (v.getId()){
            case R.id.back_button:
                Navigation.findNavController(requireView()).popBackStack();
                break;
        }
    };
}
