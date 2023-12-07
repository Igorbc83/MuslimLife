package com.muslimlife.app.data.model.azkars;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AzkarModel implements Serializable {

    @SerializedName("azkar_chapter")
    private String azkarChapter;

    @SerializedName("azkar_header")
    private String azkarHeader;

    @SerializedName("azkar_arabic")
    private String azkarArabic;

    @SerializedName("azkar_text")
    private String azkarText;

    @SerializedName("azkar_transcription")
    private String azkarTranscription;

    @SerializedName("azkar_audio")
    private String azkarAudio;

    public String getAzkarChapter() {
        return azkarChapter;
    }

    public String getAzkarHeader() {
        return azkarHeader;
    }

    public String getAzkarArabic() {
        return azkarArabic;
    }

    public String getAzkarText() {
        return azkarText;
    }

    public String getAzkarTranscription() {
        return azkarTranscription;
    }

    public String getAzkarAudio() {
        return azkarAudio;
    }
}
