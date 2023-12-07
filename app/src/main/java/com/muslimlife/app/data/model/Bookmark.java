package com.muslimlife.app.data.model;

public class Bookmark {
    private String number;
    private String surahName;

    public Bookmark(String number, String surahName){
        this.number = number;
        this.surahName = surahName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSurahName() {
        return surahName;
    }

    public void setSurahName(String surahName) {
        this.surahName = surahName;
    }
}
