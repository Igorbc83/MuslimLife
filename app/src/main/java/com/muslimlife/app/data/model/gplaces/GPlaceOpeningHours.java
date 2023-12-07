package com.muslimlife.app.data.model.gplaces;

import com.google.gson.annotations.SerializedName;

public class GPlaceOpeningHours {

    @SerializedName("weekday_text")
    private String[] weekdayText;

    public String[] getWeekdayText() {
        return weekdayText;
    }
}
