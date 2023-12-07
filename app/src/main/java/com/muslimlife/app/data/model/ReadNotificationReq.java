package com.muslimlife.app.data.model;

public class ReadNotificationReq {
    private String id, read;

    public ReadNotificationReq(String id, String read) {
        this.id = id;
        this.read = read;
    }

    public String getId() {
        return id;
    }

    public String getRead() {
        return read;
    }
}
