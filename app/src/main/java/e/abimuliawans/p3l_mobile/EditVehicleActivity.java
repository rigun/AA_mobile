package e.abimuliawans.p3l_mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditVehicleActivity extends AppCompatActivity {

    private EditText txtType;
    private EditText txtMerk;
    private String setType,setMerk,setID,token,BASE_URL;
    private OkHttpClient.Builder httpClientVeh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);

        //Set Toolbar
        Toolbar toolbarSupplier = findViewById(R.id.toolbarEditKendaraan);
        setSupportActionBar(toolbarSupplier);

        //Inisialisasi Edit Text
        txtType = findViewById(R.id.txtEditType);
        txtMerk = findViewById(R.id.txtEditMerk);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);
        BASE_URL = pref.getString("BASE_URL",null);

        //Pengambilan Data Kendaraaan
        SharedPreferences prefVeh = getApplication().getSharedPreferences("MyVehicle", Context.MODE_PRIVATE);
         setMerk= prefVeh.getString("merk", null);
         setType= prefVeh.getString("type",null);
         setID= prefVeh.getString("idVeh",null);

         //Set Data Di Edit Text
        txtMerk.setText(setMerk);
        txtType.setText(setType);

        //Pengecekan Bearer Token
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
                return chain.proceed(request);
            }
        });

        httpClientVeh=httpClient;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_edit,menu);

        MenuItem menuItem = menu.findItem(R.id.action_edit);


        return true;
    }

    public void actionClickEdit(MenuItem menuItem){
        updateDataVehicle(httpClientVeh);
    }

    public void updateDataVehicle(OkHttpClient.Builder httpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<VehicleDAO> vehicleCall = apiClient.updateVehicleReq(setID,
                txtMerk.getText().toString(),txtType.getText().toString());

        vehicleCall.enqueue(new Callback<VehicleDAO>() {
            @Override
            public void onResponse(Call<VehicleDAO> call, retrofit2.Response<VehicleDAO> response) {

                if(response.isSuccessful())
                {
                    Toasty.success(EditVehicleActivity.this, "Kendaraan Berhasil Diupdate",
                            Toast.LENGTH_SHORT, true).show();
                    SharedPreferences prefVeh = getApplication().getSharedPreferences("MyVehicle", Context.MODE_PRIVATE);
                    prefVeh.edit().clear().commit();

                    Intent intent = new Intent(EditVehicleActivity.this,DasboardActivity.class);
                    startActivity(intent);
                }
                else{

                    Toasty.error(EditVehicleActivity.this, "Gagal Update Data",
                            Toast.LENGTH_SHORT, true).show();
                }

            }

            @Override
            public void onFailure(Call<VehicleDAO> call, Throwable t) {
                Toasty.error(EditVehicleActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}
