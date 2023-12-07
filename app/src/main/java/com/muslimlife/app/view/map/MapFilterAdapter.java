package com.muslimlife.app.view.map;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.ItemMapChooseBinding;
import com.muslimlife.app.view.map.models.MapFilterModel;

public class MapFilterAdapter extends RecyclerView.Adapter<MapFilterAdapter.ViewHolder> {

    List<MapFilterModel> filters;
    public int currentPosition = 0;
    MapView view;

    public MapFilterAdapter(List<MapFilterModel> filters, MapView view, int currentPosition) {
        this.filters=filters;
        this.view=view;
        this.currentPosition=currentPosition;
    }

    @NonNull
    @NotNull
    @Override
    public MapFilterAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemMapChooseBinding binding = ItemMapChooseBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        return new MapFilterAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MapFilterAdapter.ViewHolder holder, int position) {
        holder.binding.text.setText(filters.get(position).getName());

        if(position==currentPosition){
            holder.binding.text.setBackgroundTintList(ColorStateList.valueOf(holder.binding.getRoot().getContext().getColor(R.color.dark_blue)));
            holder.binding.text.setTextColor(holder.binding.getRoot().getContext().getColor(R.color.white));
        }
        else{
            holder.binding.text.setBackgroundTintList(ColorStateList.valueOf(holder.binding.getRoot().getContext().getColor(R.color.white)));
            holder.binding.text.setTextColor(holder.binding.getRoot().getContext().getColor(R.color.black));
        }

        holder.itemView.setOnClickListener(v->{
            currentPosition=position;
            view.changeFilter(filters.get(position));
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder{

        ItemMapChooseBinding binding;


        public ViewHolder(ItemMapChooseBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
