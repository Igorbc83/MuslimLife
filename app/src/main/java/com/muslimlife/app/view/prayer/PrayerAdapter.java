package com.muslimlife.app.view.prayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.ItemPayerBinding;


public class PrayerAdapter extends RecyclerView.Adapter<PrayerAdapter.ViewHolder> {


    Context context;
    int currentPrayer;
    ArrayList<String> prayerTimes;
    int[] names;

    public PrayerAdapter(Context context, ArrayList<String> prayerTimes,int[] names, int currentPrayer) {

        this.context=context;
        this.currentPrayer=currentPrayer;
        this.prayerTimes=prayerTimes;
        this.names=names;

    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemPayerBinding binding = ItemPayerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.name_v.setText(context.getResources().getString(names[position]));

        holder.time_v.setText(prayerTimes.get(position));

        if(currentPrayer==position){
            holder.name_v.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.dark_blue));
            holder.time_v.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.dark_blue));
        }
    }


    @Override
    public int getItemCount() {
        return names.length;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView name_v;
        TextView time_v;


        public ViewHolder(ItemPayerBinding binding) {
            super(binding.getRoot());
            name_v = binding.prayerName;
            time_v = binding.prayerTime;
        }
    }
}
