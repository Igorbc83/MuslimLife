package com.muslimlife.app.data.model.parameters;

public class UpdateProfileResponse {

    private String id;

    public UpdateProfileResponse() {
    }

    public UpdateProfileResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
