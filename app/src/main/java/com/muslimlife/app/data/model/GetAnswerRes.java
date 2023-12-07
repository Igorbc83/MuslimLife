package com.muslimlife.app.data.model;

public class GetAnswerRes {
    private String receiver_id, question_id, answer_text, answer_date, imam_id, answer_status, question_text, question_date, imam_first_name, imam_last_name,
            imam_middle_name, imam_avater, id;
    private int question_type;

    public GetAnswerRes(String receiver_id, String question_id, String answer_text, String answer_date, String imam_id,
                        String answer_status, String question_text, String question_date, String imam_first_name,
                        String imam_last_name, String imam_middle_name, String imam_avater, String id, int question_type) {
        this.receiver_id = receiver_id;
        this.question_id = question_id;
        this.answer_text = answer_text;
        this.answer_date = answer_date;
        this.imam_id = imam_id;
        this.answer_status = answer_status;
        this.question_text = question_text;
        this.question_date = question_date;
        this.imam_first_name = imam_first_name;
        this.imam_last_name = imam_last_name;
        this.imam_middle_name = imam_middle_name;
        this.imam_avater = imam_avater;
        this.id = id;
        this.question_type = question_type;
    }


    public String getReceiver_id() {
        return receiver_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public String getAnswer_text() {
        return answer_text;
    }

    public String getAnswer_date() {
        return answer_date;
    }

    public String getImam_id() {
        return imam_id;
    }

    public String getAnswer_status() {
        return answer_status;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public String getQuestion_date() {
        return question_date;
    }

    public String getImam_first_name() {
        return imam_first_name;
    }

    public String getImam_last_name() {
        return imam_last_name;
    }

    public String getImam_middle_name() {
        return imam_middle_name;
    }

    public String getImam_avater() {
        return imam_avater;
    }

    public int getQuestion_type() {
        return question_type;
    }

    public String getId() {
        return id;
    }
}
