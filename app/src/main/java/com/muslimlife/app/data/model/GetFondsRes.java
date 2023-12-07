package com.muslimlife.app.data.model;

public class GetFondsRes {
    private String id, name, description, created;
    private int summ, status_id;

    public GetFondsRes(String id, String name, String description, String created, int summ, int status_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.created = created;
        this.summ = summ;
        this.status_id = status_id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated() {
        return created;
    }

    public int getSumm() {
        return summ;
    }

    public int getStatus_id() {
        return status_id;
    }
}
