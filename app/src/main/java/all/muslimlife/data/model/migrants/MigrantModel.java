package all.muslimlife.data.model.migrants;

import com.google.gson.annotations.SerializedName;

public class MigrantModel {

    @SerializedName("office_id")
    private String officeId;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("contact_phone")
    private String contactPhone;

    @SerializedName("office_address")
    private String officeAddress;

    @SerializedName("office_email")
    private String officeEmail;

    @SerializedName("website")
    private String website;

    @SerializedName("img_link")
    private String imgLink;

    public String getOfficeId() {
        return officeId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public String getOfficeEmail() {
        return officeEmail;
    }

    public String getWebsite() {
        return website;
    }

    public String getImgLink() {
        return imgLink;
    }
}
