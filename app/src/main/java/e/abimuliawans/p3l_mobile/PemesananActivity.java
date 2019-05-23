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
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

                mBuilder.setView(mView)
                        .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Tambah dan Cek Konsumen Baru

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
}
