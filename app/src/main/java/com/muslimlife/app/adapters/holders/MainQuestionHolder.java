package com.muslimlife.app.adapters.holders;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muslimlife.app.R;
import com.muslimlife.app.adapters.MainAdapter;
import com.muslimlife.app.adapters.RecyclerModel;
import com.muslimlife.app.databinding.ItemMainQuestionBinding;
import com.muslimlife.app.view.main.listeners.MainRecyclerListener;

public class MainQuestionHolder extends RecyclerView.ViewHolder{

    ItemMainQuestionBinding binding;

    public MainQuestionHolder(@NonNull ItemMainQuestionBinding binding) {
        super(binding.getRoot());

        this.binding = binding;
    }

    public void initMainHolder(RecyclerModel model, MainRecyclerListener listener) {
        LinearLayoutManager manager = new LinearLayoutManager(binding.mainRecycler.getContext(), RecyclerView.HORIZONTAL, false);
        MainAdapter adapter = new MainAdapter(model.getModels(), listener);

        binding.mainRecycler.setLayoutManager(manager);
        binding.mainRecycler.setAdapter(adapter);

        Shader shader = new LinearGradient(0,0,0,binding.mainTitle.title.getLineHeight(),
                binding.getRoot().getContext().getColor(R.color.gradStart),
                binding.getRoot().getContext().getColor(R.color.gradEnd),
                Shader.TileMode.REPEAT);
        binding.mainTitle.title.getPaint().setShader(shader);

        binding.mainTitle.title.setText(binding.getRoot().getContext().getString(R.string.send_quastion));

        binding.mainTitle.view.setVisibility(View.GONE);
        binding.mainTitle.openAll.setVisibility(View.INVISIBLE);
    }
}
