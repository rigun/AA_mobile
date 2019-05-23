package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class customerCallBackDAO {
    @SerializedName("cVehicleId")
    @Expose
    public String cVehicleId;

    public customerCallBackDAO(String cVehicleId) {
        this.cVehicleId = cVehicleId;
    }

    public String getcVehicleId() {
        return cVehicleId;
    }

    public void setcVehicleId(String cVehicleId) {
        this.cVehicleId = cVehicleId;
    }
}
