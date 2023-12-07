package com.muslimlife.app.data.model;

public class NotificationReceiveReq {

    private boolean push_receive;
    private int id;

    public NotificationReceiveReq(boolean isReceive, int userid){
        this.push_receive = isReceive;
        this.id = userid;
    }



}
