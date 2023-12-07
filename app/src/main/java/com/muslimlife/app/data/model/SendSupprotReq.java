package com.muslimlife.app.data.model;

public class SendSupprotReq {
    private String user_id, text;

    public SendSupprotReq(String user_id, String text) {
        this.user_id = user_id;
        this.text = text;
    }
}
