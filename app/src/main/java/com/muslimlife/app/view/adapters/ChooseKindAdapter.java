package com.muslimlife.app.view.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import com.muslimlife.app.data.model.QuastionTypeRes;
import com.muslimlife.app.databinding.ItemLocationChooseBinding;


public class ChooseKindAdapter extends RecyclerView.Adapter<ChooseKindAdapter.ViewHolder> {

    QuastionTypeRes[] quastionTypeRes;
    String choosed_pos;

    public ChooseKindAdapter(QuastionTypeRes[] quastionTypeRes, String choosed_pos) {
        this.quastionTypeRes = quastionTypeRes;
        this.choosed_pos=choosed_pos;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemLocationChooseBinding binding = ItemLocationChooseBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        holder.binding.choosedRb.setChecked(choosed_pos.equals(quastionTypeRes[position].getId()));
        holder.binding.choosedRb.setText(quastionTypeRes[position].getName());

        holder.binding.choosedRb.setOnClickListener(v->{
            choosed_pos=quastionTypeRes[position].getId();
            notifyDataSetChanged();
        });
    }

    public String getChoosed_pos() {
        return choosed_pos;
    }

    @Override
    public int getItemCount() {
        return quastionTypeRes.length;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        ItemLocationChooseBinding binding;

        public ViewHolder(ItemLocationChooseBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
