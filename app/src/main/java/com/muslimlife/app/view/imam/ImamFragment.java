package com.muslimlife.app.view.imam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.muslimlife.app.data.model.ImamResMain;
import com.muslimlife.app.data.model.QuastionTypeRes;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.viewmodel.main.MainViewModel;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentImamBinding;

public class ImamFragment extends BaseFragment implements MediaPlayer.OnCompletionListener {

    private FragmentImamBinding binding;
    private String imamId;
    private ImamResMain imam;

    @Inject
    UserRepository userRepository;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private MainViewModel viewModel;

    private boolean subState = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentImamBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);

        initViewModel();
        initUserProfileObserve();

        binding.backButton.setOnClickListener(onClickListener);
        binding.imamQuastionSendButton.setOnClickListener(onClickListener);

        imamId = requireArguments().getString(Constants.IMAM_ID);

        getImam();

        binding.playerView.setVideoURI(Uri.parse("http://37.140.241.233:8080/muslimlifeapi/video/ifa.mp4"));
        binding.playerView.setOnCompletionListener(this);

        binding.play.setOnClickListener(view -> {
            binding.play.setVisibility(View.GONE);
            binding.playerView.start();
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
        switch (v.getId()){
            case R.id.back_button:
                Navigation.findNavController(requireView()).popBackStack();
                break;
            case R.id.imam_quastion_send_button:

                if(subState){
                    SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
                    String id = sharedPref.getString(Constants.SP_KEY_USER_EMAIL, "");
                    if(id.equals("")){
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constants.OPEN_AUTH, false);
                        Navigation.findNavController(requireView()).navigate(R.id.newAuthFragment, bundle);
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.IMAM_ID, imam.getId());
                    bundle.putStringArrayList(Constants.IMAM_QUASTIONS, new ArrayList<>(Arrays.asList(imam.getTypes())));
                    ((MainActivity)requireActivity()).navController.navigate(R.id.ImamQuastionFragment,bundle);
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

    private void getImam(){
        userRepository.getImam(imamId).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull ImamResMain[] imamResMains) {
                requireActivity().runOnUiThread(()-> setVisual(imamResMains[0]));
                imam = imamResMains[0];
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    private void setVisual(ImamResMain imamResMain){
        Glide.with(requireContext()).load(imamResMain.getAvatar()).into(binding.imamIcon);
        binding.imamName.setText(imamResMain.getFirst_name()+" "+imamResMain.getLast_name());

        for (int i=0; i<imamResMain.getLanguages().length;i++)
            if(i+1!=imamResMain.getLanguages().length)
                binding.imamLanguage.setText(binding.imamLanguage.getText()+""+imamResMain.getLanguages()[i]+", ");
            else
                binding.imamLanguage.setText(binding.imamLanguage.getText()+""+imamResMain.getLanguages()[i]);

        for (int i=0; i<imamResMain.getTypes().length;i++)
            for(QuastionTypeRes type:userRepository.getQuastionTypeRes())
                if(i+1!=imamResMain.getTypes().length && imamResMain.getTypes()[i].equals(type.getId()))
                    binding.imamKind.setText(binding.imamKind.getText()+""+type.getName()+" • ");
                else if(i+1==imamResMain.getTypes().length && imamResMain.getTypes()[i].equals(type.getId()))
                    binding.imamKind.setText(binding.imamKind.getText()+""+type.getName());

        binding.imamBiography.setText(imamResMain.getBio());
        binding.imamBiography.setMaxLines(Integer.MAX_VALUE);

        /*if(binding.imamBiography.getLineCount()<binding.imamBiography.getMaxLines())
            binding.imamAllInfo.setVisibility(View.VISIBLE);
        else
            binding.imamAllInfo.setVisibility(View.GONE);

        binding.imamAllInfo.setOnClickListener(v->{
            binding.imamBiography.setMaxLines(Integer.MAX_VALUE);
            binding.imamAllInfo.setVisibility(View.GONE);
        });*/
    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        binding.play.setVisibility(View.VISIBLE);
    }
}
