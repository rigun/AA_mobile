package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionDetailDAO {
    @SerializedName("id")
    @Expose
    public String idDetailTransaction;
    @SerializedName("transaction_id")
    @Expose
    public String idTransaction;
    @SerializedName("vehicle_customer")
    @Expose
    public VehicleCustomerDAO vehicleCustomer;

    public TransactionDetailDAO(String idDetailTransaction, String idTransaction, VehicleCustomerDAO vehicleCustomer) {
        this.idDetailTransaction = idDetailTransaction;
        this.idTransaction = idTransaction;
        this.vehicleCustomer = vehicleCustomer;
    }

    public String getIdDetailTransaction() {
        return idDetailTransaction;
    }

    public void setIdDetailTransaction(String idDetailTransaction) {
        this.idDetailTransaction = idDetailTransaction;
    }

    public String getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(String idTransaction) {
        this.idTransaction = idTransaction;
    }

    public VehicleCustomerDAO getVehicleCustomer() {
        return vehicleCustomer;
    }

    public void setVehicleCustomer(VehicleCustomerDAO vehicleCustomer) {
        this.vehicleCustomer = vehicleCustomer;
    }
}
