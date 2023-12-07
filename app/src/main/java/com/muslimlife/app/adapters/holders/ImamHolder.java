package com.muslimlife.app.adapters.holders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.muslimlife.app.data.model.ImamResMain;
import com.muslimlife.app.databinding.ItemImamMainBinding;

public class ImamHolder extends RecyclerView.ViewHolder{

    ItemImamMainBinding binding;

    public ImamHolder(@NonNull ItemImamMainBinding binding) {
        super(binding.getRoot());

        this.binding = binding;
    }

    public void initImam(ImamResMain imamRes){
        Glide.with(binding.getRoot().getContext()).load(imamRes.getAvatar()).into(binding.avatarIv);
        binding.imamTv.setText(imamRes.getFirst_name()+" "+imamRes.getLast_name());
    }
}
