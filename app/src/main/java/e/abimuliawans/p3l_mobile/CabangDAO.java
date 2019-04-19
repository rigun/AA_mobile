package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CabangDAO {

    @SerializedName("id")
    @Expose
    public String idCabang;
    @SerializedName("name")
    @Expose
    public String namaCabang;

    public CabangDAO(String idCabang, String namaCabang) {
        this.idCabang = idCabang;
        this.namaCabang = namaCabang;
    }

    public String getIdCabang() {
        return idCabang;
    }

    public void setIdCabang(String idCabang) {
        this.idCabang = idCabang;
    }

    public String getNamaCabang() {
        return namaCabang;
    }

    public void setNamaCabang(String namaCabang) {
        this.namaCabang = namaCabang;
    }
}
