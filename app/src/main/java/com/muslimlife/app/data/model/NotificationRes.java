package com.muslimlife.app.data.model;

public class NotificationRes {
    private String id, user_id, owner, title,text,read,status_id, created, updated;

    public NotificationRes(String id, String user_id, String owner, String title, String text, String read, String status_id, String created, String updated) {
        this.id = id;
        this.user_id = user_id;
        this.owner = owner;
        this.title = title;
        this.text = text;
        this.read = read;
        this.status_id = status_id;
        this.created = created;
        this.updated = updated;
    }

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getRead() {
        return read;
    }

    public String getStatus_id() {
        return status_id;
    }

    public String getCreated() {
        return created;
    }

    public String getUpdated() {
        return updated;
    }
}
