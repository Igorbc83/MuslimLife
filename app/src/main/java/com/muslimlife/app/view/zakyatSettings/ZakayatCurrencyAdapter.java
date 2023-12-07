package com.muslimlife.app.view.zakyatSettings;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muslimlife.app.databinding.ItemCurrencyBinding;

public class ZakayatCurrencyAdapter extends RecyclerView.Adapter<ZakayatCurrencyAdapter.ViewHolder>{

    private final String[] currencies;
    private final OnItemClickListener onItemClick;
    private int selectedCurrency;
    public ZakayatCurrencyAdapter(String[] readers, int selectedCurrency, OnItemClickListener onItemClick) {
        this.currencies = readers;
        this.selectedCurrency = selectedCurrency;
        this.onItemClick = onItemClick;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCurrencyBinding binding = ItemCurrencyBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.choosedRb.setChecked(selectedCurrency == position);

        holder.binding.textTv.setText(currencies[position]);
        holder.binding.choosedRb.setOnClickListener(v->{
            selectedCurrency=position;
            notifyDataSetChanged();
            onItemClick.selectedCurrency(currencies[position],position);
        });

    }


    @Override
    public int getItemCount() {
        return currencies.length;
    }

    public interface OnItemClickListener {
        void selectedCurrency(String name, int position);
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        ItemCurrencyBinding binding;

        public ViewHolder(ItemCurrencyBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

}
