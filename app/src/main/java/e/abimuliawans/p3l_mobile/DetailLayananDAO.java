package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailLayananDAO {
    @SerializedName("id")
    @Expose
    public String idDetailLayanan;

    @SerializedName("service")
    @Expose
    public ServiceDAO serviceDAO;

    @SerializedName("total")
    @Expose
    public String totalLayanan;

    @SerializedName("price")
    @Expose
    public String price;

    public DetailLayananDAO(String idDetailLayanan, ServiceDAO serviceDAO, String totalLayanan, String price) {
        this.idDetailLayanan = idDetailLayanan;
        this.serviceDAO = serviceDAO;
        this.totalLayanan = totalLayanan;
        this.price = price;
    }

    public String getIdDetailLayanan() {
        return idDetailLayanan;
    }

    public void setIdDetailLayanan(String idDetailLayanan) {
        this.idDetailLayanan = idDetailLayanan;
    }

    public ServiceDAO getServiceDAO() {
        return serviceDAO;
    }

    public void setServiceDAO(ServiceDAO serviceDAO) {
        this.serviceDAO = serviceDAO;
    }

    public String getTotalLayanan() {
        return totalLayanan;
    }

    public void setTotalLayanan(String totalLayanan) {
        this.totalLayanan = totalLayanan;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
