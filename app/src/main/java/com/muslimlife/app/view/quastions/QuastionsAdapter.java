package com.muslimlife.app.view.quastions;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import com.muslimlife.app.R;
import com.muslimlife.app.data.model.QuastionModel;
import com.muslimlife.app.data.model.QuastionTypeRes;
import com.muslimlife.app.databinding.ItemQuastionsBinding;
import com.muslimlife.app.databinding.ItemRadioBinding;


public class QuastionsAdapter extends RecyclerView.Adapter<QuastionsAdapter.ViewHolder> {

    ArrayList<QuastionModel> quastions;
    final boolean isActive;
    private QuastionTypeRes[] quastionTypeRes;
    private String choosedFilter;
    private QuastionsView view;

    public QuastionsAdapter(ArrayList<QuastionModel> quastions, boolean isActive, QuastionTypeRes[] quastionTypeRes, String choosedFilter, QuastionsView quastionsView){
        this.quastions=quastions;
        this.isActive=isActive;
        this.quastionTypeRes=quastionTypeRes;
        this.choosedFilter=choosedFilter;
        this.view=quastionsView;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemQuastionsBinding binding =ItemQuastionsBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if(String.valueOf(quastions.get(position).getQuastionsRes().getType_id()).equals(choosedFilter)||choosedFilter.equals("-1")){
            if(!isActive){
                holder.binding.parent.setBackground(holder.itemView.getContext().getDrawable(R.drawable.bg_quastion_pass));
                holder.binding.text.setTextColor(holder.itemView.getContext().getColor(R.color.black));
                holder.binding.kind.setTextColor(holder.itemView.getContext().getColor(R.color.gray));
                holder.binding.kind.setAlpha(1);
                holder.binding.date.setTextColor(holder.itemView.getContext().getColor(R.color.gray));
                holder.binding.date.setAlpha(1);
                holder.binding.name.setTextColor(holder.itemView.getContext().getColor(R.color.gray));
                holder.binding.name.setAlpha(1);
            }

            for(QuastionTypeRes quastionTypeRes : quastionTypeRes)
                if(quastionTypeRes.getId().equals(String.valueOf(quastions.get(position).getQuastionsRes().getType_id())))
                    holder.binding.kind.setText(quastionTypeRes.getName());

            holder.binding.text.setText(quastions.get(position).getQuastionsRes().getText());
            holder.binding.name.setText(quastions.get(position).getQuastionsRes().getFullName());

            try {
                SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat myFormat = new SimpleDateFormat("dd MMM kk:mm");
                fromUser.setTimeZone(TimeZone.getTimeZone("GMT"));
                String dateS = myFormat.format(fromUser.parse(quastions.get(position).getQuastionsRes().getUpdated()));
                holder.binding.date.setText(dateS);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.itemView.setOnClickListener(v->{
                if(isActive)
                    view.chooseQuastion(quastions.get(position).getQuastionsRes().getId());
            });

            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }else{
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    @Override
    public int getItemCount() {
        return quastions.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        ItemQuastionsBinding binding;

        public ViewHolder(ItemQuastionsBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
