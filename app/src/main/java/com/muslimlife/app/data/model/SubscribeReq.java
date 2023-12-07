package com.muslimlife.app.data.model;

public class SubscribeReq {
    private int id;
    private boolean subscribed;

    public SubscribeReq(String id, boolean subscribed) {
        this.id = Integer.parseInt(id);
        this.subscribed = subscribed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
