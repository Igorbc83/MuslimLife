package com.muslimlife.app.data.model.azkars;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AzkarsRes implements Serializable {

    @SerializedName("morning")
    private List<AzkarModel> morning;

    @SerializedName("evening")
    private List<AzkarModel> evening;

    @SerializedName("important")
    private List<AzkarModel> important;

    @SerializedName("after_namaz")
    private List<AzkarModel> afterNamaz;

    public List<AzkarModel> getMorning() {
        return morning;
    }

    public List<AzkarModel> getEvening() {
        return evening;
    }

    public List<AzkarModel> getImportant() {
        return important;
    }

    public List<AzkarModel> getAfterNamaz() {
        return afterNamaz;
    }
}
