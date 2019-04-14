package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CitiesDAO {

    @SerializedName("id")
    @Expose
    public String idCities;
    @SerializedName("province_id")
    @Expose
    public String province_id;
    @SerializedName("name")
    @Expose
    public String nameCities;

    public CitiesDAO(String idCities, String province_id, String nameCities) {
        this.idCities = idCities;
        this.province_id = province_id;
        this.nameCities = nameCities;
    }

    public String getIdCities() {
        return idCities;
    }

    public void setIdCities(String idCities) {
        this.idCities = idCities;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getNameCities() {
        return nameCities;
    }

    public void setNameCities(String nameCities) {
        this.nameCities = nameCities;
    }
}
