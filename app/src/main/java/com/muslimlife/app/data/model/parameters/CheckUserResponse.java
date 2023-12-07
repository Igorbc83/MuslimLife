package com.muslimlife.app.data.model.parameters;

public class CheckUserResponse {

    private String id;
    private String phone;
    private String profile_id;
    private String token;
    private final String is_user;
    private String email;
    private String noti_sound;
    private boolean push_receive;

    public CheckUserResponse(String id, String phone, String profile_id, String token, String is_user, String email, String noti_sound, boolean push_receive) {
        this.id = id;
        this.phone = phone;
        this.profile_id = profile_id;
        this.token = token;
        this.is_user = is_user;
        this.email = email;
        this.noti_sound = noti_sound;
        this.push_receive = push_receive;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIs_user() {
        return is_user;
    }

    public String getNoti_sound() { return noti_sound; }

    public boolean isPush_receive() { return push_receive; }
}
