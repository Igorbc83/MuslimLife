package com.muslimlife.app.view.reader;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.muslimlife.app.data.model.ReadersRes;

import org.jetbrains.annotations.NotNull;

import com.muslimlife.app.databinding.ItemReaderBinding;


public class ReaderAdapter extends RecyclerView.Adapter<ReaderAdapter.ViewHolder> {

    private ReadersRes[] readers;
    private int currentReader;

    public ReaderAdapter(ReadersRes[] readers, int currentReader) {
        this.readers = readers;
        this.currentReader=currentReader;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemReaderBinding binding =ItemReaderBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        if(currentReader==position){
            holder.binding.choosedRb.setChecked(true);
        }else{
            holder.binding.choosedRb.setChecked(false);
        }

        holder.binding.textTv.setText(readers[position].getName());
        Glide.with(holder.binding.getRoot().getContext()).load(readers[position].getAvatar()).into(holder.binding.dialogIcon);

        holder.binding.choosedRb.setOnClickListener(v->{
            currentReader=position;
            notifyDataSetChanged();
        });
    }

    public int getCurrentReader(){
        return currentReader;
    }

    public String getCurrentReaderId(){
        return readers[currentReader].getId();
    }

    @Override
    public int getItemCount() {
        return readers.length;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        ItemReaderBinding binding;

        public ViewHolder(ItemReaderBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
