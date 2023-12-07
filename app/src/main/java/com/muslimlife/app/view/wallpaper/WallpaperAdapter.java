package com.muslimlife.app.view.wallpaper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import com.muslimlife.app.data.model.WallpaperRes;
import com.muslimlife.app.databinding.ItemWallpaperBinding;


public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.ViewHolder> {

    WallpaperRes[] wallpaperRes;
    WallpaperView view;
    boolean checkMod=false;

    String currentBuys;

    public WallpaperAdapter(WallpaperRes[] wallpaperRes, WallpaperView view, String currentBuys) {
        this.wallpaperRes = wallpaperRes;
        this.view = view;
        this.currentBuys = currentBuys;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemWallpaperBinding binding =ItemWallpaperBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        holder.binding.check.setVisibility(checkMod?View.VISIBLE:View.GONE);

        Glide
                .with(holder.binding.getRoot().getContext())
                .load(wallpaperRes[position].getImage())
                .apply(new RequestOptions().override(300, 300))
                .into(holder.binding.wallpaperImage);

        holder.itemView.setOnClickListener(v->{
            if(checkMod)
                holder.binding.check.setChecked(!holder.binding.check.isChecked());
            else
                view.chooseWallpaper(wallpaperRes[position]);
        });

        holder.binding.check.setOnCheckedChangeListener((buttonView, isChecked) -> wallpaperRes[position].setChecked(isChecked));

        holder.itemView.setOnLongClickListener(v->{
            checkMod=!checkMod;
            view.checkMod(checkMod);
            holder.binding.check.setChecked(checkMod);
            notifyDataSetChanged();
            return true;
        });

        if(currentBuys.contains("/" + wallpaperRes[position].getId() + "/")){
            holder.binding.buyView.setVisibility(View.VISIBLE);
            holder.binding.costView.setVisibility(View.GONE);
        }else{
            holder.binding.buyView.setVisibility(View.GONE);
            holder.binding.costView.setVisibility(View.VISIBLE);
        }
    }

    public ArrayList<WallpaperRes> getWalwaper(){
        ArrayList<WallpaperRes> checked=new ArrayList<>();

        for (WallpaperRes wallpaper: wallpaperRes)
            if(wallpaper.isChecked())
                checked.add(wallpaper);

        return checked;
    }

    @Override
    public int getItemCount() {
        return wallpaperRes.length;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        ItemWallpaperBinding binding;

        public ViewHolder(ItemWallpaperBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
