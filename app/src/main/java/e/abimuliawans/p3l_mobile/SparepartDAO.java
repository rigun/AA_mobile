package e.abimuliawans.p3l_mobile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SparepartDAO {

    @SerializedName("code")
    @Expose
    public String codeSparepart ;

    @SerializedName("merk")
    @Expose
    public String merkSparepart ;

    @SerializedName("name")
    @Expose
    public String namaSparepart;

    @SerializedName("type")
    @Expose
    public String typeSparepart;

    @SerializedName("picture")
    @Expose
    public String picSparepart;

    @SerializedName("supplier_id")
    @Expose
    public String idSupplierSpart;

    public SparepartDAO(String codeSparepart, String merkSparepart, String namaSparepart, String typeSparepart, String picSparepart, String idSupplierSpart) {
        this.codeSparepart = codeSparepart;
        this.merkSparepart = merkSparepart;
        this.namaSparepart = namaSparepart;
        this.typeSparepart = typeSparepart;
        this.picSparepart = picSparepart;
        this.idSupplierSpart = idSupplierSpart;
    }

    public String getCodeSparepart() {
        return codeSparepart;
    }

    public void setCodeSparepart(String codeSparepart) {
        this.codeSparepart = codeSparepart;
    }

    public String getMerkSparepart() {
        return merkSparepart;
    }

    public void setMerkSparepart(String merkSparepart) {
        this.merkSparepart = merkSparepart;
    }

    public String getNamaSparepart() {
        return namaSparepart;
    }

    public void setNamaSparepart(String namaSparepart) {
        this.namaSparepart = namaSparepart;
    }

    public String getTypeSparepart() {
        return typeSparepart;
    }

    public void setTypeSparepart(String typeSparepart) {
        this.typeSparepart = typeSparepart;
    }

    public String getPicSparepart() {
        return picSparepart;
    }

    public void setPicSparepart(String picSparepart) {
        this.picSparepart = picSparepart;
    }

    public String getIdSupplierSpart() {
        return idSupplierSpart;
    }

    public void setIdSupplierSpart(String idSupplierSpart) {
        this.idSupplierSpart = idSupplierSpart;
    }
}
