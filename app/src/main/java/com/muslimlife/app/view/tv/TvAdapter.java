package com.muslimlife.app.view.tv;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import com.muslimlife.app.data.model.TvRes;
import com.muslimlife.app.databinding.ItemTvBinding;
import com.muslimlife.app.view.YoutubeStreamActivity;


public class TvAdapter extends RecyclerView.Adapter<TvAdapter.ViewHolder> {

    TvRes[] tvRes;

    public TvAdapter(TvRes[] tvRes) {
        this.tvRes = tvRes;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemTvBinding binding =ItemTvBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.binding.dialogTitel.setText(tvRes[position].getName());
        holder.binding.dialogBottomText.setText(tvRes[position].getDescription());
        //holder.time.setText(tv.time[position]);
        //holder.icon.setImageResource(tv.icon[position]);
        Glide.with(holder.itemView.getContext()).load(tvRes[position].getImage()).into(holder.binding.dialogIcon);
        holder.itemView.setOnClickListener(v -> {
            String url = tvRes[position].getLink();
            if(url.contains("youtube")){
                Intent intent = new Intent(holder.itemView.getContext(), YoutubeStreamActivity.class);
                intent.putExtra("youtube_id", url.split("v=")[1]);

                holder.itemView.getContext().startActivity(intent);
            }else if(url.contains("youtu.be")){
                Intent intent = new Intent(holder.itemView.getContext(), YoutubeStreamActivity.class);
                String[] list = url.split("/");
                intent.putExtra("youtube_id", list[list.length - 1]);

                holder.itemView.getContext().startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return tvRes.length;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        ItemTvBinding binding;

        public ViewHolder(ItemTvBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
