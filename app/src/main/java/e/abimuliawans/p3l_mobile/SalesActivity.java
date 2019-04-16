package e.abimuliawans.p3l_mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class SalesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private List<SalesDAO> mListSales = new ArrayList<>();
    private List<String> listSpinner = new ArrayList<>();
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private RecycleAdapterSales recycleAdapterSales;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerViewSales;
    private EditText nameSales,phoneSales,alamatSales;
    private Spinner spinnerCity;
    private String token,BASE_URL;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);
        BASE_URL = pref.getString("BASE_URL",null);

        //Inisialisasi Progres Bar
        progressBar = findViewById(R.id.progress_bar_sales);

        //Set Toolbar
        Toolbar toolbarSales = findViewById(R.id.toolbarSales);
        setSupportActionBar(toolbarSales);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Inisialisasi Recycle
        recyclerView =findViewById(R.id.recyclerViewSales);
        recycleAdapterSales= new RecycleAdapterSales(SalesActivity.this,mListSales);
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycleAdapterSales);

        //Pengecekan Bearer Token
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
                return chain.proceed(request);
            }
        });

        // Menampilkan RecyclerView
        setRecycleViewSales(httpClient);

        //Floating Button
        floatingActionButton=findViewById(R.id.btnAddSales);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //New Alert
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SalesActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_sales,null);

                nameSales = mView.findViewById(R.id.txtSalName);
                phoneSales = mView.findViewById(R.id.txtSalPhone);
                alamatSales = mView.findViewById(R.id.txtSalAddress);
                spinnerCity = mView.findViewById(R.id.spinnerCitySal);
                //Set Spinner City
                loadSpinnerCities(httpClient);

                mBuilder.setView(mView)
                        .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Tambah
                                addNewSales(httpClient);
                            }
                        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Batal
                            }
                });

                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setRecycleViewSales(OkHttpClient.Builder httpClient){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<ValueSales> apiClientSupplier = apiClient.getSales();

        apiClientSupplier.enqueue(new Callback<ValueSales>() {
            @Override
            public void onResponse(Call<ValueSales> call, retrofit2.Response<ValueSales> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(SalesActivity.this,R.anim.layout_anim_recycle);
                recyclerView.setLayoutAnimation(animationController);
                recycleAdapterSales.notifyDataSetChanged();
                recycleAdapterSales=new RecycleAdapterSales(SalesActivity.this,response.body().getResult());
                recyclerView.setAdapter(recycleAdapterSales);

            }

            @Override
            public void onFailure(Call<ValueSales> call, Throwable t) {

                Toasty.error(SalesActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    public void addNewSales(final OkHttpClient.Builder httpClient){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<SalesDAO> salesDAOCall = apiClient.addSalesReq(nameSales.getText().toString(),phoneSales.getText().toString(),
                alamatSales.getText().toString(),spinnerCity.getSelectedItem().toString());

        salesDAOCall.enqueue(new Callback<SalesDAO>() {
            @Override
            public void onResponse(Call<SalesDAO> call, retrofit2.Response<SalesDAO> response) {

                if(response.isSuccessful())
                {
                    Toasty.success(SalesActivity.this, "Sales Berhasil Ditambah",
                            Toast.LENGTH_SHORT, true).show();

                    setRecycleViewSales(httpClient);
                }
                else{

                    Toasty.error(SalesActivity.this, "Gagal Menambahkan Data",
                            Toast.LENGTH_SHORT, true).show();
                }

            }

            @Override
            public void onFailure(Call<SalesDAO> call, Throwable t) {
                Toasty.error(SalesActivity.this, t.getMessage(),
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SalesActivity.this,
                        android.R.layout.simple_spinner_item,listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCity.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<CitiesDAO>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView =(SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        recycleAdapterSales.getFilter().filter(s);
        return false;
    }

}
