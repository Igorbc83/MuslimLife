package com.muslimlife.app.data.model;

public class Save64Req {
    private String user, file;

    public Save64Req(String user, String file) {
        this.user = user;
        this.file = "data:image/png;base64,"+file;
    }
}
