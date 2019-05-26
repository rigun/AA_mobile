package e.abimuliawans.p3l_mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class PemesananActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private List<ShowPemesananDAO> mListOrder = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecycleAdapterPemesanan recycleAdapterPemesanan;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerViewPemesanan;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;
    private String token,BASE_URL,inputIDcabang;
    private Integer integerIDCabang;
    private EditText unitOrder,totalOrder;
    private Spinner spinnerSupplier,spinnerSparepart;
    private List<String> listSpinnerCode = new ArrayList<>();
    private List<String> listSpinnerSupplier = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);

        //Inisialisasi Progres Bar
        progressBar = findViewById(R.id.progress_bar_pemesanan);

        //Set Toolbar
        Toolbar toolbarPemesanan = findViewById(R.id.toolbarPemesanan);
        setSupportActionBar(toolbarPemesanan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Inisialisasi Recycle
        recyclerView =findViewById(R.id.recyclerViewPemesanan);
        recycleAdapterPemesanan= new RecycleAdapterPemesanan(PemesananActivity.this,mListOrder);
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycleAdapterPemesanan);

        //Ambil ID Cabang
        Bundle bundle = getIntent().getExtras();
        inputIDcabang = bundle.getString("idCabang");
        integerIDCabang = Integer.parseInt(inputIDcabang);

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

        //Floating Button
        floatingActionButton=findViewById(R.id.btnAddPemesanan);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PemesananActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_order, null);

                spinnerSparepart = mView.findViewById(R.id.spinnerSparepartOrder);
                spinnerSupplier = mView.findViewById(R.id.spinnerChooseSupplier);
                totalOrder = mView.findViewById(R.id.txtTotalOrder);
                unitOrder = mView.findViewById(R.id.txtUnitOrder);

                //Load Code Sparepart and Supplier
                loadCodeSparepartSpinner(httpClient,inputIDcabang);
                loadIDSupplier(httpClient);

                mBuilder.setView(mView)
                        .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Tambah dan Cek Konsumen Baru
                                addOrder(httpClient);

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

        // Menampilkan RecyclerView
        setRecycleViewPemesanan(httpClient);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setRecycleViewPemesanan(OkHttpClient.Builder httpClient) {
        //Set Data Sparepart By Supplier
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<ShowPemesananDAO>> orderCall = apiClient.getOrder(integerIDCabang);

        orderCall.enqueue(new Callback<List<ShowPemesananDAO>>() {
            @Override
            public void onResponse(Call<List<ShowPemesananDAO>> call, retrofit2.Response<List<ShowPemesananDAO>> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(PemesananActivity.this,R.anim.layout_anim_recycle);
                recyclerView.setLayoutAnimation(animationController);

                List<ShowPemesananDAO> lisDataOrder = response.body();
                recycleAdapterPemesanan.notifyDataSetChanged();
                recycleAdapterPemesanan=new RecycleAdapterPemesanan(PemesananActivity.this,lisDataOrder);
                recyclerView.setAdapter(recycleAdapterPemesanan);
            }

            @Override
            public void onFailure(Call<List<ShowPemesananDAO>> call, Throwable t) {
                //
                Toasty.error(PemesananActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
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
        recycleAdapterPemesanan.getFilter().filter(s);
        return false;
    }

    public void addOrder(OkHttpClient.Builder httpClientAdd) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClientAdd.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<OrderDAO> orderDAOCall = apiClient.addOrder(spinnerSupplier.getSelectedItem().toString(),
                integerIDCabang,spinnerSparepart.getSelectedItem().toString(),unitOrder.getText().toString(),
                totalOrder.getText().toString());
        orderDAOCall.enqueue(new Callback<OrderDAO>() {
            @Override
            public void onResponse(Call<OrderDAO> call, retrofit2.Response<OrderDAO> response) {
                Toasty.success(PemesananActivity.this, "Pemesanan Berhasil Ditambah",
                        Toast.LENGTH_SHORT, true).show();

                Intent intent = new Intent(PemesananActivity.this,DasboardActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<OrderDAO> call, Throwable t) {
                Toasty.error(PemesananActivity.this, "Gagal Menambah Data",
                        Toast.LENGTH_SHORT, true).show();
            }
        });

    }

    private void loadCodeSparepartSpinner(OkHttpClient.Builder httpClient, String id_cabang) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<SparepartCabangSupDAO>> sparepartCabang = apiClient.getSparepartCabang(id_cabang);

        sparepartCabang.enqueue(new Callback<List<SparepartCabangSupDAO>>() {
            @Override
            public void onResponse(Call<List<SparepartCabangSupDAO>> call, retrofit2.Response<List<SparepartCabangSupDAO>> response) {
                List<SparepartCabangSupDAO> sparepartDAOS = response.body();
                for(int i=0; i < sparepartDAOS.size(); i++)
                {
                    String code = sparepartDAOS.get(i).getCodeSpareScp();
                    listSpinnerCode.add(code);
                }
                listSpinnerCode.add(0,"-SELECT CODE-");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(PemesananActivity.this,
                        android.R.layout.simple_spinner_item,listSpinnerCode);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSparepart.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<SparepartCabangSupDAO>> call, Throwable t) {
                //
                Toasty.error(PemesananActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    public void loadIDSupplier(OkHttpClient.Builder httpClient){

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
                    String id = supplierDAOList.get(i).getIdSupplier();
                    String name = supplierDAOList.get(i).getNameSupplier();
                    String input = id+"-"+name;
                    listSpinnerSupplier.add(input);
                }
                listSpinnerSupplier.add(0,"-SELECT SUPPLIER-");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(PemesananActivity.this,
                        android.R.layout.simple_spinner_item,listSpinnerSupplier);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSupplier.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ValueSupplier> call, Throwable t) {
                //
            }
        });
    }
}
