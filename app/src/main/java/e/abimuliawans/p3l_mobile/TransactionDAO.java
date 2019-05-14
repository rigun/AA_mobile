package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionDAO {
    @SerializedName("id")
    @Expose
    public String idKonsumenTrans;
    @SerializedName("name")
    @Expose
    public String nameKonsumenTrans;
    @SerializedName("phoneNumber")
    @Expose
    public String phoneKonsumenTrans;
    @SerializedName("address")
    @Expose
    public String addressKonsumenTrans;
    @SerializedName("city")
    @Expose
    public String cityKonsumenTrans;
    @SerializedName("branch")
    @Expose
    public String cabangTrans;
    @SerializedName("role")
    @Expose
    public String roleTrans;
    @SerializedName("jenisTransaksi")
    @Expose
    public String jenisTransaksi;
    @SerializedName("customer")
    @Expose
    public KonsumenDAO konsumenTrans;
    @SerializedName("data")
    @Expose
    public DetailSparepartDAO detailSparepart;

    public KonsumenDAO getKonsumenTrans() {
        return konsumenTrans;
    }

    public void setKonsumenTrans(KonsumenDAO konsumenTrans) {
        this.konsumenTrans = konsumenTrans;
    }

    public String getIdKonsumenTrans() {
        return idKonsumenTrans;
    }

    public void setIdKonsumenTrans(String idKonsumenTrans) {
        this.idKonsumenTrans = idKonsumenTrans;
    }

    public String getNameKonsumenTrans() {
        return nameKonsumenTrans;
    }

    public void setNameKonsumenTrans(String nameKonsumenTrans) {
        this.nameKonsumenTrans = nameKonsumenTrans;
    }

    public String getPhoneKonsumenTrans() {
        return phoneKonsumenTrans;
    }

    public void setPhoneKonsumenTrans(String phoneKonsumenTrans) {
        this.phoneKonsumenTrans = phoneKonsumenTrans;
    }

    public String getAddressKonsumenTrans() {
        return addressKonsumenTrans;
    }

    public void setAddressKonsumenTrans(String addressKonsumenTrans) {
        this.addressKonsumenTrans = addressKonsumenTrans;
    }

    public String getCityKonsumenTrans() {
        return cityKonsumenTrans;
    }

    public void setCityKonsumenTrans(String cityKonsumenTrans) {
        this.cityKonsumenTrans = cityKonsumenTrans;
    }

    public String getCabangTrans() {
        return cabangTrans;
    }

    public void setCabangTrans(String cabangTrans) {
        this.cabangTrans = cabangTrans;
    }

    public String getRoleTrans() {
        return roleTrans;
    }

    public void setRoleTrans(String roleTrans) {
        this.roleTrans = roleTrans;
    }

    public String getJenisTransaksi() {
        return jenisTransaksi;
    }

    public void setJenisTransaksi(String jenisTransaksi) {
        this.jenisTransaksi = jenisTransaksi;
    }

    public DetailSparepartDAO getDetailSparepart() {
        return detailSparepart;
    }

    public void setDetailSparepart(DetailSparepartDAO detailSparepart) {
        this.detailSparepart = detailSparepart;
    }
}
