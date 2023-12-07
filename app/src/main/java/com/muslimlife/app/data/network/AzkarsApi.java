package com.muslimlife.app.data.network;

import com.muslimlife.app.data.model.azkars.AzkarsRes;

import all.muslimlife.data.model.QrCodeRes;
import all.muslimlife.data.model.migrants.MigrantsRes;
import all.muslimlife.data.model.clients.ClientsRes;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AzkarsApi {

    @GET("postgresqlphpconnect/index.php")
    Single<AzkarsRes> getAzkars(
            @Query("lang") String language
    );

    @GET("postgresqlphpconnect/migrants")
    Single<MigrantsRes> getMigrants();

    @GET("/postgresqlphpconnect/qrs")
    Single<QrCodeRes> getQrCodeInfo(
            @Query("user_id") String userId
    );

    @GET("/postgresqlphpconnect/clients/")
    Single<ClientsRes> getClients();
}
