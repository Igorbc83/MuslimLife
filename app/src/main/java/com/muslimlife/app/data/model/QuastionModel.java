package com.muslimlife.app.data.model;

public class QuastionModel {
    private GetQuastionsRes quastionsRes;
    private String name, avatar;

    public QuastionModel(GetQuastionsRes quastionsRes, String name, String avatar) {
        this.quastionsRes = quastionsRes;
        this.name = name;
        this.avatar = avatar;
    }

    public QuastionModel(GetQuastionsRes quastionsRes) {
        this.quastionsRes = quastionsRes;
    }

    public GetQuastionsRes getQuastionsRes() {
        return quastionsRes;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
