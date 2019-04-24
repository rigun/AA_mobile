package e.abimuliawans.p3l_mobile;

import android.content.Intent;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiClient {

    //Login =========================================================================================
    @POST("login")
    @FormUrlEncoded
    Call<LoginResponse> loginRequest(@Field("email") String email,
                                     @Field("password") String password);
    //Kendaraan =====================================================================================
    @GET("vehicle")
    Call<List<VehicleDAO>> getVehicle();

    @POST("vehicle")
    @FormUrlEncoded
    Call<VehicleDAO> addVehicleReq(@Field("merk") String merk,
                                   @Field("type") String type);
    @POST("vehicle/{id}")
    @FormUrlEncoded
    Call<VehicleDAO> updateVehicleReq(@Path("id") String idVeh,
                                      @Field("merk") String merk,
                                      @Field("type") String type);
    @DELETE("vehicle/{id}")
    Call<VehicleDAO> deleteVehicle(@Path("id") Integer id);

    //Supplier ======================================================================================
    @GET("personbyrole/supplier")
    Call<ValueSupplier> getSupplier();

    @POST("personbyrole/supplier")
    @FormUrlEncoded
    Call<SupplierDAO> addSupplierReq(@Field("name") String name,
                                     @Field("phoneNumber") String phone,
                                     @Field("address") String address,
                                     @Field("city") String city);

    @DELETE("deleteperson/{id}")
    Call<SupplierDAO> deleteSupplierReq(@Path("id") Integer id);

    @POST("updateperson/{id}")
    @FormUrlEncoded
    Call<SupplierDAO> updateSupplierReg(@Path("id") Integer id,
                                        @Field("name") String name,
                                        @Field("phoneNumber") String phone,
                                        @Field("address") String address,
                                        @Field("city") String city,
                                        @Field("role") String role);
    //Sparepart =====================================================================================
    @GET("sparepart")
    Call<List<SparepartDAO>> getSparepart();

    @GET("sparepartBySupplier/{supplierId}")
    Call<List<SparepartDAO>> getSparepartBySupplier(@Path("supplierId") Integer idSupplier);


    @POST("sparepart")
    @FormUrlEncoded
    Call<SparepartDAO> addSparepart(@Field("code") String code,
                                    @Field("name") String name,
                                    @Field("merk") String merk,
                                    @Field("type") String type,
                                    @Field("supplier_id") String supID,
                                    @Field("vehicles") String vehID,
                                    @Field("picture") String picture);

    @DELETE("sparepart/{code}")
    Call<SparepartDAO> deleteSparepart(@Path("code") String code);

    @POST("sparepart/{code}")
    @FormUrlEncoded
    Call<SparepartDAO> updateSparepart(@Path("code") String code,
                                       @Field("name") String name,
                                       @Field("merk") String merk,
                                       @Field("type") String type,
                                       @Field("supplier_id") String supplierID,
                                       @Field("vehicles") String vehiclesID);

    //Cities ========================================================================================
    @GET("cities")
    Call<List<CitiesDAO>> getCities();


    //Sales =========================================================================================
    @GET("personbyrole/sales")
    Call<ValueSales> getSales();

    @POST("personbyrole/sales")
    @FormUrlEncoded
    Call<SalesDAO> addSalesReq(@Field("name") String name,
                               @Field("phoneNumber") String phone,
                               @Field("address") String address,
                               @Field("city") String city);

    @POST("updateperson/{id}")
    @FormUrlEncoded
    Call<SalesDAO> updateSalesReg(@Path("id") Integer id,
                                  @Field("name") String name,
                                  @Field("phoneNumber") String phone,
                                  @Field("address") String address,
                                  @Field("city") String city,
                                  @Field("role") String role);

    @DELETE("deleteperson/{id}")
    Call<SalesDAO> deleteSalesReq(@Path("id") Integer id);


    //Konsumen ======================================================================================
    @GET("personbyrole/konsumen")
    Call<ValueKonsumen> getKonsumen();

    @POST("personbyrole/konsumen")
    @FormUrlEncoded
    Call<KonsumenDAO> addKonsumenReq(@Field("name") String name,
                               @Field("phoneNumber") String phone,
                               @Field("address") String address,
                               @Field("city") String city);

    @POST("updateperson/{id}")
    @FormUrlEncoded
    Call<KonsumenDAO> updateKonsumenReg(@Path("id") Integer id,
                                  @Field("name") String name,
                                  @Field("phoneNumber") String phone,
                                  @Field("address") String address,
                                  @Field("city") String city,
                                  @Field("role") String role);

    @DELETE("deleteperson/{id}")
    Call<KonsumenDAO> deleteKonsumenReq(@Path("id") Integer id);


    //Cabang ========================================================================================
    @GET("branch")
    Call<List<CabangDAO>> getCabang();

    //Order ========================================================================================
    @POST("order")
    @FormUrlEncoded
    Call<OrderDAO> addOrder(@Field("supplier_id") Integer idSupplier,
                            @Field("branch_id") Integer idCabang,
                            @Field("sparepart_code") String code,
                            @Field("unit") String unit,
                            @Field("total") Integer total);

    @GET("sparepartBS/{idSupplier}/{idCabang}")
    Call<ValueDataOrder> getPemesanan(@Path("idSupplier") Integer idSupplier,
                                      @Path("idCabang") Integer idCabang);
}
