package com.muslimlife.app.adapters.holders;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.ItemMigrantsBinding;


public class MigrantsHolder extends RecyclerView.ViewHolder{

    ItemMigrantsBinding binding;

    public MigrantsHolder(@NonNull ItemMigrantsBinding binding) {
        super(binding.getRoot());

        this.binding = binding;
    }

    public void initMigrants(){
        binding.mainTitle.title.setText(binding.getRoot().getResources().getString(R.string.migrants_title));
        binding.mainTitle.openAll.setVisibility(View.INVISIBLE);

        Shader shader = new LinearGradient(0,0,0,binding.mainTitle.title.getLineHeight(),
                binding.getRoot().getContext().getColor(R.color.gradStart),
                binding.getRoot().getContext().getColor(R.color.gradEnd),
                Shader.TileMode.REPEAT);
        binding.mainTitle.title.getPaint().setShader(shader);
    }

}