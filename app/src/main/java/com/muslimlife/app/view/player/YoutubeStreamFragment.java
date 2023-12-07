package com.muslimlife.app.view.player;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.viewmodel.main.MainViewModel;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentYoutubeStreamBinding;

public class YoutubeStreamFragment extends BaseFragment {

    private FragmentYoutubeStreamBinding binding;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private MainViewModel viewModel;

    SharedPreferences sharedPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding =  FragmentYoutubeStreamBinding.inflate(inflater, container, false);

        initViewModel();
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);
        //binding.backButton.setOnClickListener(onClickListener);

        getLifecycle().addObserver(binding.youtubePlayerView);

        if(getArguments() != null) {
            String youtubeId = getArguments().getString("youtube_id");

            if(youtubeId != null && !youtubeId.isEmpty()) {
                binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.loadVideo(youtubeId, 0);
                    }
                });
            }
        }


        return binding.getRoot();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel.class);
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
