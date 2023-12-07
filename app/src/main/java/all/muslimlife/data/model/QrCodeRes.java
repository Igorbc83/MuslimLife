package all.muslimlife.data.model;

import com.google.gson.annotations.SerializedName;

public class QrCodeRes {

    @SerializedName("qr_code")
    private String qrCode;

    @SerializedName("discount")
    private float discount;

    @SerializedName("total")
    private float total;

    public String getQrCode() {
        return qrCode;
    }

    public float getDiscount() {
        return discount;
    }

    public float getTotal() {
        return total;
    }
}
