package com.muslimlife.app.view.bookmark;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.muslimlife.app.R;
import com.muslimlife.app.data.model.Bookmark;
import com.muslimlife.app.databinding.ItemSuriBinding;


public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {


    List<Bookmark> bookmarks;
    private OnItemClickChildren onItemClickChildren;

    public BookmarkAdapter(List<Bookmark> bookmarks, OnItemClickChildren clickChildren) {
        this.bookmarks = bookmarks;
        this.onItemClickChildren = clickChildren;
    }

    public interface OnItemClickChildren {
        void onItemClick(Bookmark bookmark);
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
        holder.bookmarkNumber.setVisibility(View.GONE);
        holder.bookmarkTitle.setText(bookmarks.get(position).getSurahName());

        holder.bookmarkText.setText(String.format("%s %s", bookmarks.get(position).getNumber(), holder.bookmarkNumber.getResources().getString(R.string.page)));

        holder.itemView.setOnClickListener(v->{
            onItemClickChildren.onItemClick(bookmarks.get(position));
        });
    }



    @Override
    public int getItemCount() {
        return bookmarks.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView bookmarkNumber;
        TextView bookmarkTitle;
        TextView bookmarkText;

        public ViewHolder(ItemSuriBinding binding) {
            super(binding.getRoot());
            bookmarkNumber = binding.suriNumber;
            bookmarkTitle = binding.suriTitel;
            bookmarkText = binding.suriOriginalTitel;

        }
    }
}
