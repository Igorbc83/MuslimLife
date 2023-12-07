package com.muslimlife.app.data.model;

public class TvRes {
    private String id, name, link, image, status_id, created, description;

    public TvRes(String id, String name, String link, String image, String status_id, String created, String description) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.image = image;
        this.status_id = status_id;
        this.created = created;
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

    public String getStatus_id() {
        return status_id;
    }

    public String getCreated() {
        return created;
    }

    public String getDescription() {
        return description;
    }
}
