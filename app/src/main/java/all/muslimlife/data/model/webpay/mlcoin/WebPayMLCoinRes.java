package all.muslimlife.data.model.webpay.mlcoin;

import com.google.gson.annotations.SerializedName;

public class WebPayMLCoinRes {

    @SerializedName("balance")
    private String balance;

    public String getBalance() {
        if(balance == null || balance.isEmpty() || balance.contains("null"))
            balance = "0";
        return balance;
    }
}
