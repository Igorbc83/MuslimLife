package com.muslimlife.app.view.pushSettings;

import static com.muslimlife.app.utils.Constants.SOUND_1;
import static com.muslimlife.app.utils.Constants.SOUND_2;
import static com.muslimlife.app.utils.Constants.SOUND_3;
import static com.muslimlife.app.utils.Constants.SOUND_4;
import static com.muslimlife.app.utils.Constants.SOUND_5;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.ItemNotificationSettingsBinding;


public class PushAdapter extends RecyclerView.Adapter<PushAdapter.ViewHolder> {

    int[] sounds;
    int choosedPos=0, currentAudio=-1;
    MediaPlayer mediaPlayer;
    Context context;

    public PushAdapter(Context context, int[] sounds, int selectedSound){
        this.sounds=sounds;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes
                        .Builder()
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build());
        this.context=context;
        this.choosedPos = selectedSound;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationSettingsBinding binding = ItemNotificationSettingsBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.title.setText(holder.itemView.getContext().getString(R.string.variant,position+1));
        holder.binding.choosedRb.setChecked(choosedPos==position);
        holder.binding.choosedRb.setOnClickListener(v->chooseSound(position));
        holder.binding.title.setOnClickListener(v->chooseSound(position));
//        holder.itemView.setOnClickListener(v->chooseSound(position));
        holder.binding.icon.setOnClickListener(v->{
            if(mediaPlayer.isPlaying())
                stopAudio(holder);
            else
                startAudio(holder, position);
        });
        if(currentAudio==position)
            holder.binding.icon.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.ic_pause, null));
        else
            holder.binding.icon.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.ic_play, null));
    }

    private void stopAudio(ViewHolder holder){
        mediaPlayer.pause();
        mediaPlayer.release();
        mediaPlayer = new MediaPlayer();
        holder.binding.icon.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.ic_play, null));
        currentAudio=-1;
        notifyDataSetChanged();
    }

    private void startAudio(ViewHolder holder, int position){
        currentAudio=position;
        mediaPlayer = MediaPlayer.create(context, sounds[position]);
        mediaPlayer.start();
        holder.binding.icon.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.drawable.ic_pause, null));
        notifyDataSetChanged();
    }

    public void chooseSound(int position){
        choosedPos = position;
        notifyDataSetChanged();
    }

    public int getChoosedSound(){
        return choosedPos;
    }

    public String getChoosedSoundName(){
        switch (choosedPos){
            case 1:
                return SOUND_2;
            case 2:
                return SOUND_3;
            case 3:
                return SOUND_4;
            case 4:
                return SOUND_5;
            default:
                return SOUND_1;
        }
    }
    @Override
    public int getItemCount() {
        return sounds.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemNotificationSettingsBinding binding;

        public ViewHolder(@NonNull ItemNotificationSettingsBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
