package com.muslimlife.app.view.azkar.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.muslimlife.app.data.model.azkars.AzkarModel;
import com.muslimlife.app.databinding.ItemAzkarBinding;
import com.muslimlife.app.view.adapters.OnItemSelectListener;

public class AzkarListAdapter extends RecyclerView.Adapter<AzkarListAdapter.AzkarViewHolder> {

    private List<AzkarModel> items;
    private final OnItemSelectListener listener;

    public AzkarListAdapter(List<AzkarModel> items, OnItemSelectListener listener) {
        this.listener = listener;
        this.items = items;
    }

    @NonNull
    @Override
    public AzkarListAdapter.AzkarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAzkarBinding binding = ItemAzkarBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        return new AzkarListAdapter.AzkarViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AzkarListAdapter.AzkarViewHolder holder, int position) {
        AzkarModel item = items.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class AzkarViewHolder extends RecyclerView.ViewHolder {

        private final ItemAzkarBinding binding;

        public AzkarViewHolder(ItemAzkarBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AzkarModel item, int position) {
            binding.text.setText(item.getAzkarHeader());

            binding.getRoot().setOnClickListener(view -> listener.onItemSelect(position));
        }
    }

    public void setItems(List<AzkarModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
