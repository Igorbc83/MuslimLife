package com.muslimlife.app.adapters;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muslimlife.app.adapters.holders.KoranHolder;
import com.muslimlife.app.data.model.KoranTestRes;

import com.muslimlife.app.databinding.ItemKoranPageFullBinding;
import com.muslimlife.app.view.koran.listeners.KoranPageListener;

public class KoranAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private List<KoranTestRes> items;

    private boolean stateFull = true;

    private DisplayMetrics displayMetrics;

    private KoranPageListener listener;

    public KoranAdapter(List<KoranTestRes> items, DisplayMetrics displayMetrics, KoranPageListener listener) {
        this.items = items;
        this.displayMetrics = displayMetrics;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KoranHolder(ItemKoranPageFullBinding.inflate(
                LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((KoranHolder) viewHolder).initKoranPage(stateFull, items.get(position), displayMetrics, listener, position, items.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        return stateFull ? 1 : 0;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setStateFull(boolean stateFull) {
        this.stateFull = stateFull;
        notifyDataSetChanged();

        listener.stateChange(stateFull);
    }

    public boolean isStateFull() {
        return stateFull;
    }
}
