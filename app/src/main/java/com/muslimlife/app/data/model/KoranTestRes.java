package com.muslimlife.app.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KoranTestRes implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("surah_number")
    private String surahNumber;

    @SerializedName("page")
    private String page;

    @SerializedName("symbols")
    private String symbols;

    @SerializedName("start_ayah")
    private int startAyah;

    @SerializedName("number")
    private String number;

    private boolean clear = false;

    private void cleanPage(){
        symbols = symbols.replace(" ", "");
        page = page.replace(" ", "");

        String[] lines = page.split("\n");
        StringBuilder pageBuilder = new StringBuilder();

        for(int i = 0; i < lines.length; i++) {
            if (!lines[i].isEmpty()) {
                pageBuilder.append(lines[i]);
                if(i != lines.length - 1)
                    pageBuilder.append("\n");
            }
        }

        page = pageBuilder.toString();

        if(number.equals("1") || number.equals("2")){
            page = "\n " +
                    "\n " +
                    "\n " +
                    "\n " +
                    page +
                    "\n " +
                    "\n " +
                    "\n " +
                    "\n ";
        }

        clear = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSurahNumber() {
        if(surahNumber == null || surahNumber.isEmpty())
            surahNumber = "0";
        return Integer.parseInt(surahNumber);
    }

    public void setSurahNumber(String surahNumber) {
        this.surahNumber = surahNumber;
    }

    public String getPage() {
        if(!clear)
            cleanPage();

        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSymbols() {
        return symbols;
    }

    public void setSymbols(String symbols) {
        this.symbols = symbols;
    }

    public int getStartAyah() {
        return startAyah;
    }

    public void setStartAyah(int startAyah) {
        this.startAyah = startAyah;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
