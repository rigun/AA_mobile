package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleDAO {

    @SerializedName("merk")
    @Expose
    public String merkVehicle;
    @SerializedName("type")
    @Expose
    public String typeVehicle;

    public VehicleDAO(String merkVehicle, String typeVehicle) {
        this.merkVehicle = merkVehicle;
        this.typeVehicle = typeVehicle;
    }

    public String getMerkVehicle() {
        return merkVehicle;
    }

    public void setMerkVehicle(String merkVehicle) {
        this.merkVehicle = merkVehicle;
    }

    public String getTypeVehicle() {
        return typeVehicle;
    }

    public void setTypeVehicle(String typeVehicle) {
        this.typeVehicle = typeVehicle;
    }
}
