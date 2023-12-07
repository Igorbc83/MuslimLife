package com.muslimlife.app.data.model;

public class PlacesRes {
    public String id, name, address;
    public String[] schedule;
    public String[] images;
    public String description,phone,instagram,type,created,vk,telegram,whatsapp,lat,lon;
    public int status_id;

    private boolean isFulInfo = false;

    public PlacesRes(){}

    public PlacesRes(String id, String name, String address, String[] schedule, String[] images, String description, String phone, String instagram, String type, String created, String vk, String telegram, String whatsapp, String lat, String lon, int status_id) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.schedule = schedule;
        this.images = images;
        this.description = description;
        this.phone = phone;
        this.instagram = instagram;
        this.type = type;
        this.created = created;
        this.vk = vk;
        this.telegram = telegram;
        this.whatsapp = whatsapp;
        this.lat = lat;
        this.lon = lon;
        this.status_id = status_id;
    }

    public boolean isFulInfo() {
        return isFulInfo;
    }

    public void setFulInfo(boolean fulInfo) {
        isFulInfo = fulInfo;
    }
}
