package com.muslimlife.app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserLocation implements Parcelable {

    private String id;
    private String code;
    private String name;
    private String country_id;
    private double latitude;
    private  double longitude;
    private boolean selected = false;

    public UserLocation(String code) {
        this.code = code;
    }

    protected UserLocation(Parcel in) {
        id = in.readString();
        code = in.readString();
        name = in.readString();
        country_id = in.readString();
    }

    public static final Creator<UserLocation> CREATOR = new Creator<UserLocation>() {
        @Override
        public UserLocation createFromParcel(Parcel in) {
            return new UserLocation(in);
        }

        @Override
        public UserLocation[] newArray(int size) {
            return new UserLocation[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getCountryId() {
        return country_id;
    }

    public void setCountryId(String country_id) {
        this.country_id = country_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(country_id);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
