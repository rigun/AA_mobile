package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShowPemesananDAO {

    @SerializedName("id")
    @Expose
    public String idPemesanan;

    @SerializedName("supplier_id")
    @Expose
    public String idSupplierPemesanan;

    @SerializedName("branch_id")
    @Expose
    public String idCabangPemesanan;

    @SerializedName("created_at")
    @Expose
    public String tanggalPemesanan;

    @SerializedName("detail")
    @Expose
    public List<DetailOrderDAO> detailPemesanan;


    public ShowPemesananDAO(String idPemesanan, String idSupplierPemesanan, String idCabangPemesanan, String tanggalPemesanan) {
        this.idPemesanan = idPemesanan;
        this.idSupplierPemesanan = idSupplierPemesanan;
        this.idCabangPemesanan = idCabangPemesanan;
        this.tanggalPemesanan = tanggalPemesanan;
    }

    public String getIdPemesanan() {
        return idPemesanan;
    }

    public void setIdPemesanan(String idPemesanan) {
        this.idPemesanan = idPemesanan;
    }

    public String getIdSupplierPemesanan() {
        return idSupplierPemesanan;
    }

    public void setIdSupplierPemesanan(String idSupplierPemesanan) {
        this.idSupplierPemesanan = idSupplierPemesanan;
    }

    public String getIdCabangPemesanan() {
        return idCabangPemesanan;
    }

    public void setIdCabangPemesanan(String idCabangPemesanan) {
        this.idCabangPemesanan = idCabangPemesanan;
    }

    public String getTanggalPemesanan() {
        return tanggalPemesanan;
    }

    public void setTanggalPemesanan(String tanggalPemesanan) {
        this.tanggalPemesanan = tanggalPemesanan;
    }

    public List<DetailOrderDAO> getDetailPemesanan() {
        return detailPemesanan;
    }
}
