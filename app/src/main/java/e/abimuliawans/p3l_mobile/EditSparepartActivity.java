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

public class EditSparepartActivity extends AppCompatActivity {

    private Spinner spinnerSpare,spinnerSpare2;
    private EditText namaSpare,merkSpare,typeSpare;
    private OkHttpClient.Builder httpClientSup;
    private String token,code,BASE_URL;
    private List<String> listSpinner = new ArrayList<String>();
    private List<String> listSpinner2 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sparepart);

        //Set Toolbar
        Toolbar toolbarEditSpare = findViewById(R.id.toolbarEditSparepart);
        setSupportActionBar(toolbarEditSpare);

        //Inisialisasi
        spinnerSpare=findViewById(R.id.spinnerEditSpare);
        spinnerSpare2=findViewById(R.id.spinnerEditSpare2);
        namaSpare=findViewById(R.id.txtEditNamaSpar);
        merkSpare=findViewById(R.id.txtEditMerkSpar);
        typeSpare=findViewById(R.id.txtEditTypeSpar);


        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);
        BASE_URL = pref.getString("BASE_URL",null);

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
        loadVehicleSpinner(httpClientSup);
        loadSupplierSpinner(httpClientSup);

        //Get Intent dan Set Edit Text
        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        String nama = intent.getStringExtra("name");
        String merk = intent.getStringExtra("merk");
        String type = intent.getStringExtra("type");

        namaSpare.setText(nama);
        merkSpare.setText(merk);
        typeSpare.setText(type);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_edit,menu);

        MenuItem menuItem = menu.findItem(R.id.action_edit);

        return true;
    }

    public void actionClickEdit(MenuItem menuItem){
       updateSparepart(httpClientSup);
    }

    public void loadVehicleSpinner(OkHttpClient.Builder httpClient)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<VehicleDAO>> listCall = apiClient.getVehicle();

        listCall.enqueue(new Callback<List<VehicleDAO>>() {
            @Override
            public void onResponse(Call<List<VehicleDAO>> call, retrofit2.Response<List<VehicleDAO>> response) {
                List<VehicleDAO> vehicleDAOList = response.body();
                for(int i=0; i < vehicleDAOList.size(); i++ ){
                    String id = vehicleDAOList.get(i).getIdVehicle();
                    listSpinner.add(id);
                }
                listSpinner.add(0,"-SELECT ID VEHICLE-");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditSparepartActivity.this,
                        android.R.layout.simple_spinner_item,listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSpare.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<VehicleDAO>> call, Throwable t) {

            }
        });
    }

    public void loadSupplierSpinner(OkHttpClient.Builder httpClient)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<ValueSupplier> apiClientSupplier = apiClient.getSupplier();

        apiClientSupplier.enqueue(new Callback<ValueSupplier>() {
            @Override
            public void onResponse(Call<ValueSupplier> call, retrofit2.Response<ValueSupplier> response) {
                List<SupplierDAO> supplierDAOList = response.body().getResult();
                for(int i=0; i < supplierDAOList.size(); i++)
                {
                    String idSup = supplierDAOList.get(i).getIdSupplier();
                    listSpinner2.add(idSup);
                }
                listSpinner2.add(0,"-SELECT ID SUPPLIER-");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditSparepartActivity.this,
                        android.R.layout.simple_spinner_item,listSpinner2);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSpare2.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ValueSupplier> call, Throwable t) {

            }
        });
    }

    public void updateSparepart(OkHttpClient.Builder httpClient)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<SparepartDAO> sparepartDAOCall = apiClient.updateSparepart(code,namaSpare.getText().toString(),
                merkSpare.getText().toString(),typeSpare.getText().toString(),
                spinnerSpare2.getSelectedItem().toString(),spinnerSpare.getSelectedItem().toString());

        sparepartDAOCall.enqueue(new Callback<SparepartDAO>() {
            @Override
            public void onResponse(Call<SparepartDAO> call, retrofit2.Response<SparepartDAO> response) {
                if(response.isSuccessful())
                {
                    Toasty.success(EditSparepartActivity.this, "Sparepart Berhasil Diupdate",
                            Toast.LENGTH_SHORT, true).show();

                    Intent intent = new Intent(EditSparepartActivity.this,DasboardActivity.class);
                    startActivity(intent);
                }
                else{

                    Toasty.error(EditSparepartActivity.this, "Gagal Update Data",
                            Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onFailure(Call<SparepartDAO> call, Throwable t) {
                Toasty.error(EditSparepartActivity.this, "Gagal Update Data",
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}
