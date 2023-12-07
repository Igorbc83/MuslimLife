package com.muslimlife.app.view.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muslimlife.app.data.model.GetAnswerRes;

import org.jetbrains.annotations.NotNull;

import com.muslimlife.app.databinding.ItemMessageBinding;


public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {

    AnswerView answerView;
    GetAnswerRes[] answerRes;

    public AnswersAdapter(AnswerView answerView, GetAnswerRes[] answerRes) {
        this.answerView=answerView;
        this.answerRes=answerRes;
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
        holder.binding.messageTitel.setText(answerRes[position].getImam_first_name()+" "+answerRes[position].getImam_last_name());
        holder.binding.messagePrev.setText(answerRes[position].getAnswer_text());

        if(answerRes[position].getAnswer_status().equals("1"))
            holder.binding.messageIndicator.setVisibility(View.VISIBLE);
        else
            holder.binding.messageIndicator.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v->answerView.chooseAnswer(answerRes[position]));
    }



    @Override
    public int getItemCount() {
        return answerRes.length;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        ItemMessageBinding binding;


        public ViewHolder(ItemMessageBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
