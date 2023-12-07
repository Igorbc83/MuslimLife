package com.muslimlife.app.data.model.gplaces;

import com.google.gson.annotations.SerializedName;

public class GPlaces {

    @SerializedName("place_id")
    private String id;

    private String name;
    private GPlacesGeometry geometry;
    private String vicinity;

    public String getName() {
        return name;
    }

    public GPlacesGeometry getGeometry() {
        return geometry;
    }

    public String getVicinity() {
        return vicinity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
