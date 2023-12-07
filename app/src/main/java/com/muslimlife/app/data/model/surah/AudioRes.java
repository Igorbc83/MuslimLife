package com.muslimlife.app.data.model.surah;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AudioRes implements Serializable {

    @SerializedName("audio")
    private String audio;

    /*@SerializedName("reader_name")
    private String readerName;*/

    @SerializedName("reader_id")
    private String readerId;

    public String getAudio() {
        return audio;
    }

    /*public String getReaderName() {
        return readerName;
    }*/

    public String getReaderId() {
        return readerId;
    }
}
