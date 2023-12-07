package com.muslimlife.app.data.model;

public class ImamRes {

    int photo;
    String name;

    public ImamRes(int photo, String name) {
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
