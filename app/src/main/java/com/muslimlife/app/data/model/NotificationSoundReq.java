package com.muslimlife.app.data.model;

public class NotificationSoundReq {

    private String android_channel_id;
    private int id;

    public NotificationSoundReq(String sound, int userid){
        this.android_channel_id = sound;
        this.id = userid;
    }



}
