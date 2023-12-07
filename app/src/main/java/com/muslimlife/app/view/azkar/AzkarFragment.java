package com.muslimlife.app.view.azkar;

import static com.muslimlife.app.utils.Constants.MENU_OFF;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.Navigation;

import javax.inject.Inject;

import com.muslimlife.app.R;
import com.muslimlife.app.data.model.azkars.AzkarModel;
import com.muslimlife.app.data.model.surah.AyahRes;
import com.muslimlife.app.data.repository.AzkarsRepository;
import com.muslimlife.app.databinding.FragmentAzkarBinding;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

public class AzkarFragment extends BaseFragment {

    private FragmentAzkarBinding binding;

    @Inject
    AzkarsRepository azkarsRepository;

    MediaPlayer mediaPlayer;
    Thread mediaThread = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAzkarBinding.inflate(inflater, container, false);
        ((MainActivity) requireActivity()).setMenu(MENU_OFF);

        mediaPlayer = new MediaPlayer();

        initView();

        return binding.getRoot();
    }

    private void initView(){
        binding.close.setOnClickListener(v-> Navigation.findNavController(requireView()).popBackStack());

        Shader shader = new LinearGradient(0,0,0,binding.azkarArabicText.getLineHeight(),
                binding.getRoot().getContext().getColor(R.color.azkarGradientStart),
                binding.getRoot().getContext().getColor(R.color.azkarGradientEnd),
                Shader.TileMode.REPEAT);
        binding.azkarArabicText.getPaint().setShader(shader);

        AzkarModel azkar = azkarsRepository
                .getAzkarsByType(requireArguments().getInt(Constants.BUNDLE_AZKAR))
                .get(requireArguments().getInt(Constants.BUNDLE_AZKAR_POSITION));

        if(azkar.getAzkarText() != null)
            binding.azkarText.setText(azkar.getAzkarText());

        if(azkar.getAzkarArabic() != null)
            binding.azkarArabicText.setText(azkar.getAzkarArabic());

        if(azkar.getAzkarTranscription() != null)
            binding.azkarTranscriptionText.setText(azkar.getAzkarTranscription());

        if(azkar.getAzkarAudio() != null && !azkar.getAzkarAudio().isEmpty()){
            binding.play.setVisibility(View.VISIBLE);

            binding.play.setOnClickListener(view -> {
                if(mediaPlayer != null){
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        binding.play.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_white, null));
                    }else{
                        linePlay(azkar.getAzkarAudio());
                        binding.play.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause, null));
                    }
                }
            });

            binding.progress.setOnClickListener(view -> {});
        }
    }

    public void linePlay(String audioUrl) {
        binding.progress.setVisibility(View.VISIBLE);

        mediaThread = new Thread(() -> {
            try {
                if (mediaPlayer != null)
                    mediaPlayer.stop();

                showPlayerProgress(true);

                mediaPlayer = new MediaPlayer();

                mediaPlayer.setAudioAttributes(
                        new AudioAttributes
                                .Builder()
                                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                                .build());

                mediaPlayer.setOnPreparedListener(mediaPlayer ->
                        showPlayerProgress(false));

                mediaPlayer.setDataSource(audioUrl);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception ex) {
            }
        });
        mediaThread.start();
    }

    private void showPlayerProgress(boolean show) {
        requireActivity().runOnUiThread(() ->
                binding.progress.setVisibility(show ? View.VISIBLE : View.GONE));
    }

    @Override
    public void onPause() {
        if(mediaPlayer != null)
            mediaPlayer.stop();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        if(mediaPlayer != null)
            mediaPlayer.stop();

        if(mediaThread != null && mediaThread.isAlive())
            mediaThread.interrupt();

        super.onDestroy();
    }

    @Override
    public void showError(String error) {

    }
}
