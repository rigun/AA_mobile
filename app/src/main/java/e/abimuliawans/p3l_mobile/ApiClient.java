package e.abimuliawans.p3l_mobile;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiClient {

    @POST("login")
    @FormUrlEncoded
    Call<EmployeeData> loginRequest (@Field("email") String email,
                                      @Field("password") String password);
}
