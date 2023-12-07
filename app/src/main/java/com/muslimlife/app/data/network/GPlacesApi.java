package com.muslimlife.app.data.network;

import com.muslimlife.app.data.model.gplaces.GPlaceDetailResult;
import com.muslimlife.app.data.model.gplaces.GPlacesResult;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GPlacesApi {

    @GET("place/nearbysearch/json")
    Single<GPlacesResult> getPlaces(
            @Query("keyword") String keyword,
            @Query("location") String location,
            @Query("radius") long radius,
            @Query("type") String type,
            @Query("language") String language,
            @Query("key") String key,
            @Query("sensor") boolean sensor
    );

    @GET("place/details/json")
    Single<GPlaceDetailResult> getPlaceDetail(
            @Query("place_id") String keyword,
            @Query("language") String language,
            @Query("key") String key
    );
}
