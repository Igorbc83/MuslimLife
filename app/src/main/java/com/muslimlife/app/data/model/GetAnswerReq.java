package com.muslimlife.app.data.model;

public class GetAnswerReq {
    String user_id;

    public GetAnswerReq(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }
}
