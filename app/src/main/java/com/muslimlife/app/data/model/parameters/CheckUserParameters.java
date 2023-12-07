package com.muslimlife.app.data.model.parameters;

public class CheckUserParameters {

    private String email;

    public CheckUserParameters(String email) {
        this.email = email;
    }

    public String getPhone() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
