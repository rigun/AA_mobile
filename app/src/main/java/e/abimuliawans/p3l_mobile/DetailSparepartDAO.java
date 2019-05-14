package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailSparepartDAO {
    @SerializedName("id")
    @Expose
    public String idDetailSpare;

    @SerializedName("trasanctiondetail_id")
    @Expose
    public String transDetailId;

    @SerializedName("sparepart_code")
    @Expose
    public String codeSpare;

    @SerializedName("total")
    @Expose
    public String totalSpare;

    @SerializedName("price")
    @Expose
    public String price;

    @SerializedName("sparepart")
    @Expose
    public String sparepart;

    public DetailSparepartDAO(String idDetailSpare, String transDetailId,
                              String codeSpare, String totalSpare, String price, String sparepart) {
        this.idDetailSpare = idDetailSpare;
        this.transDetailId = transDetailId;
        this.codeSpare = codeSpare;
        this.totalSpare = totalSpare;
        this.price = price;
        this.sparepart = sparepart;
    }

    public String getIdDetailSpare() {
        return idDetailSpare;
    }

    public void setIdDetailSpare(String idDetailSpare) {
        this.idDetailSpare = idDetailSpare;
    }

    public String getTransDetailId() {
        return transDetailId;
    }

    public void setTransDetailId(String transDetailId) {
        this.transDetailId = transDetailId;
    }

    public String getCodeSpare() {
        return codeSpare;
    }

    public void setCodeSpare(String codeSpare) {
        this.codeSpare = codeSpare;
    }

    public String getTotalSpare() {
        return totalSpare;
    }

    public void setTotalSpare(String totalSpare) {
        this.totalSpare = totalSpare;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSparepart() {
        return sparepart;
    }

    public void setSparepart(String sparepart) {
        this.sparepart = sparepart;
    }
}
