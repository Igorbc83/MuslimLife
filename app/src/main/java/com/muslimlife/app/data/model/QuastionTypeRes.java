package com.muslimlife.app.data.model;

public class QuastionTypeRes {
    private String id, name,status_id,created;

    public QuastionTypeRes(String id, String name, String status_id, String created) {
        this.id = id;
        this.name = name;
        this.status_id = status_id;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus_id() {
        return status_id;
    }

    public String getCreated() {
        return created;
    }
}
