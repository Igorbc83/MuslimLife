package com.muslimlife.app.data.model;

public class DiaspoarRes {

    int photo;
    String name;

    public DiaspoarRes(int photo, String name) {
        this.photo = photo;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPhoto() {
        return photo;
    }
}
