package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StokSparepartDAO {
    @SerializedName("id")
    @Expose
    public String idScp;

    @SerializedName("sparepart_code")
    @Expose
    public String codeSpareScp;

    @SerializedName("branch_id")
    @Expose
    public String idCabangScp;

    @SerializedName("buy")
    @Expose
    public String buyScp;

    @SerializedName("sell")
    @Expose
    public String sellScp;

    @SerializedName("position")
    @Expose
    public String positionScp;

    @SerializedName("stock")
    @Expose
    public String stockScp;

    @SerializedName("limitstock")
    @Expose
    public String limitstockScp;

    public StokSparepartDAO(String idScp, String codeSpareScp, String idCabangScp,
                            String buyScp, String sellScp, String positionScp, String stockScp, String limitstockScp) {
        this.idScp = idScp;
        this.codeSpareScp = codeSpareScp;
        this.idCabangScp = idCabangScp;
        this.buyScp = buyScp;
        this.sellScp = sellScp;
        this.positionScp = positionScp;
        this.stockScp = stockScp;
        this.limitstockScp = limitstockScp;
    }

    public String getIdScp() {
        return idScp;
    }

    public String getCodeSpareScp() {
        return codeSpareScp;
    }

    public String getIdCabangScp() {
        return idCabangScp;
    }

    public String getBuyScp() {
        return buyScp;
    }

    public String getSellScp() {
        return sellScp;
    }

    public String getPositionScp() {
        return positionScp;
    }

    public String getStockScp() {
        return stockScp;
    }

    public String getLimitstockScp() {
        return limitstockScp;
    }
}
