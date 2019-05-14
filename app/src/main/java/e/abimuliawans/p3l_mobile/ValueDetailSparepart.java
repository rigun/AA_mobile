package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ValueDetailSparepart {

     List<DetailSparepartDAO> data;

    public List<DetailSparepartDAO> getResult(){
        return data;
    }
}
