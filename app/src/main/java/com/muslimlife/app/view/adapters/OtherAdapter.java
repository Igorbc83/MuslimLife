package com.muslimlife.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.muslimlife.app.R;
import com.muslimlife.app.data.model.OtherEntity;
import com.muslimlife.app.databinding.ItemOtherBinding;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.main.listeners.MainRecyclerListener;

public class OtherAdapter extends ArrayAdapter<OtherEntity> {

    private List<OtherEntity> otherEntities;

    Activity activity;

    MainRecyclerListener listener;

    private String dialogType;

    public OtherAdapter(String dialogType, Activity activity, int otherResourceId, List<OtherEntity> otherEntities, MainRecyclerListener listener) {
        super(activity, otherResourceId, otherEntities);
        this.activity = activity;
        this.otherEntities = otherEntities;
        this.listener = listener;
        this.dialogType = dialogType;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ItemOtherBinding binding = ItemOtherBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        OtherEntity other = otherEntities.get(position);

        binding.text.setText(binding.text.getResources().getString(other.getText()));

        binding.image
                .setImageDrawable(ResourcesCompat.getDrawable(
                        getContext().getResources(),
                        other.getDrawable(),
                        null));

        Drawable buttonDrawable = binding.bg.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        DrawableCompat.setTint(buttonDrawable, ContextCompat.getColor(getContext(), other.getBackgroundColor()));
        binding.bg.setBackground(buttonDrawable);

        if(dialogType.equals(Constants.MAIN_BOTTOM_DIALOG_AZKARS)){
            binding.sermonsItem.setOnClickListener(v -> {
                listener.selectAzkars(other.getType());
            });
        }else {
            if (other.getType() != -1) {
                binding.sermonsItem.setOnClickListener(v -> {
                    //if (position != 3)
                        listener.selectWorship(other.getType());
                    //else
                        //Toast.makeText(activity, getContext().getString(R.string.section_under_development), Toast.LENGTH_SHORT).show();
                });
            } else {
                binding.sermonsItem.setOnClickListener(v -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(other.getUrl()));
                    activity.startActivity(browserIntent);
                });
            }
        }

        return binding.getRoot();
    }

    public OtherEntity getItem(int position) {
        return otherEntities.get(position);
    }

}
