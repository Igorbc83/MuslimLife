package all.muslimlife.data.model.clients;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ClientModel {

    @SerializedName("client_id")
    private String officeId;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("openhours")
    private String openhours;

    @SerializedName("main_dishes")
    private String mainDishes;

    @SerializedName("aov")
    private String aov;

    @SerializedName("dostavka")
    private String dostavka;

    @SerializedName("address")
    private String address;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("website")
    private String website;

    @SerializedName("img_main")
    private String imgMain;

    @SerializedName("img_1")
    private String img_1;

    @SerializedName("img_2")
    private String img_2;

    @SerializedName("img_3")
    private String img_3;

    @SerializedName("img_4")
    private String img_4;

    @SerializedName("img_5")
    private String img_5;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("org_type")
    private String orgType;

    public List<String> getImages(){
        List<String> images = new ArrayList<>();

        if(imgMain != null && !imgMain.isEmpty())
            images.add(imgMain);

        if(img_1 != null && !img_1.isEmpty())
            images.add(img_1);

        if(img_2 != null && !img_2.isEmpty())
            images.add(img_2);

        if(img_3 != null && !img_3.isEmpty())
            images.add(img_3);

        if(img_4 != null && !img_4.isEmpty())
            images.add(img_4);

        if(img_5 != null && !img_5.isEmpty())
            images.add(img_5);

        return images;
    }

    public String getOfficeId() {
        return officeId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getOpenhours() {
        return openhours;
    }

    public String getMainDishes() {
        return mainDishes;
    }

    public String getAov() {
        return aov;
    }

    public String getDostavka() {
        return dostavka;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public String getImgMain() {
        return imgMain;
    }

    public String getImg_1() {
        return img_1;
    }

    public String getImg_2() {
        return img_2;
    }

    public String getImg_3() {
        return img_3;
    }

    public String getImg_4() {
        return img_4;
    }

    public String getImg_5() {
        return img_5;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getOrgType() {
        return orgType;
    }
}
