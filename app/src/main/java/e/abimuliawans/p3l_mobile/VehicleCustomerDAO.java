package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleCustomerDAO {
    @SerializedName("licensePlate")
    @Expose
    public String nomorPlat;

    public VehicleCustomerDAO(String nomorPlat) {
        this.nomorPlat = nomorPlat;
    }

    public String getNomorPlat() {
        return nomorPlat;
    }

    public void setNomorPlat(String nomorPlat) {
        this.nomorPlat = nomorPlat;
    }
}
