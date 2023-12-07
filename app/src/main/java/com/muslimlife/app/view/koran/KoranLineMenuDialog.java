package com.muslimlife.app.view.koran;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.muslimlife.app.data.model.ReadersRes;
import com.muslimlife.app.view.koran.listeners.KoranPageListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.muslimlife.app.databinding.DialogLineKoranMenuBinding;

public class KoranLineMenuDialog extends DialogFragment {

    private DialogLineKoranMenuBinding binding;

    KoranPageListener listener;

    ReadersRes reader;

    boolean subState;

    public KoranLineMenuDialog(KoranPageListener listener, ReadersRes reader, boolean subState){
        this.listener = listener;
        this.reader = reader;
        this.subState = subState;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogLineKoranMenuBinding.inflate(inflater, container, false);

        setViewClickListeners();

        if(reader != null) {
            if(reader.getAvatar() != null)
                Glide.with(requireContext()).load(reader.getAvatar()).into(binding.readerAvatar);
        }

        return binding.getRoot();
    }

    private void setViewClickListeners() {
        binding.linePlay.setOnClickListener(v -> {
            if(subState) {
                listener.linePlay();
                dismiss();
            }else{
                listener.showSubDialog();
            }
        });

        binding.lineTranslate.setOnClickListener(v -> {
            listener.lineTranslate();
            dismiss();
        });

        binding.lineShare.setOnClickListener(v -> {
            listener.lineShare();
            dismiss();
        });

        binding.readerAvatar.setOnClickListener(v -> {
            listener.selectReader();
            dismiss();
        });

        if(subState) {
            binding.readerAvatarView.setVisibility(View.VISIBLE);
        }else{
            binding.readerAvatarView.setVisibility(View.GONE);
        }
    }
}
