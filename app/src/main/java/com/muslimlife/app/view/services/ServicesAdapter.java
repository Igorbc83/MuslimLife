package com.muslimlife.app.view.services;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.muslimlife.app.R;
import com.muslimlife.app.data.model.OtherEntity;
import com.muslimlife.app.databinding.ItemServicesSwitchBinding;


public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    List<OtherEntity> services;
    Drawable bg;
    boolean type;
    SharedPreferences prefs;

    public ServicesAdapter(SharedPreferences prefs,  List<OtherEntity> services, boolean type) {
        this.type=type;
        this.prefs = prefs;
        this.services = services;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemServicesSwitchBinding binding =ItemServicesSwitchBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        if(services.get(position).isActive() == type){
            holder.icon_v.setImageResource(services.get(position).getDrawable());

            bg = ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.lineral_bg);
            bg.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(),services.get(position).getBackgroundColor()), PorterDuff.Mode.SRC_IN);

            holder.icon_v.setBackground(bg);
            holder.text_v.setText(services.get(position).getText());

            holder.switch_v.setChecked(type);

            holder.switch_v.setOnCheckedChangeListener((buttonView, isChecked) -> {
                services.get(position).setActive(isChecked);
                save();
            });
        }else{
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    public void save(){
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < services.size(); i++) {
            str.append(services.get(i).isActive()).append(",");
        }
        prefs.edit().putString("muslimlife.Services", str.toString()).apply();
    }



    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        ImageView icon_v;
        TextView text_v;
        SwitchCompat switch_v;

        public ViewHolder(ItemServicesSwitchBinding binding) {
            super(binding.getRoot());
            icon_v = binding.settingsSwitcherIcon;
            text_v = binding.settingsSwitcherText;
            switch_v = binding.settingsSwitch;
        }
    }
}
