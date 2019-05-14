package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceDAO {
    @SerializedName("id")
    @Expose
    public String idService;

    @SerializedName("name")
    @Expose
    public String nameService;

    public ServiceDAO(String idService, String nameService) {
        this.idService = idService;
        this.nameService = nameService;
    }

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    public String getNameService() {
        return nameService;
    }

    public void setNameService(String nameService) {
        this.nameService = nameService;
    }
}
