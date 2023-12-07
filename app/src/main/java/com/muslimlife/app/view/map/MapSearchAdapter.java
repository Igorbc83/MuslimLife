package com.muslimlife.app.view.map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muslimlife.app.data.model.MapClusterItem;
import com.muslimlife.app.data.model.PlacesRes;

import com.muslimlife.app.databinding.ItemMapChooseBinding;
import com.muslimlife.app.databinding.ItemMapRvBinding;


public class MapSearchAdapter extends RecyclerView.Adapter<MapSearchAdapter.ViewHolder> implements MapView{

    PlacesRes[] placesRes;
    MapView mapView;
    EditText searchEdit;

    public MapSearchAdapter(PlacesRes[] placesRes, MapView mapView, EditText searchEdit){
        this.placesRes=placesRes;
        this.mapView=mapView;
        this.searchEdit=searchEdit;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMapRvBinding binding =ItemMapRvBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new MapSearchAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(placesRes[position].name.toLowerCase().contains(searchEdit.getText().toString().toLowerCase())){
            holder.binding.searchName.setText(placesRes[position].name);
            holder.binding.searchAddress.setText(placesRes[position].address);
            holder.itemView.setOnClickListener(v-> mapView.choosePlace(placesRes[position]));

            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    @Override
    public int getItemCount() {
        return placesRes.length;
    }

    @Override
    public void changeFilter(int position) {

    }

    @Override
    public void compilePlaces(PlacesRes[] placesRes) {

    }

    @Override
    public void clickCluster(PlacesRes placesRes, MapClusterItem item) {

    }

    @Override
    public void choosePlace(PlacesRes placesRes) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemMapRvBinding binding;

        public ViewHolder(@NonNull ItemMapRvBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
