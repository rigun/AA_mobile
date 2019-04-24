package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDataDAO {
    @SerializedName("id")
    @Expose
    public Integer idOrder;

    @SerializedName("sparepart_code")
    @Expose
    public String code_Sparepart;

    @SerializedName("branch_id")
    @Expose
    public String idCabang;

    @SerializedName("created_at")
    @Expose
    public String tanggal;


    public Integer getIdOrder() {
        return idOrder;
    }

    public String getCode_Sparepart() {
        return code_Sparepart;
    }

    public String getIdCabang() {
        return idCabang;
    }

    public String getTanggal() {
        return tanggal;
    }
}
