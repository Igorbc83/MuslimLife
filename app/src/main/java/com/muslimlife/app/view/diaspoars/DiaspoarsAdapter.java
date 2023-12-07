package com.muslimlife.app.view.diaspoars;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import com.muslimlife.app.data.model.DiaspoarResponce;
import com.muslimlife.app.databinding.ItemRadioBinding;


public class DiaspoarsAdapter extends RecyclerView.Adapter<DiaspoarsAdapter.ViewHolder> {

    DiaspoarResponce[] diaspoars;
    DiaspoarsView view;

    public DiaspoarsAdapter(DiaspoarResponce[] diaspoars, DiaspoarsView view) {
        this.diaspoars = diaspoars;
        this.view=view;
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

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.binding.dialogTitel.setText(diaspoars[position].getName());
        holder.binding.dialogBottomText.setText(diaspoars[position].getName()+" "+diaspoars[position].getImam_lastname());
        Glide.with(holder.itemView.getContext()).load(diaspoars[position].getImage()).into(holder.binding.dialogIcon);

        holder.itemView.setOnClickListener(v->view.setDiaspoar(diaspoars[position]));
    }



    @Override
    public int getItemCount() {
        return diaspoars.length;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        ItemRadioBinding binding;

        public ViewHolder(ItemRadioBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
