package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SupplierDAO {

    @SerializedName("id")
    @Expose
    public String idSupplier;
    @SerializedName("name")
    @Expose
    public String nameSupplier;
    @SerializedName("phoneNumber")
    @Expose
    public String phoneSupplier;
    @SerializedName("address")
    @Expose
    public String addressSupplier;
    @SerializedName("city")
    @Expose
    public String citySupplier;

    public SupplierDAO(String idSupplier, String nameSupplier, String phoneSupplier, String addressSupplier, String citySupplier) {
        this.idSupplier = idSupplier;
        this.nameSupplier = nameSupplier;
        this.phoneSupplier = phoneSupplier;
        this.addressSupplier = addressSupplier;
        this.citySupplier = citySupplier;
    }

    public String getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(String idSupplier) {
        this.idSupplier = idSupplier;
    }

    public String getNameSupplier() {
        return nameSupplier;
    }

    public void setNameSupplier(String nameSupplier) {
        this.nameSupplier = nameSupplier;
    }

    public String getAddressSupplier() {
        return addressSupplier;
    }

    public void setAddressSupplier(String addressSupplier) {
        this.addressSupplier = addressSupplier;
    }

    public String getCitySupplier() {
        return citySupplier;
    }

    public void setCitySupplier(String citySupplier) {
        this.citySupplier = citySupplier;
    }

    public String getPhoneSupplier() {
        return phoneSupplier;
    }

    public void setPhoneSupplier(String phoneSupplier) {
        this.phoneSupplier = phoneSupplier;
    }
}
