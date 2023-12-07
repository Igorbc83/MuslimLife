package all.muslimlife.data.model.clients;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClientsRes {

    @SerializedName("client")
    private List<ClientModel> client;

    public List<ClientModel> getClient() {
        return client;
    }
}
