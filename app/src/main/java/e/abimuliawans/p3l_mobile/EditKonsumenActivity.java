package e.abimuliawans.p3l_mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditKonsumenActivity extends AppCompatActivity {

    private EditText txtNamaKon,txtAddressKon,txtPhoneKon;
    private List<String> listSpinner = new ArrayList<>();
    private Spinner spinnerEditCityKon;
    private String token,setNama,setAddress,setPhone,setId,BASE_URL;
    private Integer inputId;
    private OkHttpClient.Builder httpClientKon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_konsumen);

        //Set Toolbar
        Toolbar toolbarKonsumen = findViewById(R.id.toolbarEditKonsumen);
        setSupportActionBar(toolbarKonsumen);

        //Inisialisasi Edit Text dan Spinner
        txtNamaKon =findViewById(R.id.txtEditNamaKon);
        txtAddressKon=findViewById(R.id.txtEditAlamatKon);
        txtPhoneKon=findViewById(R.id.txtEditPhoneKon);
        spinnerEditCityKon=findViewById(R.id.spinnerCityEditKon);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);
        BASE_URL = pref.getString("BASE_URL",null);

        //Pengambilan Data Sales
        SharedPreferences prefSub = getApplication().getSharedPreferences("MyKonsumen", Context.MODE_PRIVATE);
        setNama= prefSub.getString("name",null);
        setAddress= prefSub.getString("address",null);
        setPhone= prefSub.getString("phone",null);
        setId= prefSub.getString("id",null);

        inputId= Integer.parseInt(setId);

        //Set Data Di Edit Text
        txtNamaKon.setText(setNama);
        txtAddressKon.setText(setAddress);
        txtPhoneKon.setText(setPhone);

        //Pengecekan Bearer Token
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
                return chain.proceed(request);
            }
        });

        httpClientKon=httpClient;

        loadSpinnerCities(httpClientKon);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_edit,menu);

        MenuItem menuItem = menu.findItem(R.id.action_edit);

        return true;
    }

    public void actionClickEdit(MenuItem menuItem){
        //Fungsi Edit
        updateDataKonsumen(httpClientKon);
    }

    private void updateDataKonsumen(OkHttpClient.Builder httpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<KonsumenDAO> updateKonsumenReg = apiClient.updateKonsumenReg(inputId,txtNamaKon.getText().toString(),
                txtPhoneKon.getText().toString(),txtAddressKon.getText().toString(),spinnerEditCityKon.getSelectedItem().toString(),
                "konsumen");

        updateKonsumenReg.enqueue(new Callback<KonsumenDAO>() {
            @Override
            public void onResponse(Call<KonsumenDAO> call, retrofit2.Response<KonsumenDAO> response) {

                if(response.isSuccessful())
                {
                    Toasty.success(EditKonsumenActivity.this, "Sales Berhasil Diupdate",
                            Toast.LENGTH_SHORT, true).show();
                    SharedPreferences prefVeh = getApplication().getSharedPreferences("MySales", Context.MODE_PRIVATE);
                    prefVeh.edit().clear().commit();

                    Intent intent = new Intent(EditKonsumenActivity.this,DasboardActivity.class);
                    startActivity(intent);
                }
                else{

                    Toasty.error(EditKonsumenActivity.this, "Gagal Update Data",
                            Toast.LENGTH_SHORT, true).show();
                }

            }

            @Override
            public void onFailure(Call<KonsumenDAO> call, Throwable t) {
                Toasty.error(EditKonsumenActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    public void loadSpinnerCities(OkHttpClient.Builder httpClient)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<CitiesDAO>> listCall = apiClient.getCities();

        listCall.enqueue(new Callback<List<CitiesDAO>>() {
            @Override
            public void onResponse(Call<List<CitiesDAO>> call, retrofit2.Response<List<CitiesDAO>> response) {
                List<CitiesDAO> citiesDAOS = response.body();
                for(int i=0; i < citiesDAOS.size(); i++ ){
                    String name = citiesDAOS.get(i).getNameCities();
                    listSpinner.add(name);
                }
                listSpinner.add(0,"-SELECT NAME CITY-");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditKonsumenActivity.this,
                        android.R.layout.simple_spinner_item,listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEditCityKon.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<CitiesDAO>> call, Throwable t) {

            }
        });
    }
}
