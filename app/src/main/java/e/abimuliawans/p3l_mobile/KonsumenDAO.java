package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KonsumenDAO {
    @SerializedName("id")
    @Expose
    public String idKonsumen;
    @SerializedName("name")
    @Expose
    public String nameKonsumen;
    @SerializedName("phoneNumber")
    @Expose
    public String phoneKonsumen;
    @SerializedName("address")
    @Expose
    public String addressKonsumen;
    @SerializedName("city")
    @Expose
    public String cityKonsumen;

    public KonsumenDAO(String idKonsumen, String nameKonsumen, String phoneKonsumen, String addressKonsumen, String cityKonsumen) {
        this.idKonsumen = idKonsumen;
        this.nameKonsumen = nameKonsumen;
        this.phoneKonsumen = phoneKonsumen;
        this.addressKonsumen = addressKonsumen;
        this.cityKonsumen = cityKonsumen;
    }

    public String getIdKonsumen() {
        return idKonsumen;
    }

    public void setIdKonsumen(String idKonsumen) {
        this.idKonsumen = idKonsumen;
    }

    public String getNameKonsumen() {
        return nameKonsumen;
    }

    public void setNameKonsumen(String nameKonsumen) {
        this.nameKonsumen = nameKonsumen;
    }

    public String getPhoneKonsumen() {
        return phoneKonsumen;
    }

    public void setPhoneKonsumen(String phoneKonsumen) {
        this.phoneKonsumen = phoneKonsumen;
    }

    public String getAddressKonsumen() {
        return addressKonsumen;
    }

    public void setAddressKonsumen(String addressKonsumen) {
        this.addressKonsumen = addressKonsumen;
    }

    public String getCityKonsumen() {
        return cityKonsumen;
    }

    public void setCityKonsumen(String cityKonsumen) {
        this.cityKonsumen = cityKonsumen;
    }
}
