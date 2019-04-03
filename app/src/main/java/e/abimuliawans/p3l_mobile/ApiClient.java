package e.abimuliawans.p3l_mobile;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiClient {

    @POST("login")
    @FormUrlEncoded
    Call<LoginResponse> loginRequest (@Field("email") String email,
                                      @Field("password") String password);

    @GET("vehicle")
    Call<List<VehicleDAO>> getVehicle();

}
