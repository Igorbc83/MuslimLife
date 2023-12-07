package com.muslimlife.app.view.map;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import com.muslimlife.app.R;
import com.muslimlife.app.databinding.ItemMapChooseBinding;



public class MapAdapter extends RecyclerView.Adapter<MapAdapter.ViewHolder> {

    String[] filters;
    public int currentPosition=0;
    MapView view;

    public MapAdapter(String[] filters, MapView view) {
        this.filters=filters;
        this.view=view;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemMapChooseBinding binding = ItemMapChooseBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.binding.text.setText(filters[position]);

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
            view.changeFilter(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return filters.length;
    }


    public class ViewHolder extends  RecyclerView.ViewHolder{

        ItemMapChooseBinding binding;


        public ViewHolder(ItemMapChooseBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
