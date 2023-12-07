package com.muslimlife.app.data.model;

public class DiaspoarResponce {
    private String id, name, imam_id, vk, instagram, status_id, created,imam_name,imam_lastname,imam_middlename, imam_phone, imam_avatar, imam_bio, image;

    public DiaspoarResponce(String id, String name, String imam_id, String vk, String instagram, String status_id, String created, String imam_name, String imam_lastname, String imam_middlename, String imam_phone, String imam_avatar, String imam_bio, String image) {
        this.id = id;
        this.name = name;
        this.imam_id = imam_id;
        this.vk = vk;
        this.instagram = instagram;
        this.status_id = status_id;
        this.created = created;
        this.imam_name = imam_name;
        this.imam_lastname = imam_lastname;
        this.imam_middlename = imam_middlename;
        this.imam_phone = imam_phone;
        this.imam_avatar = imam_avatar;
        this.imam_bio = imam_bio;
        this.image = image;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImam_id() {
        return imam_id;
    }

    public String getVk() {
        return vk;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getStatus_id() {
        return status_id;
    }

    public String getCreated() {
        return created;
    }

    public String getImam_name() {
        return imam_name;
    }

    public String getImam_lastname() {
        return imam_lastname;
    }

    public String getImam_middlename() {
        return imam_middlename;
    }

    public String getImam_phone() {
        return imam_phone;
    }

    public String getImam_avatar() {
        return imam_avatar;
    }

    public String getImam_bio() {
        return imam_bio;
    }

    public String getImage() {
        return image;
    }
}
