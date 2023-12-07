package com.muslimlife.app.data.model;

public class PushTimeReq {
    private final String id;
    private final PushTime push_time;

    public PushTimeReq(String id, PushTime push_time) {
        this.id = id;
        this.push_time = push_time;
    }
}
