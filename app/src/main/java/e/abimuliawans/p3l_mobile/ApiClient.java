package e.abimuliawans.p3l_mobile;

import android.content.Intent;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiClient {


    @POST("login")
    @FormUrlEncoded
    Call<LoginResponse> loginRequest(@Field("email") String email,
                                     @Field("password") String password);

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

    @GET("sparepart")
    Call<List<SparepartDAO>> getSparepart();

}
