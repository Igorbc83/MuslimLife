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
import com.muslimlife.app.databinding.ItemMainRecyclerBinding;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.main.listeners.MainRecyclerListener;

public class MainHolder extends RecyclerView.ViewHolder{

    ItemMainRecyclerBinding binding;

    public MainHolder(@NonNull ItemMainRecyclerBinding binding) {
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

        switch (model.getType()) {
            case Constants.RECYCLER_MAIN_WORSHIP_ITEM:
                binding.mainTitle.openAll.setOnClickListener(v -> listener.launchBottomDialog(Constants.MAIN_BOTTOM_DIALOG_WORSHIP));
                break;
            case Constants.RECYCLER_MAIN_OTHER_ITEM:
                binding.mainTitle.title.setText(binding.getRoot().getContext().getString(R.string.other));
                binding.mainTitle.openAll.setOnClickListener(v -> listener.launchBottomDialog(Constants.MAIN_BOTTOM_DIALOG_OTHER));
                break;
            case Constants.RECYCLER_MAIN_QUESTION_ITEM:
                binding.mainTitle.title.setText(binding.getRoot().getContext().getString(R.string.send_quastion));
                binding.mainTitle.openAll.setOnClickListener(v -> listener.selectWorship(R.id.QuastionFragment));
                break;
            case Constants.RECYCLER_MAIN_DIASPOARS_ITEM:
                binding.mainTitle.title.setText(binding.getRoot().getContext().getString(R.string.diaspoars));
                binding.mainTitle.openAll.setOnClickListener(v -> listener.selectWorship(R.id.DiaspoarsFragment));
                break;
            case Constants.RECYCLER_MAIN_PLACES_ITEM:
                binding.mainTitle.title.setText(binding.getRoot().getContext().getString(R.string.places));
                binding.mainTitle.openAll.setVisibility(View.GONE);
                break;
            case Constants.RECYCLER_MAIN_AZKARS_ITEM:
                binding.mainTitle.title.setText(binding.getRoot().getContext().getString(R.string.azkar_title));
                binding.mainTitle.openAll.setOnClickListener(v -> listener.launchBottomDialog(Constants.MAIN_BOTTOM_DIALOG_AZKARS));
                break;
        }
    }
}
