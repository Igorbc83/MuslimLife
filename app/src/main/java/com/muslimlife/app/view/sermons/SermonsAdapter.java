package com.muslimlife.app.view.sermons;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.muslimlife.app.data.model.parameters.SermonsResponse;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentSermonsBinding;
import com.muslimlife.app.databinding.ItemSermonsBinding;
import com.muslimlife.app.view.YoutubeStreamActivity;


public class SermonsAdapter extends RecyclerView.Adapter<SermonsAdapter.ViewHolder>{

    private final List<SermonsResponse> sermonsResponses;
    private MediaPlayer mediaPlayer;
    FragmentSermonsBinding fragmentBinding;
    private String currentAudio;
    private String currentAudioTitle;

    private int currentPlay = -1;
    private int lastPlay = -1;

    public SermonsAdapter(List<SermonsResponse> sermonsResponses, FragmentSermonsBinding binding) {
        this.sermonsResponses = sermonsResponses;
        /*mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes
                        .Builder()
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build());*/
        initPlayer();
        fragmentBinding = binding;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemSermonsBinding binding =ItemSermonsBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        hideAudio(holder);
        hideVideo(holder);
        hideText(holder);

        switch (sermonsResponses.get(position).type){
            case 1:
                // text
                showText(position, holder);
                holder.binding.breaker.setVisibility(View.GONE);
                break;
            case 2:
                // image
                showImage(position, holder);
                holder.binding.breaker.setVisibility(View.GONE);
                break;
            case 3:
                // audio
                holder.binding.breaker.setVisibility(View.GONE);
                showAudio(position, holder);
                break;
            case 4:
                // video
                showVideo(position, holder);
                holder.binding.breaker.setVisibility(View.GONE);
                break;
            case 5:
                // text + audio
                showText(position, holder);
                showAudio(position, holder);
                break;
            case 6:
                // audio + video
                showVideo(position, holder);
                showAudio(position, holder);
                break;
            case 7:
                // text + video
                showText(position, holder);
                showVideo(position, holder);
                holder.binding.breaker.setVisibility(View.GONE);
                break;
            case 8:
                // text + audio + video
                showText(position, holder);
                showVideo(position, holder);
                showAudio(position, holder);
                break;
        }

        holder.binding.textBlockShowAll.setOnClickListener(v->{
            holder.binding.videoBlockText.setMaxLines(Integer.MAX_VALUE);
            holder.binding.textBlockShowAll.setVisibility(View.GONE);
        } );

        if(currentPlay != position){
            holder.binding.audioBlockPlay.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.ic_play, null));
        }

        holder.binding.videoBlockImageView.setOnClickListener(v->{
            if(sermonsResponses.get(position).video!=null){
                String url = sermonsResponses.get(position).video;
                if(url.contains("youtube")){
                    Intent intent = new Intent(holder.itemView.getContext(), YoutubeStreamActivity.class);
                    intent.putExtra("youtube_id", url.split("v=")[1]);

                    holder.itemView.getContext().startActivity(intent);
                }else if(url.contains("youtu.be")){
                    Intent intent = new Intent(holder.itemView.getContext(), YoutubeStreamActivity.class);
                    String[] list = url.split("/");
                    intent.putExtra("youtube_id", list[list.length - 1]);

                    holder.itemView.getContext().startActivity(intent);
                }
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sermonsResponses.get(position).video));
                //holder.itemView.getContext().startActivity(intent);
            }

        });

        holder.binding.audioBlockPlay.setOnClickListener(v->{
            if(mediaPlayer.isPlaying() && currentPlay == position)
                stopAudio(holder);
            else{
                stopAudio(holder);
                startAudio(holder, position);
            }
            /*else if(mediaPlayer.isPlaying() && currentPlay != position) {
                stopAudio(holder);
                startAudio(holder, position);
            }else if(!mediaPlayer.isPlaying() && currentPlay == position){
                startAudio(holder, position);
            }else if(!mediaPlayer.isPlaying() && currentPlay != position){
                startAudio(holder, position);
            }*/
        });
    }

    private void stopAudio(ViewHolder holder){
       mediaPlayer.stop();
       mediaPlayer.release();
       //mediaPlayer = new MediaPlayer();
        initPlayer();
       holder.binding.audioBlockPlay.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.ic_play, null));
       fragmentBinding.player.playButton.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.ic_play, null));
       notifyItemChanged(lastPlay);
    }

    public void stop(){
        mediaPlayer.pause();
        //mediaPlayer.release();
        //mediaPlayer = new MediaPlayer();
        initPlayer();
    }

    private void initPlayer(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes
                        .Builder()
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build());
    }

    private void startAudio(ViewHolder holder, int position) {
        try {
            if(currentPlay == -1 && lastPlay == -1){
                currentPlay = position;
                lastPlay = position;
            }else{
                lastPlay = currentPlay;
                currentPlay = position;
            }

            if (position > -1) {
                currentAudio = sermonsResponses.get(position).audio;
                currentAudioTitle = sermonsResponses.get(position).audio_title;
            }

            fragmentBinding.player.playButton.setOnClickListener(v->{
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    fragmentBinding.player.playButton.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.ic_play, null));
                }else {
                    mediaPlayer.start();
                    fragmentBinding.player.playButton.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.ic_pause, null));
                }
            });

            fragmentBinding.player.xButton.setOnClickListener(v->{
                fragmentBinding.player.getRoot().setVisibility(View.GONE);
                if(mediaPlayer.isPlaying()) {
                    stopAudio(holder);
                    notifyItemChanged(currentPlay);
                }
            });

            fragmentBinding.player.playButton.setEnabled(false);
            mediaPlayer.setDataSource(currentAudio);

            /*mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    observer.stop();
                    fragmentBinding.player.playerBar.setProgress(mp.getCurrentPosition());
                    // TODO Auto-generated method stub
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            });*/

            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    fragmentBinding.player.playerBar.setSecondaryProgress((mediaPlayer.getDuration() * percent) / 100);
                }
            });

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    fragmentBinding.player.playButton.setEnabled(true);
                }
            });

            observer = new MediaObserver();
            mediaPlayer.prepare();
            mediaPlayer.start();

            holder.binding.audioBlockPlay.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.ic_pause, null));
            notifyItemChanged(lastPlay);
            fragmentBinding.player.playButton.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.ic_pause, null));
            fragmentBinding.player.getRoot().setVisibility(View.VISIBLE);
            fragmentBinding.player.playerBar.setMax(mediaPlayer.getDuration());
            //fragmentBinding.player.title.setText(currentAudioTitle);

            fragmentBinding.player.playerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                    if (isFromUser) {
                        mediaPlayer.seekTo(progress);
                        seekBar.setProgress(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            new Thread(observer).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private MediaObserver observer = null;

    private class MediaObserver implements Runnable {
        private AtomicBoolean stop = new AtomicBoolean(false);

        public void stop() {
            stop.set(true);
        }

        @Override
        public void run() {
            while (!stop.get()) {
                try {
                    fragmentBinding.player.playerBar.setProgress(mediaPlayer.getCurrentPosition());
                    Thread.sleep(200);
                } catch (Exception ex) {
                    String s = ex.getMessage();
                }

            }
        }
    }

    private void hideVideo(ViewHolder holder){
        holder.binding.imageBlock.setVisibility(View.GONE);
    }

    private void hideText(ViewHolder holder){
        holder.binding.textBlockShowAll.setVisibility(View.GONE);
        holder.binding.videoBlockTitel.setVisibility(View.GONE);
        holder.binding.videoBlockText.setVisibility(View.GONE);
    }

    private void hideAudio(ViewHolder holder){
        holder.binding.audioBlock.setVisibility(View.GONE);
    }

    private void showImage(int position, ViewHolder holder){
        Glide.with(holder.itemView.getContext()).load(sermonsResponses.get(position).image).into(holder.binding.videoBlockImageView);
        holder.binding.imageBlock.setVisibility(View.VISIBLE);

    }

    private void showVideo(int position, ViewHolder holder){
        showImage(position, holder);
        holder.binding.videoBlockTime.setText(sermonsResponses.get(position).time_video);
        holder.binding.videoBlockTime.setVisibility(View.VISIBLE);
    }

    private void showText(int position, ViewHolder holder){
        holder.binding.videoBlockTitel.setText(sermonsResponses.get(position).title);
        holder.binding.videoBlockTitel.setVisibility(View.VISIBLE);
        holder.binding.videoBlockText.setText(Html.fromHtml(sermonsResponses.get(position).text,Html.FROM_HTML_MODE_LEGACY));
        holder.binding.videoBlockText.setVisibility(View.VISIBLE);
        holder.binding.textBlockShowAll.setVisibility(View.VISIBLE);
        showAll(holder);
    }

    private void showAudio(int position, ViewHolder holder){
        holder.binding.audioBlock.setVisibility(View.VISIBLE);
        holder.binding.audioBlockTitel.setText(sermonsResponses.get(position).audio_title);
        holder.binding.audioBlockText.setText(sermonsResponses.get(position).audio_text);
        holder.binding.audioBlockTime.setText(sermonsResponses.get(position).time_audio);
        if(mediaPlayer.isPlaying())
            holder.binding.audioBlockPlay.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.ic_pause, null));
        else
            holder.binding.audioBlockPlay.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.ic_play, null));

    }

    private void showAll(ViewHolder holder){
        holder.binding.videoBlockText.post(() -> {
            if(holder.binding.videoBlockText.getLineCount()<holder.binding.videoBlockText.getMaxLines())
                holder.binding.textBlockShowAll.setVisibility(View.GONE);
        });
    }

    @Override
    public int getItemCount() {
        return sermonsResponses.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder{

        ItemSermonsBinding binding;

        public ViewHolder(ItemSermonsBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
