package com.muslimlife.app.data.model.surah;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SurahRes implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("number")
    private String number;

    @SerializedName("page_number")
    private String pageNumber;

    @SerializedName("translate")
    private Map<String, String> translate;

    @SerializedName("audio")
    private List<AudioRes> audios;

    @SerializedName("ayahs")
    private List<AyahRes> ayahs;

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public List<AudioRes> getAudios() {
        return audios;
    }

    public Map<String, String> getTranslate() {
        return translate;
    }

    public List<AyahRes> getAyahs() {
        return ayahs;
    }
}
