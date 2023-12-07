package com.muslimlife.app.data.model.gplaces;

import com.google.gson.annotations.SerializedName;

public class GPlaceDetailPhoto {

    @SerializedName("photo_reference")
    private String photoReference;

    public String getPhotoReference() {
        return photoReference;
    }
}
