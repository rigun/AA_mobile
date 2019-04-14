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

public class EditSalesActivity extends AppCompatActivity {

    private EditText txtNamaSal,txtAddressSal,txtPhoneSal;
    private List<String> listSpinner = new ArrayList<>();
    private Spinner spinnerEditCitySal;
    private String token,setNama,setAddress,setPhone,setId,BASE_URL;
    private Integer inputId;
    private OkHttpClient.Builder httpClientSup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sales);

        //Set Toolbar
        Toolbar toolbarSupplier = findViewById(R.id.toolbarEditSales);
        setSupportActionBar(toolbarSupplier);

        //Inisialisasi Edit Text dan Spinner
        txtNamaSal =findViewById(R.id.txtEditNamaSal);
        txtAddressSal=findViewById(R.id.txtEditAlamatSal);
        txtPhoneSal=findViewById(R.id.txtEditPhoneSal);
        spinnerEditCitySal=findViewById(R.id.spinnerCityEditSal);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);
        BASE_URL = pref.getString("BASE_URL",null);

        //Pengambilan Data Sales
        SharedPreferences prefSub = getApplication().getSharedPreferences("MySales", Context.MODE_PRIVATE);
        setNama= prefSub.getString("name",null);
        setAddress= prefSub.getString("address",null);
        setPhone= prefSub.getString("phone",null);
        setId= prefSub.getString("id",null);

        inputId= Integer.parseInt(setId);

        //Set Data Di Edit Text
        txtNamaSal.setText(setNama);
        txtAddressSal.setText(setAddress);
        txtPhoneSal.setText(setPhone);

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

        loadSpinnerCities(httpClientSup);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_edit,menu);

        MenuItem menuItem = menu.findItem(R.id.action_edit);

        return true;
    }

    public void actionClickEdit(MenuItem menuItem){
        //Fungsi Edit
        updateDataSales(httpClientSup);
    }

    private void updateDataSales(OkHttpClient.Builder httpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<SalesDAO> updateSalesReg = apiClient.updateSalesReg(inputId,txtNamaSal.getText().toString(),
                txtPhoneSal.getText().toString(),txtAddressSal.getText().toString(),spinnerEditCitySal.getSelectedItem().toString(),
                "sales");

        updateSalesReg.enqueue(new Callback<SalesDAO>() {
            @Override
            public void onResponse(Call<SalesDAO> call, retrofit2.Response<SalesDAO> response) {

                if(response.isSuccessful())
                {
                    Toasty.success(EditSalesActivity.this, "Sales Berhasil Diupdate",
                            Toast.LENGTH_SHORT, true).show();
                    SharedPreferences prefVeh = getApplication().getSharedPreferences("MySales", Context.MODE_PRIVATE);
                    prefVeh.edit().clear().commit();

                    Intent intent = new Intent(EditSalesActivity.this,DasboardActivity.class);
                    startActivity(intent);
                }
                else{

                    Toasty.error(EditSalesActivity.this, "Gagal Update Data",
                            Toast.LENGTH_SHORT, true).show();
                }

            }

            @Override
            public void onFailure(Call<SalesDAO> call, Throwable t) {
                Toasty.error(EditSalesActivity.this, t.getMessage(),
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditSalesActivity.this,
                        android.R.layout.simple_spinner_item,listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEditCitySal.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<CitiesDAO>> call, Throwable t) {

            }
        });
    }
}
