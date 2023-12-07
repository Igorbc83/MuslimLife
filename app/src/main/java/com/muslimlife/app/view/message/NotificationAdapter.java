package com.muslimlife.app.view.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muslimlife.app.data.model.NotificationRes;

import org.jetbrains.annotations.NotNull;

import com.muslimlife.app.databinding.ItemMessageBinding;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    AnswerView answerView;
    NotificationRes[] notificationRes;

    public NotificationAdapter(AnswerView answerView, NotificationRes[] notificationRes) {
        this.answerView=answerView;
        this.notificationRes=notificationRes;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemMessageBinding binding = ItemMessageBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.binding.messageTitel.setText(notificationRes[position].getTitle());
        holder.binding.messagePrev.setText(notificationRes[position].getText());

        if(notificationRes[position].getRead().equals("0"))
            holder.binding.messageIndicator.setVisibility(View.VISIBLE);
        else
            holder.binding.messageIndicator.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v->answerView.chooseNotification(notificationRes[position]));
    }



    @Override
    public int getItemCount() {
        return notificationRes.length;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        ItemMessageBinding binding;


        public ViewHolder(ItemMessageBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
