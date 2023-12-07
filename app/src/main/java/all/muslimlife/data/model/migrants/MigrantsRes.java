package all.muslimlife.data.model.migrants;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MigrantsRes {

    @SerializedName("offices")
    private List<MigrantModel> offices;

    public List<MigrantModel> getOffices() {
        return offices;
    }
}
