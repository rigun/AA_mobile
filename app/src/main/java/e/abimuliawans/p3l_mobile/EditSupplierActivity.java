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

public class EditSupplierActivity extends AppCompatActivity {

    private EditText txtNamaSup,txtAddressSup,txtCitySup,txtPhoneSup;
    private String token,setNama,setAddress,setCity,setPhone,setId;
    private Integer inputId;
    private OkHttpClient.Builder httpClientSup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_supplier);

        //Set Toolbar
        Toolbar toolbarSupplier = findViewById(R.id.toolbarEditSupplier);
        setSupportActionBar(toolbarSupplier);

        //Inisialisasi Edit Text
       txtNamaSup=findViewById(R.id.txtEditNamaSup);
       txtAddressSup=findViewById(R.id.txtEditAlamatSup);
       txtCitySup=findViewById(R.id.txtEditCitySup);
       txtPhoneSup=findViewById(R.id.txtEditPhoneSup);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);

        //Pengambilan Data Supplier
        SharedPreferences prefSub = getApplication().getSharedPreferences("MySupplier", Context.MODE_PRIVATE);
        setNama= prefSub.getString("name",null);
        setCity= prefSub.getString("city",null);
        setAddress= prefSub.getString("address",null);
        setPhone= prefSub.getString("phone",null);
        setId= prefSub.getString("id",null);

        inputId= Integer.parseInt(setId);

        //Set Data Di Edit Text
        txtNamaSup.setText(setNama);
        txtAddressSup.setText(setAddress);
        txtPhoneSup.setText(setPhone);
        txtCitySup.setText(setCity);

        //Pengecekan Bearer Token
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
                return chain.proceed(request);
            }
        });

        httpClientSup=httpClient;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_edit,menu);

        MenuItem menuItem = menu.findItem(R.id.action_edit);


        return true;
    }

    public void actionClickEdit(MenuItem menuItem){
       //Fungsi Edit
        updateDataSupplier(httpClientSup);
    }

    public void updateDataSupplier(OkHttpClient.Builder httpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api1.thekingcorp.org/")
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<SupplierDAO> vehicleCall = apiClient.updateSupplierReg(inputId,txtNamaSup.getText().toString(),
                txtPhoneSup.getText().toString(),txtAddressSup.getText().toString(),txtCitySup.getText().toString(),
                "supplier");

        vehicleCall.enqueue(new Callback<SupplierDAO>() {
            @Override
            public void onResponse(Call<SupplierDAO> call, retrofit2.Response<SupplierDAO> response) {

                if(response.isSuccessful())
                {
                    Toasty.success(EditSupplierActivity.this, "Supplier Berhasil Diupdate",
                            Toast.LENGTH_SHORT, true).show();
                    SharedPreferences prefVeh = getApplication().getSharedPreferences("MySupplier", Context.MODE_PRIVATE);
                    prefVeh.edit().clear().commit();

                    Intent intent = new Intent(EditSupplierActivity.this,DasboardActivity.class);
                    startActivity(intent);
                }
                else{

                    Toasty.error(EditSupplierActivity.this, "Gagal Update Data",
                            Toast.LENGTH_SHORT, true).show();
                }

            }

            @Override
            public void onFailure(Call<SupplierDAO> call, Throwable t) {
                Toasty.error(EditSupplierActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}
