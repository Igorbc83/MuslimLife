package com.muslimlife.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muslimlife.app.adapters.holders.BarakatHolder;
import com.muslimlife.app.adapters.holders.DiaspoarsHolder;
import com.muslimlife.app.adapters.holders.ImamHolder;
import com.muslimlife.app.adapters.holders.MainHolder;
import com.muslimlife.app.adapters.holders.MainQuestionHolder;
import com.muslimlife.app.adapters.holders.MigrantsHolder;
import com.muslimlife.app.adapters.holders.WorshipHolder;
import com.muslimlife.app.databinding.ItemMainQuestionBinding;
import com.muslimlife.app.databinding.ItemMigrantsBinding;
import com.muslimlife.app.utils.Constants;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.ItemBarakatBinding;
import com.muslimlife.app.databinding.ItemDiaspoarMainBinding;
import com.muslimlife.app.databinding.ItemImamMainBinding;
import com.muslimlife.app.databinding.ItemMainRecyclerBinding;
import com.muslimlife.app.databinding.ItemMainWorshipBinding;

import com.muslimlife.app.view.main.listeners.MainRecyclerListener;

import all.muslimlife.adapters.holders.ChatAIMainHolder;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private List<RecyclerModel> items;

    MainRecyclerListener listener;

    public MainAdapter(List<RecyclerModel> items, MainRecyclerListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case Constants.RECYCLER_PLACES_ITEM:
            case Constants.RECYCLER_WORSHIP_ITEM:
            case Constants.RECYCLER_AZKARS_ITEM:
                return new WorshipHolder(ItemMainWorshipBinding.inflate(
                        LayoutInflater.from(parent.getContext())));
            case Constants.RECYCLER_SPACE_12_ITEM:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_space_12dp, parent, false));
            case Constants.RECYCLER_IMAM_ITEM:
                return new ImamHolder(ItemImamMainBinding.inflate(
                        LayoutInflater.from(parent.getContext())));
            case Constants.RECYCLER_MAIN_WORSHIP_ITEM:
            case Constants.RECYCLER_MAIN_OTHER_ITEM:
            case Constants.RECYCLER_MAIN_DIASPOARS_ITEM:
            case Constants.RECYCLER_MAIN_PLACES_ITEM:
            case Constants.RECYCLER_MAIN_AZKARS_ITEM:
                return new MainHolder(ItemMainRecyclerBinding.inflate(
                        LayoutInflater.from(parent.getContext())));
            case Constants.RECYCLER_MAIN_QUESTION_ITEM:
                return new MainQuestionHolder(ItemMainQuestionBinding.inflate(
                        LayoutInflater.from(parent.getContext())));
            case Constants.RECYCLER_DIASPOARS_ITEM:
                return new DiaspoarsHolder(ItemDiaspoarMainBinding.inflate(
                        LayoutInflater.from(parent.getContext())));
            case Constants.RECYCLER_MAIN_BARAKAT_ITEM:
                return new BarakatHolder(ItemBarakatBinding.inflate(
                        LayoutInflater.from(parent.getContext())));
            case Constants.RECYCLER_MAIN_MIGRANTS_ITEM:
                return new MigrantsHolder(ItemMigrantsBinding.inflate(
                        LayoutInflater.from(parent.getContext())));
            case Constants.RECYCLER_MAIN_CHAT_AI_ITEM:
                return new ChatAIMainHolder(ItemBarakatBinding.inflate(
                        LayoutInflater.from(parent.getContext())));
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        switch (items.get(position).getType()) {
            case Constants.RECYCLER_WORSHIP_ITEM:
                ((WorshipHolder) viewHolder).initWorship(items.get(position).getWorship());
                ((WorshipHolder) viewHolder).itemView.setOnClickListener(v -> {
                    listener.selectWorship(items.get(position).getWorship().getType());
                    /*if(items.get(position).getWorship().getType()!=R.id.ZakyatFragment)
                        listener.selectWorship(items.get(position).getWorship().getType());
                    else
                        Toast.makeText(viewHolder.itemView.getContext(), viewHolder.itemView.getContext().getString(R.string.section_under_development), Toast.LENGTH_SHORT).show();*/
                });
                break;
            case Constants.RECYCLER_IMAM_ITEM:
                ((ImamHolder) viewHolder).initImam(items.get(position).getImamRes());
                ((ImamHolder) viewHolder).itemView.setOnClickListener(v -> listener.selectImam(items.get(position).getImamRes()));
                break;
            case Constants.RECYCLER_DIASPOARS_ITEM:
                ((DiaspoarsHolder) viewHolder).initDiaspoar(items.get(position).getDiaspoarRes());
                ((DiaspoarsHolder) viewHolder).itemView.setOnClickListener(v -> listener.selectDiaspoar(items.get(position).getDiaspoarRes()));
                break;
            case Constants.RECYCLER_MAIN_PLACES_ITEM:
            case Constants.RECYCLER_MAIN_WORSHIP_ITEM:
            case Constants.RECYCLER_MAIN_OTHER_ITEM:
            case Constants.RECYCLER_MAIN_DIASPOARS_ITEM:
            case Constants.RECYCLER_MAIN_AZKARS_ITEM:
                ((MainHolder) viewHolder).initMainHolder(items.get(position), listener);
                break;
            case Constants.RECYCLER_MAIN_QUESTION_ITEM:
                ((MainQuestionHolder) viewHolder).initMainHolder(items.get(position), listener);
                break;
            case Constants.RECYCLER_PLACES_ITEM:
                ((WorshipHolder) viewHolder).initWorship(items.get(position).getWorship());
                ((WorshipHolder) viewHolder).itemView.setOnClickListener(v -> listener.selectMap(position));
                break;
            case Constants.RECYCLER_MAIN_BARAKAT_ITEM:
                ((BarakatHolder) viewHolder).initBarakat();
                viewHolder.itemView.setOnClickListener(v -> listener.selectBarakat());
                break;
            case Constants.RECYCLER_AZKARS_ITEM:
                ((WorshipHolder) viewHolder).initWorship(items.get(position).getWorship());
                ((WorshipHolder) viewHolder).itemView.setOnClickListener(v -> {
                    listener.selectAzkars(items.get(position).getWorship().getType());
                });
                break;
            case Constants.RECYCLER_MAIN_MIGRANTS_ITEM:
                ((MigrantsHolder) viewHolder).initMigrants();
                viewHolder.itemView.setOnClickListener(v -> listener.selectMigrants());
                break;
            case Constants.RECYCLER_MAIN_CHAT_AI_ITEM:
                ((ChatAIMainHolder) viewHolder).init();
                viewHolder.itemView.setOnClickListener(v -> listener.selectChatAI());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder { public ViewHolder(View view) { super(view); }}

}
