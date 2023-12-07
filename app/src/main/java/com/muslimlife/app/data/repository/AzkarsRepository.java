package com.muslimlife.app.data.repository;

import java.util.ArrayList;
import java.util.List;

import all.muslimlife.data.model.QrCodeRes;
import all.muslimlife.data.model.migrants.MigrantsRes;
import all.muslimlife.data.model.clients.ClientsRes;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import com.muslimlife.app.data.model.azkars.AzkarModel;
import com.muslimlife.app.data.model.azkars.AzkarsRes;
import com.muslimlife.app.data.network.AzkarsApi;
import com.muslimlife.app.utils.Constants;

public class AzkarsRepository {

    private String language = "en";
    private AzkarsApi api;

    private AzkarsRes azkarsRes;

    private MigrantsRes migrantsRes;

    public AzkarsRepository(AzkarsApi api) {
        this.api = api;
    }

    public Single<AzkarsRes> getAzkars() {
        return api.getAzkars(language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<MigrantsRes> getMigrants() {
        return api.getMigrants()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<QrCodeRes> getQrCodeInfo(String userId) {
        return api.getQrCodeInfo(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ClientsRes> getClients() {
        return api.getClients()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public AzkarsRes getAzkarsRes() {
        return azkarsRes;
    }

    public List<AzkarModel> getAzkarsByType(int type){
        switch (type){
            case Constants.AZKAR_MORNING:
                return azkarsRes.getMorning();
            case Constants.AZKAR_EVENING:
                return azkarsRes.getEvening();
            case Constants.AZKAR_IMPORTANT:
                return azkarsRes.getImportant();
            case Constants.AZKAR_AFTER_NAMAZ:
                return azkarsRes.getAfterNamaz();
            default:
                return new ArrayList<>();
        }
    }

    public void setAzkarsRes(AzkarsRes azkarsRes) {
        this.azkarsRes = azkarsRes;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public MigrantsRes getMigrantsRes() {
        return migrantsRes;
    }

    public void setMigrantsRes(MigrantsRes migrantsRes) {
        this.migrantsRes = migrantsRes;
    }
}
