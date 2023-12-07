package com.muslimlife.app.data.repository;

import com.muslimlife.app.data.model.gplaces.GPlaceDetailResult;
import com.muslimlife.app.data.model.gplaces.GPlacesResult;

import io.reactivex.Single;

import com.muslimlife.app.data.network.GPlacesApi;

public class GPlacesRepository {

    private final GPlacesApi api;

    public GPlacesRepository(GPlacesApi api) {
        this.api = api;
    }

    public Single<GPlacesResult> getPlaces(
            String keyword,
            String location,
            long radius,
            String type
            /*String language,
            String key,
            boolean sensor*/
    ) {
        return api.getPlaces(
                keyword,
                location,
                radius,
                type,
                "ru", //language
                "AIzaSyCY86gWeztSiacWZr9tc2D2zU3ui49BWqM", //key
                true//sensor
        );
    }

    public Single<GPlaceDetailResult> getPlaceDetail(
            String keyword
    ) {
        return api.getPlaceDetail(
                keyword,
                "ru", //language
                "AIzaSyCY86gWeztSiacWZr9tc2D2zU3ui49BWqM" //key
        );
    }
}
