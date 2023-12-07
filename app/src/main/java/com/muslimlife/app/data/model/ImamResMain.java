package com.muslimlife.app.data.model;

public class ImamResMain {

    private String id, first_name, last_name, middle_name, description, bio, phone, avatar, status_id, created, updated;
    private String[] types, languages;

    public ImamResMain(String id, String first_name, String last_name, String middle_name, String description, String bio, String phone, String avatar, String status_id, String created, String updated, String[] types, String[] languages) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.middle_name = middle_name;
        this.description = description;
        this.bio = bio;
        this.phone = phone;
        this.avatar = avatar;
        this.status_id = status_id;
        this.created = created;
        this.updated = updated;
        this.types = types;
        this.languages = languages;
    }

    public String getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public String getDescription() {
        return description;
    }

    public String getBio() {
        return bio;
    }

    public String getPhone() {
        return phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getStatus_id() {
        return status_id;
    }

    public String getCreated() {
        return created;
    }

    public String getUpdated() {
        return updated;
    }

    public String[] getTypes() {
        return types;
    }

    public String[] getLanguages() {
        return languages;
    }
}
