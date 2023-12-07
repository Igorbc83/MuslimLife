package com.muslimlife.app.data.model.parameters;

public class RegistrationParameters {

    private String email;
    private String name;
    private String uid;

    public RegistrationParameters(String email, String name, String uid) {
        this.email = email;
        this.name = name;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return email;
    }

    public void setPhone(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
