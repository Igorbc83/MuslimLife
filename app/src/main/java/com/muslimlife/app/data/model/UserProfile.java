package com.muslimlife.app.data.model;

public class UserProfile {

    private String id;
    private String name;
    private String lastname;
    private String middlename;
    private String phone;
    private String email;
    private String birthday;
    private String country;
    private String city;
    private String avatar;
    private double latitude;
    private  double longitude;
    private double lat;
    private double lon;
    private boolean subscribed;
    private String noti_sound;
    private Boolean push_receive;

    private String description;

    public UserProfile(){
    }

    public UserProfile(String id, String name, String lastname, String middlename, String phone, String email,
                       String birthday, String country, String city, String avatar, double latitude, double longitude,
                       double lat, double lon, boolean subscribed, String noti_sound, boolean push_receive) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.middlename = middlename;
        this.phone = phone;
        this.email = email;
        this.birthday = birthday;
        this.country = country;
        this.city = city;
        this.avatar = avatar;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lat = lat;
        this.lon = lon;
        this.subscribed = subscribed;
        this.noti_sound = noti_sound;
        this.push_receive = push_receive;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return String.format("%s %s", name, lastname);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastName) {
        this.lastname = lastName;
    }

    public String getMiddleName() {
        return middlename;
    }

    public void setMiddleName(String middleName) {
        this.middlename = middleName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String countryId) {
        this.country = countryId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public String getNoti_sound() { return this.noti_sound; }

    public boolean isPush_receive() { return push_receive!=null?push_receive:false; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
