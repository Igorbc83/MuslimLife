package com.muslimlife.app.view.suri;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muslimlife.app.data.model.surah.SurahRes;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.muslimlife.app.databinding.ItemSuriBinding;


public class SuriAdapter extends RecyclerView.Adapter<SuriAdapter.ViewHolder> {


    List<SurahRes> surahList;
    private final OnItemClickChildren onItemClickChildren;
    private String lang = "en";

    public SuriAdapter(List<SurahRes> surahList, OnItemClickChildren clickChildren, String lang) {
        this.surahList = surahList;
        this.onItemClickChildren = clickChildren;
        this.lang = lang;
    }

    public interface OnItemClickChildren {
        void onItemClick(SurahRes surah);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemSuriBinding binding =ItemSuriBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.suriNumber.setText((position + 1) +".");
        holder.suriOriginal.setText(surahList.get(position).getTranslate().get("ar"));

        String translate = surahList.get(position).getTranslate().get(lang);
        if(translate != null && !translate.isEmpty())
            holder.suriTittel.setText(translate);
        else
            holder.suriTittel.setText("Нет перевода");

        holder.itemView.setOnClickListener(v->{
            onItemClickChildren.onItemClick(surahList.get(position));
        });
    }



    @Override
    public int getItemCount() {
        return surahList.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        TextView suriNumber;
        TextView suriTittel;
        TextView suriOriginal;

        public ViewHolder(ItemSuriBinding binding) {
            super(binding.getRoot());
            suriNumber = binding.suriNumber;
            suriTittel = binding.suriTitel;
            suriOriginal = binding.suriOriginalTitel;

        }
    }
}
