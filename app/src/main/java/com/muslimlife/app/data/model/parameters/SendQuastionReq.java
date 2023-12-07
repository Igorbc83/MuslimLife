package com.muslimlife.app.data.model.parameters;

public class SendQuastionReq {
    private String user_id, receiver_id, text;
    private int type_id;

    public SendQuastionReq(String user_id, String receiver_id, String text, int type_id) {
        this.user_id = user_id;
        this.receiver_id = receiver_id;
        this.text = text;
        this.type_id = type_id;
    }

    public SendQuastionReq(String user_id, String receiver_id, String text) {
        this.user_id = user_id;
        this.receiver_id = receiver_id;
        this.text = text;
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

    public int getType_id() {
        return type_id;
    }
}
