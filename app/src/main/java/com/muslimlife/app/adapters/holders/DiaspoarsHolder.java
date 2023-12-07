package com.muslimlife.app.adapters.holders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.muslimlife.app.data.model.DiaspoarResponce;
import com.muslimlife.app.databinding.ItemDiaspoarMainBinding;
import com.muslimlife.app.databinding.ItemImamMainBinding;

public class DiaspoarsHolder extends RecyclerView.ViewHolder{

    ItemDiaspoarMainBinding binding;

    public DiaspoarsHolder(@NonNull ItemDiaspoarMainBinding binding) {
        super(binding.getRoot());

        this.binding = binding;
    }

    public void initDiaspoar(DiaspoarResponce diaspoarResRes){
        Glide.with(binding.getRoot().getContext()).load(diaspoarResRes.getImage()).into(binding.avatarIv);
        binding.nameTv.setText(diaspoarResRes.getName());
    }

}
