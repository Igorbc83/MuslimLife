package com.muslimlife.app.view.radio;

import static com.muslimlife.app.utils.Constants.MENU_OFF;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.muslimlife.app.data.model.RadioRes;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.utils.ServicesUtil;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.viewmodel.main.MainViewModel;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentRadioBinding;

public class RadioFragment extends BaseFragment implements RadioListener{

    private FragmentRadioBinding binding;
    RadioAdapter radioAdapter;

    @Inject
    UserRepository userRepository;

    private boolean playPause;
    private MediaPlayer mediaPlayer;
    private boolean initialStage = true;

    Player at;

    RadioRes selectedRadio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRadioBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(MENU_OFF);

        ServicesUtil.updateScore(getActivity().getPreferences(Context.MODE_PRIVATE), Constants.SERVICE_RADIO);

        binding.backButton.setOnClickListener(view->{
            Navigation.findNavController(requireView()).popBackStack();
        });

        mediaPlayer = new MediaPlayer();

        mediaPlayer.setAudioAttributes(
                new AudioAttributes
                        .Builder()
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build());

        binding.player.xButton.setOnClickListener(v -> {
            binding.player.getRoot().setVisibility(View.GONE);
            mediaPlayer.stop();
        });

        binding.player.playButton.setOnClickListener(v -> {
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                binding.player.playButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play, null));
            }else{
                mediaPlayer.start();
                binding.player.playButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause, null));
            }
        });

        getRadio(userRepository.isSubscribe());

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    @Override
    public void selectRadio(RadioRes radio) {
        selectedRadio = radio;

        try {
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer();

            Glide.with(this)
                    .load(radio.getImage())
                    .into(binding.player.radioIcon);

            binding.player.playButton.setVisibility(View.INVISIBLE);
            binding.player.getRoot().setVisibility(View.VISIBLE);
            binding.player.title.setText(getString(R.string.loading));

            mediaPlayer.setAudioAttributes(
                    new AudioAttributes
                            .Builder()
                            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                            .build());

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mp.reset();
                    return false;
                }
            });

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    Log.d("RADIO_TEST", "setOnPreparedListener");
                    mp.start();

                    binding.player.title.setText(radio.getName());
                    binding.player.playButton.setVisibility(View.VISIBLE);

                }
            });

            mediaPlayer.setDataSource(radio.getLink());
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Player extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                    initialStage = true;
                    playPause = false;
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                });

                mediaPlayer.prepare();
                prepared = true;

            } catch (Exception e) {
                //Log.e("MyAudioStreamingApp", e.getMessage());
                prepared = false;
            }

            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            binding.player.title.setText("test");

            mediaPlayer.start();
            initialStage = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            binding.player.getRoot().setVisibility(View.VISIBLE);
            binding.player.title.setText(getString(R.string.loading));
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            //at.execute(selectedRadio.getLink());
        }
    }

    private void getRadio(boolean isSub){
        userRepository.getRadio().subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull RadioRes[] radioRes) {
                setAdapter(radioRes, isSub);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    private void setAdapter(RadioRes[] radioRes, boolean isSub){
        /*if(isSub)
            radioAdapter=new RadioAdapter(radioRes, binding, this);
        else{
            RadioRes[] radioList = new RadioRes[]{radioRes[0]};
            radioAdapter=new RadioAdapter(radioList, binding, this);
        }*/
        radioAdapter=new RadioAdapter(radioRes, binding, this);

        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.radioRv.setLayoutManager(llm);
        binding.radioRv.setAdapter(radioAdapter);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }
}
