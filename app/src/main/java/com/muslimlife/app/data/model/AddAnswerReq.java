package com.muslimlife.app.data.model;

public class AddAnswerReq {

    private String user_id, receiver_id, text;
    private int question_id;

    public AddAnswerReq(String user_id, String receiver_id, String text, int question_id) {
        this.user_id = user_id;
        this.receiver_id = receiver_id;
        this.text = text;
        this.question_id = question_id;
    }
}
