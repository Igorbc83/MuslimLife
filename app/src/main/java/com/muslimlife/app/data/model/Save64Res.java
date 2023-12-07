package com.muslimlife.app.data.model;

public class Save64Res {
    private String user_id, avatar;

    public Save64Res(String user_id, String avatar) {
        this.user_id = user_id;
        this.avatar = avatar;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getAvatar() {
        return avatar;
    }
}
