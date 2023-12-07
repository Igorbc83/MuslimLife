package all.muslimlife.data.model.chatai;

import com.google.gson.annotations.SerializedName;

public class ChatAIReq {

    @SerializedName("dataToSend")
    private String dataToSend;

    public ChatAIReq(String dataToSend) {
        this.dataToSend = dataToSend;
    }

    public String getDataToSend() {
        return dataToSend;
    }

    public void setDataToSend(String dataToSend) {
        this.dataToSend = dataToSend;
    }
}
