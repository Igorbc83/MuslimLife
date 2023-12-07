package com.muslimlife.app.view.radio;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.muslimlife.app.data.model.RadioRes;

import org.jetbrains.annotations.NotNull;


import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentRadioBinding;
import com.muslimlife.app.databinding.ItemRadioBinding;


public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder> {

    private RadioRes[] radioRes;
    private int choosedRadio=-1;
    FragmentRadioBinding fragmentBinding;
    private RadioListener radioListener;


    public RadioAdapter(RadioRes[] radioRes, FragmentRadioBinding fragmentBinding, RadioListener radioListener) {
        this.radioRes = radioRes;
        this.fragmentBinding=fragmentBinding;
        this.radioListener = radioListener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemRadioBinding binding =ItemRadioBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new ViewHolder(binding);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(choosedRadio==position)
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getColor(R.color.line_gray));
        else
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getColor(R.color.light_white));


        holder.binding.dialogTitel.setText(radioRes[position].getName());
        holder.binding.dialogBottomText.setText(radioRes[position].getDescription());
        Glide.with(holder.binding.getRoot().getContext()).load(radioRes[position].getImage()).into(holder.binding.dialogIcon);

        holder.itemView.setOnClickListener(v-> {
            choosedRadio = position;
            radioListener.selectRadio(radioRes[position]);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return radioRes.length;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        ItemRadioBinding binding;

        public ViewHolder(ItemRadioBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
