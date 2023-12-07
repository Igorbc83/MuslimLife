package com.muslimlife.app.data.model;

public class WallpaperRes {
    private String id, image, status_id, created;
    private boolean checked=false;

    public WallpaperRes(String id, String image, String status_id, String created) {
        this.id = id;
        this.image = image;
        this.status_id = status_id;
        this.created = created;
    }

    public String getId() {
        return id;
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

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }
}
