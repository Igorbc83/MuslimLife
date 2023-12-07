package com.muslimlife.app.data.model.gplaces;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GPlaceDetail {

    @SerializedName("current_opening_hours")
    private GPlaceOpeningHours openingHours;

    @SerializedName("formatted_phone_number")
    private String formattedPhone;

    @SerializedName("international_phone_number")
    private String internationalPhone;

    private List<GPlaceDetailPhoto> photos;

    public GPlaceOpeningHours getOpeningHours() {
        return openingHours;
    }

    public String getFormattedPhone() {
        return formattedPhone;
    }

    public String getInternationalPhone() {
        return internationalPhone;
    }

    public List<GPlaceDetailPhoto> getPhotos() {
        return photos;
    }
}
