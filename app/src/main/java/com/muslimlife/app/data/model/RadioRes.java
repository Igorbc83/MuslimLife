package com.muslimlife.app.data.model;

public class RadioRes {
    private String id;
    private String name;
    private String link;
    private String image;
    private String created;
    private String description;
    private int status_id;

    public RadioRes(String id, String name, String link, String image, String created, int status_id,String description) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.image = image;
        this.created = created;
        this.status_id = status_id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getImage() {
        return image;
    }

    public String getCreated() {
        return created;
    }

    public int getStatus_id() {
        return status_id;
    }


    public String getDescription() {
        return description;
    }
}
