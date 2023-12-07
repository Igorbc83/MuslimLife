package com.muslimlife.app.view.language;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import com.muslimlife.app.databinding.ItemLocationChooseBinding;


public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {



    String[] languages;
    int choosed_pos;

    LanguageListener listener;

    public LanguageAdapter(String[]languages, int choosed_pos) {

        this.languages = languages;
        this.choosed_pos = choosed_pos;
    }

    public LanguageAdapter(String[]languages, int choosed_pos, LanguageListener listener) {

        this.languages = languages;
        this.choosed_pos = choosed_pos;
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemLocationChooseBinding binding = ItemLocationChooseBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.radioButton.setText(languages[position]);
        holder.radioButton.setChecked(choosed_pos==position);

        holder.radioButton.setOnClickListener(v->{
            choosed_pos=position;

            if(listener != null)
                listener.selectedLang(position);

            this.notifyDataSetChanged();
        });
    }

    public int getCurrentLanguagePosition(){
        return choosed_pos;
    }

    public String getCurrentLanguage(){
        return languages[choosed_pos];
    }


    @Override
    public int getItemCount() {
        return languages.length;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView text_v;
        RadioButton radioButton;

        public ViewHolder(ItemLocationChooseBinding binding) {
            super(binding.getRoot());
            radioButton = binding.choosedRb;

        }
    }

    public interface LanguageListener {
        void selectedLang(int pos);
    }
}
