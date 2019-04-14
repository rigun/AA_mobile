package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesDAO {

    @SerializedName("id")
    @Expose
    public String idSales;
    @SerializedName("name")
    @Expose
    public String nameSales;
    @SerializedName("phoneNumber")
    @Expose
    public String phoneSales;
    @SerializedName("address")
    @Expose
    public String addressSales;
    @SerializedName("city")
    @Expose
    public String citySales;

    public SalesDAO(String idSales, String nameSales, String phoneSales, String addressSales, String citySales) {
        this.idSales = idSales;
        this.nameSales = nameSales;
        this.phoneSales = phoneSales;
        this.addressSales = addressSales;
        this.citySales = citySales;
    }

    public String getIdSales() {
        return idSales;
    }

    public void setIdSales(String idSales) {
        this.idSales = idSales;
    }

    public String getNameSales() {
        return nameSales;
    }

    public void setNameSales(String nameSales) {
        this.nameSales = nameSales;
    }

    public String getPhoneSales() {
        return phoneSales;
    }

    public void setPhoneSales(String phoneSales) {
        this.phoneSales = phoneSales;
    }

    public String getAddressSales() {
        return addressSales;
    }

    public void setAddressSales(String addressSales) {
        this.addressSales = addressSales;
    }

    public String getCitySales() {
        return citySales;
    }

    public void setCitySales(String citySales) {
        this.citySales = citySales;
    }
}
