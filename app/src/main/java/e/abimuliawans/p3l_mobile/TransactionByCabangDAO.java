package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionByCabangDAO {
    @SerializedName("id")
    @Expose
    public String idTransaction;
    @SerializedName("transactionNumber")
    @Expose
    public String transactionNumber;
    @SerializedName("totalServices")
    @Expose
    public String totalServices;
    @SerializedName("totalSpareparts")
    @Expose
    public String totalSpareparts;
    @SerializedName("totalCost")
    @Expose
    public String totalCost;
    @SerializedName("payment")
    @Expose
    public String payment;
    @SerializedName("diskon")
    @Expose
    public String diskon;
    @SerializedName("customer_id")
    @Expose
    public String customer_id;
    @SerializedName("customer")
    @Expose
    public KonsumenDAO konsumenTrans;
    @SerializedName("status")
    @Expose
    public String status;


    public TransactionByCabangDAO(String idTransaction, String transactionNumber, String totalServices,
                                  String totalSpareparts, String totalCost, String payment,
                                  String diskon, String customer_id) {
        this.idTransaction = idTransaction;
        this.transactionNumber = transactionNumber;
        this.totalServices = totalServices;
        this.totalSpareparts = totalSpareparts;
        this.totalCost = totalCost;
        this.payment = payment;
        this.diskon = diskon;
        this.customer_id = customer_id;
    }

    public String getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(String idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getTotalServices() {
        return totalServices;
    }

    public void setTotalServices(String totalServices) {
        this.totalServices = totalServices;
    }

    public String getTotalSpareparts() {
        return totalSpareparts;
    }

    public void setTotalSpareparts(String totalSpareparts) {
        this.totalSpareparts = totalSpareparts;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getDiskon() {
        return diskon;
    }

    public void setDiskon(String diskon) {
        this.diskon = diskon;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public KonsumenDAO getKonsumenTrans() {
        return konsumenTrans;
    }

    public void setKonsumenTrans(KonsumenDAO konsumenTrans) {
        this.konsumenTrans = konsumenTrans;
    }

    public String getStatus() {
        return status;
    }
}
