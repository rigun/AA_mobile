package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KendaraanKonsumenDAO {
    @SerializedName("id")
    @Expose
    public String idKendaraanKonsumen;

    @SerializedName("licensePlate")
    @Expose
    public String platNomorKendaraanKonsumen;

    @SerializedName("vehicle")
    @Expose
    public VehicleDAO detailKendaraanKonsumen;

    public KendaraanKonsumenDAO(String idKendaraanKonsumen, String platNomorKendaraanKonsumen, VehicleDAO detailKendaraanKonsumen) {
        this.idKendaraanKonsumen = idKendaraanKonsumen;
        this.platNomorKendaraanKonsumen = platNomorKendaraanKonsumen;
        this.detailKendaraanKonsumen = detailKendaraanKonsumen;
    }

    public void setIdKendaraanKonsumen(String idKendaraanKonsumen) {
        this.idKendaraanKonsumen = idKendaraanKonsumen;
    }

    public void setPlatNomorKendaraanKonsumen(String platNomorKendaraanKonsumen) {
        this.platNomorKendaraanKonsumen = platNomorKendaraanKonsumen;
    }

    public void setDetailKendaraanKonsumen(VehicleDAO detailKendaraanKonsumen) {
        this.detailKendaraanKonsumen = detailKendaraanKonsumen;
    }

    public String getIdKendaraanKonsumen() {
        return idKendaraanKonsumen;
    }

    public String getPlatNomorKendaraanKonsumen() {
        return platNomorKendaraanKonsumen;
    }

    public VehicleDAO getDetailKendaraanKonsumen() {
        return detailKendaraanKonsumen;
    }
}
