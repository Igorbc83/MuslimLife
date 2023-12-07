package all.muslimlife.data.model.webpay.sub;

import com.google.gson.annotations.SerializedName;

public class WebPaySubRes {

    @SerializedName("end_date")
    private String endDate;

    public String getEndDate() {
        return endDate;
    }
}
