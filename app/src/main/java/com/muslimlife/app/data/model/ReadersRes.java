package com.muslimlife.app.data.model;

public class ReadersRes {
    private String id, name, avatar, status, created;

    public ReadersRes(String id, String name, String avatar, String status, String created) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.status = status;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated() {
        return created;
    }
}
