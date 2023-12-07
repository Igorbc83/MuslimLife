package com.muslimlife.app.view.map;

import java.util.List;

import com.muslimlife.app.data.model.MapClusterItem;
import com.muslimlife.app.data.model.PlacesRes;
import com.muslimlife.app.view.map.models.MapFilterModel;

public interface MapView {
    default void changeFilter(int position){}
    default void changeFilter(MapFilterModel filter){}
    default void compilePlaces(PlacesRes[] placesRes) {}
    default void compilePlaces(List<PlacesRes> placesRes) {}
    void clickCluster(PlacesRes placesRes, MapClusterItem item);
    void choosePlace(PlacesRes placesRes);
}
