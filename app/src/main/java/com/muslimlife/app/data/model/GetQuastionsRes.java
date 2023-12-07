package com.muslimlife.app.data.model;

public class GetQuastionsRes {
    private String id;
    private String user_id;
    private String receiver_id;
    private String text;
    private String approved;
    private String status_id;
    private String created;
    private String updated;
    private String answered;
    private String user_name;
    private String user_lastname;
    private String user_avatar;
    private int type_id;

    public GetQuastionsRes(String id, String user_id, String receiver_id, String text, String approved,
                           String status_id, String created, String updated, String answered, String user_name,
                           String user_lastname, String user_avatar, int type_id) {
        this.id = id;
        this.user_id = user_id;
        this.receiver_id = receiver_id;
        this.text = text;
        this.approved = approved;
        this.status_id = status_id;
        this.created = created;
        this.updated = updated;
        this.answered = answered;
        this.user_name = user_name;
        this.user_lastname = user_lastname;
        this.user_avatar = user_avatar;
        this.type_id = type_id;
    }


    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public String getText() {
        return text;
    }

    public String getApproved() {
        return approved;
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

    public String getAnswered() {
        return answered;
    }

    public int getType_id() {
        return type_id;
    }

    public String getFullName(){return user_name + " " + user_lastname;}

    public String getUser_avatar() {
        return user_avatar;
    }
}
