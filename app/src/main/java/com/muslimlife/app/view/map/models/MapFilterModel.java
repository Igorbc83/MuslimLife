package com.muslimlife.app.view.map.models;

import com.muslimlife.app.view.map.PlaceTypes;

public class MapFilterModel {

    private String name;
    private PlaceTypes type;
    private String typeStr;
    private String nameStr;

    public MapFilterModel(String name, PlaceTypes type, String typeStr) {
        this.name = name;
        this.nameStr = name;
        this.type = type;
        this.typeStr = typeStr;
    }

    public MapFilterModel(String name, PlaceTypes type, String typeStr, String nameStr) {
        this.name = name;
        this.nameStr = nameStr;
        this.type = type;
        this.typeStr = typeStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlaceTypes getType() {
        return type;
    }

    public void setType(PlaceTypes type) {
        this.type = type;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public String getNameStr() {
        return nameStr;
    }

    public void setNameStr(String nameStr) {
        this.nameStr = nameStr;
    }
}
