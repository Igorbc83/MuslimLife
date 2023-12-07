package com.muslimlife.app.view.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.radiobutton.MaterialRadioButton;

import java.util.ArrayList;
import java.util.List;

import com.muslimlife.app.data.model.UserLocation;
import com.muslimlife.app.databinding.ItemLocationChooseBinding;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<UserLocation> items = new ArrayList<>();
    private MaterialRadioButton chooseButton;
    private final OnItemSelectListener listener;

    public LocationAdapter(OnItemSelectListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLocationChooseBinding binding = ItemLocationChooseBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        return new LocationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        UserLocation item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {

        private final ItemLocationChooseBinding binding;

        public LocationViewHolder(ItemLocationChooseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(UserLocation item) {
            binding.choosedRb.setText(item.getName());
            binding.choosedRb.setChecked(item.isSelected());

            if(item.isSelected()) {
                chooseButton = binding.choosedRb;
            }

            binding.choosedRb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (chooseButton != null)
                    chooseButton.setChecked(false);

                chooseButton = binding.choosedRb;
                listener.onItemSelect(getAbsoluteAdapterPosition());
            });
        }
    }

    public void setItems(List<UserLocation> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
