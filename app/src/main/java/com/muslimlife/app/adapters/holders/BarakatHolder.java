package com.muslimlife.app.adapters.holders;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.ItemBarakatBinding;

public class BarakatHolder extends RecyclerView.ViewHolder{

    ItemBarakatBinding binding;

    public BarakatHolder(@NonNull ItemBarakatBinding binding) {
        super(binding.getRoot());

        this.binding = binding;
    }

    public void initBarakat(){
        binding.mainTitle.title.setText(binding.getRoot().getResources().getString(R.string.barakat));
        binding.mainTitle.openAll.setVisibility(View.INVISIBLE);

        Shader shader = new LinearGradient(0,0,0,binding.mainTitle.title.getLineHeight(),
                binding.getRoot().getContext().getColor(R.color.gradStart),
                binding.getRoot().getContext().getColor(R.color.gradEnd),
                Shader.TileMode.REPEAT);
        binding.mainTitle.title.getPaint().setShader(shader);
    }

}

