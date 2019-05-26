package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailOrderDAO {
    @SerializedName("sparepart_code")
    @Expose
    public String sparepartDetail;

    @SerializedName("unit")
    @Expose
    public String unit;

    @SerializedName("total")
    @Expose
    public String totalDetail;

    @SerializedName("buy")
    @Expose
    public String buy;

    public String getSparepartDetail() {
        return sparepartDetail;
    }

    public String getUnit() {
        return unit;
    }

    public String getTotalDetail() {
        return totalDetail;
    }

    public String getBuy() {
        return buy;
    }
}
