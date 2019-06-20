package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ValueResponseTransKonsumen {

    @SerializedName("msg")
    @Expose
    public TransactionByCabangDAO result;

    public TransactionByCabangDAO getResult() {
        return result;
    }
}
