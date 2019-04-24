package e.abimuliawans.p3l_mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TambahPemasananActivity extends AppCompatActivity {
    private TextView idSupplierTxt,idCabangTxt;
    private String setIdSup,token,id,setIdCabang,setCodeSparepart,BASE_URL;
    private EditText unitTxt,totalTxt;
    private ProgressDialog dialog;
    private OkHttpClient.Builder httpClientAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pemasanan);

        //Set Toolbar
        Toolbar toolbarOrder = findViewById(R.id.toolbarAddPemesanan);
        setSupportActionBar(toolbarOrder);

        //Set XML
        idSupplierTxt = findViewById(R.id.txtIDSupAddOrder);
        idCabangTxt = findViewById(R.id.txtIDCabangAddOrder);
        unitTxt = findViewById(R.id.txtAddUnitOrder);
        totalTxt = findViewById(R.id.txtAddTotalOrder);

        //Pengambilan Data Order
        SharedPreferences prefSub = getApplication().getSharedPreferences("MyOrder", Context.MODE_PRIVATE);
        setIdSup= prefSub.getString("idSupplier",null);
        setIdCabang = prefSub.getString("idCabang",null);
        setCodeSparepart = prefSub.getString("code",null);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);
        BASE_URL = pref.getString("BASE_URL",null);

        //Pengecekan Bearer Token
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
                return chain.proceed(request);
            }
        });

        httpClientAdd=httpClient;

        //Set Data ID Supplier dan ID Cabang
        idSupplierTxt.setText(setIdSup);
        idCabangTxt.setText(setIdCabang);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_edit,menu);

        MenuItem menuItem = menu.findItem(R.id.action_edit);
        return true;
    }

    public void actionClickEdit(MenuItem menuItem){
        //Fungsi Edit
        addOrder(httpClientAdd);
    }

    public void addOrder(OkHttpClient.Builder httpClientAdd) {

        Integer inputIdSupplier = Integer.parseInt(setIdSup);
        Integer inputICabang = Integer.parseInt(setIdCabang);
        String inputCode = setCodeSparepart;
        String inputUnit = unitTxt.getText().toString().trim();
        String total = totalTxt.getText().toString().trim();
        Integer inputTotal = Integer.parseInt(total);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClientAdd.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<OrderDAO> orderDAOCall = apiClient.addOrder(inputIdSupplier,inputICabang,inputCode,inputUnit,inputTotal);
        orderDAOCall.enqueue(new Callback<OrderDAO>() {
            @Override
            public void onResponse(Call<OrderDAO> call, Response<OrderDAO> response) {
                Toasty.success(TambahPemasananActivity.this, "Pemesanan Berhasil Ditambah",
                        Toast.LENGTH_SHORT, true).show();

                Intent intent = new Intent(TambahPemasananActivity.this,DasboardActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<OrderDAO> call, Throwable t) {
                Toasty.error(TambahPemasananActivity.this, "Gagal Menambah Data",
                        Toast.LENGTH_SHORT, true).show();
            }
        });

    }
}
