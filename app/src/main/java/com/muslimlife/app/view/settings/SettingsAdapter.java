package com.muslimlife.app.view.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.ItemServiceBinding;
import com.muslimlife.app.databinding.ItemSettingsBlock4RvBinding;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    private String[] titels;
    private int[] icons ={R.drawable.ic_message,R.drawable.ic_share,R.drawable.ic_donation,R.drawable.ic_language,R.drawable.ic_service,R.drawable.ic_support,R.drawable.ic_info};
    View view;
    private boolean status;

    public SettingsAdapter(View view, boolean status) {
        this.view = view;
        titels= new String[]{view.getContext().getString(R.string.messages), view.getContext().getString(R.string.shareApp),
                view.getContext().getString(R.string.donation), view.getContext().getString(R.string.appLanguage),
                view.getContext().getString(R.string.servicesMenu), view.getContext().getString(R.string.texPod),
                view.getContext().getString(R.string.aboutApp)};
        this.status = status;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemSettingsBlock4RvBinding binding =ItemSettingsBlock4RvBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.titel.setText(titels[position]);
        holder.icon_v.setImageResource(icons[position]);

        if(position==0 && status)
            holder.icon_v.setImageResource(R.drawable.ic_settings_message);

        holder.itemView.setOnClickListener(v -> SettingsListener.rv_click(position, view));
    }



    @Override
    public int getItemCount() {
        return titels.length;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView titel;
        public ImageView icon_v;

        public ViewHolder(ItemSettingsBlock4RvBinding binding) {
            super(binding.getRoot());
            titel = itemView.findViewById(R.id.settings_block_text);
            icon_v = itemView.findViewById(R.id.settings_block_icon);
        }
    }
}
