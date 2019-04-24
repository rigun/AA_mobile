package e.abimuliawans.p3l_mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

public class PemesananActivity extends AppCompatActivity {

    private List<OrderDataDAO> mListOrder = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecycleAdapterPemesanan recycleAdapterPemesanan;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerViewPemesanan;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;
    private String token,BASE_URL;

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
                Intent intent = new Intent(PemesananActivity.this,CabangActivity.class);
                startActivity(intent);
            }
        });

        // Menampilkan RecyclerView
        setRecycleViewPemesanan(httpClient,9,2);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setRecycleViewPemesanan(OkHttpClient.Builder httpClient, int idSupplier, int idCabang) {
        //Set Data Sparepart By Supplier
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<ValueDataOrder> orderCall = apiClient.getPemesanan(idSupplier,idCabang);

        orderCall.enqueue(new Callback<ValueDataOrder>() {
            @Override
            public void onResponse(Call<ValueDataOrder> call, retrofit2.Response<ValueDataOrder> response) {
                //progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(PemesananActivity.this,R.anim.layout_anim_recycle);
                recyclerView.setLayoutAnimation(animationController);

                List<OrderDataDAO> lisDataOrder = response.body().getData();

                recycleAdapterPemesanan.notifyDataSetChanged();
                recycleAdapterPemesanan=new RecycleAdapterPemesanan(PemesananActivity.this,lisDataOrder);
                recyclerView.setAdapter(recycleAdapterPemesanan);
            }

            @Override
            public void onFailure(Call<ValueDataOrder> call, Throwable t) {
                //
            }
        });
    }
}
