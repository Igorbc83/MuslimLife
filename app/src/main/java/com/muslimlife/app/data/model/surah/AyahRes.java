package com.muslimlife.app.data.model.surah;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class AyahRes implements Serializable {

    @SerializedName("number")
    private String number;

    @SerializedName("translate")
    private Map<String, String> translate;

    @SerializedName("audio")
    private List<AudioRes> audios;

    public String getNumber() {
        return number;
    }

    public List<AudioRes> getAudios() {
        return audios;
    }

    public Map<String, String> getTranslate() {
        return translate;
    }
}
