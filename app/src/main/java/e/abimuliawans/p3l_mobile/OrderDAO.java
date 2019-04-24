package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDAO {
    @SerializedName("supplier_id")
    @Expose
    public Integer idSupplier;

    @SerializedName("branch_id")
    @Expose
    public Integer idCabang;

    @SerializedName("sparepart_code")
    @Expose
    public String sparepartCode;

    @SerializedName("unit")
    @Expose
    public String unit;

    @SerializedName("total")
    @Expose
    public Integer total;

    public OrderDAO(Integer idSupplier, Integer idCabang, String sparepartCode, String unit, Integer total) {
        this.idSupplier = idSupplier;
        this.idCabang = idCabang;
        this.sparepartCode = sparepartCode;
        this.unit = unit;
        this.total = total;
    }

    public Integer getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(Integer idSupplier) {
        this.idSupplier = idSupplier;
    }

    public Integer getIdCabang() {
        return idCabang;
    }

    public void setIdCabang(Integer idCabang) {
        this.idCabang = idCabang;
    }

    public String getSparepartCode() {
        return sparepartCode;
    }

    public void setSparepartCode(String sparepartCode) {
        this.sparepartCode = sparepartCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
