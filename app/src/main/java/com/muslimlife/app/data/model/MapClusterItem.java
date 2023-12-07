package com.muslimlife.app.data.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.muslimlife.app.view.map.MapView;

import com.muslimlife.app.R;

public class MapClusterItem extends DefaultClusterRenderer<MapClusterItem> implements ClusterItem, MapView {

    private final LatLng position;
    private final String title;
    private final String snippet;
    private final BitmapDescriptor icon;
    private final IconGenerator mClusterIconGenerator;
    private final Context context;
    private final ClusterManager<MapClusterItem> clusterManager;
    private final PlacesRes placesRes;
    private Marker marker;
    private boolean change = false;

    public MapClusterItem(LatLng position, String title, String snippet, BitmapDescriptor icon,Context context, GoogleMap map, ClusterManager<MapClusterItem> clusterManager, PlacesRes placesRes) {
        super(context, map, clusterManager);
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.icon = icon;
        this.context=context;
        this.clusterManager=clusterManager;
        mClusterIconGenerator=new IconGenerator(context);
        this.placesRes = placesRes;
    }


    public BitmapDescriptor getIcon() {
        return icon;
    }

    public Marker getMarker(){
        return marker;
    }

    public PlacesRes getPlacesRes() {
        return placesRes;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return position;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    @Nullable
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return snippet;
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<MapClusterItem> cluster, MarkerOptions markerOptions) {

    }

    @Override
    protected void onClusterItemRendered(MapClusterItem clusterItem, Marker marker) {
        if(!change){
            super.onClusterItemRendered(clusterItem, marker);
            marker.setIcon(clusterItem.getIcon());
            clusterItem.marker=marker;
        }
    }

    @Override
    public void changeFilter(int position) {

    }

    @Override
    public void compilePlaces(PlacesRes[] placesRes) {

    }

    @Override
    public void clickCluster(PlacesRes placesRes, MapClusterItem item) {
        switch (placesRes.type){
            case "1":
                item.getMarker().setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_shop));
                break;
            case "2":
            case "3":
                item.getMarker().setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cafe_rest));
                break;
            case "4":
            case "5":
                item.getMarker().setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_church));
                break;
        }
    }

    @Override
    public void choosePlace(PlacesRes placesRes) {
    }


    @Override
    protected void onBeforeClusterItemRendered(MapClusterItem item, MarkerOptions markerOptions) {
        markerOptions.icon(item.getIcon());
    }
}
