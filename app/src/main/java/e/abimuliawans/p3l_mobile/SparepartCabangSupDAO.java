package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SparepartCabangSupDAO {
    @SerializedName("id")
    @Expose
    public Integer idScp;

    @SerializedName("sparepart_code")
    @Expose
    public String codeSpareScp;

    @SerializedName("branch_id")
    @Expose
    public Integer idCabangScp;

    @SerializedName("buy")
    @Expose
    public Double buyScp;

    @SerializedName("sell")
    @Expose
    public Double sellScp;

    @SerializedName("position")
    @Expose
    public String positionScp;

    @SerializedName("stock")
    @Expose
    public Integer stockScp;

    @SerializedName("limitstock")
    @Expose
    public Integer limitstockScp;

    public SparepartCabangSupDAO(Integer idScp, String codeSpareScp, Integer idCabangScp,
                                 Double buyScp, Double sellScp, String positionScp, Integer stockScp,
                                 Integer limitstockScp) {
        this.idScp = idScp;
        this.codeSpareScp = codeSpareScp;
        this.idCabangScp = idCabangScp;
        this.buyScp = buyScp;
        this.sellScp = sellScp;
        this.positionScp = positionScp;
        this.stockScp = stockScp;
        this.limitstockScp = limitstockScp;
    }

    public Integer getIdScp() {
        return idScp;
    }

    public void setIdScp(Integer idScp) {
        this.idScp = idScp;
    }

    public String getCodeSpareScp() {
        return codeSpareScp;
    }

    public void setCodeSpareScp(String codeSpareScp) {
        this.codeSpareScp = codeSpareScp;
    }

    public Integer getIdCabangScp() {
        return idCabangScp;
    }

    public void setIdCabangScp(Integer idCabangScp) {
        this.idCabangScp = idCabangScp;
    }

    public Double getBuyScp() {
        return buyScp;
    }

    public void setBuyScp(Double buyScp) {
        this.buyScp = buyScp;
    }

    public Double getSellScp() {
        return sellScp;
    }

    public void setSellScp(Double sellScp) {
        this.sellScp = sellScp;
    }

    public String getPositionScp() {
        return positionScp;
    }

    public void setPositionScp(String positionScp) {
        this.positionScp = positionScp;
    }

    public Integer getStockScp() {
        return stockScp;
    }

    public void setStockScp(Integer stockScp) {
        this.stockScp = stockScp;
    }

    public Integer getLimitstockScp() {
        return limitstockScp;
    }

    public void setLimitstockScp(Integer limitstockScp) {
        this.limitstockScp = limitstockScp;
    }
}
