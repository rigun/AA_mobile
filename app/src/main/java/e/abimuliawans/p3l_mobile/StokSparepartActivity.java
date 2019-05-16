package e.abimuliawans.p3l_mobile;

import android.content.Context;
import android.content.SharedPreferences;
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

public class StokSparepartActivity extends AppCompatActivity {

    private RecycleAdapterStokSparepart recycleAdapterStokSparepart;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerViewKonsumen;
    private String token,BASE_URL;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private List<StokSparepartDAO> mStokSparepart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stok_sparepart);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);
        BASE_URL = pref.getString("BASE_URL",null);

        //Inisialisasi Progres Bar
        progressBar = findViewById(R.id.progress_bar_stok_kurang);

        //Set Toolbar
        Toolbar toolbarSales = findViewById(R.id.toolbarStokKurang);
        setSupportActionBar(toolbarSales);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //Inisialisasi Recycle
        recyclerView =findViewById(R.id.recyclerViewStokKurang);
        recycleAdapterStokSparepart= new RecycleAdapterStokSparepart(StokSparepartActivity.this,mStokSparepart);
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycleAdapterStokSparepart);

        //Pengecekan Bearer Token
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
                return chain.proceed(request);
            }
        });

        //Set View Stok Kurang
        setRecycleViewStokKurang(httpClient);

    }

    public void setRecycleViewStokKurang(OkHttpClient.Builder httpClient)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<StokSparepartDAO>> listCall = apiClient.getStokKurang();

        listCall.enqueue(new Callback<List<StokSparepartDAO>>() {
            @Override
            public void onResponse(Call<List<StokSparepartDAO>> call, retrofit2.Response<List<StokSparepartDAO>> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(StokSparepartActivity.this,R.anim.layout_anim_recycle);
                recyclerView.setLayoutAnimation(animationController);
                recycleAdapterStokSparepart.notifyDataSetChanged();
                List<StokSparepartDAO> stokSparepartDAOS = response.body();

                recycleAdapterStokSparepart=new RecycleAdapterStokSparepart(StokSparepartActivity.this,stokSparepartDAOS);
                recyclerView.setAdapter(recycleAdapterStokSparepart);

            }

            @Override
            public void onFailure(Call<List<StokSparepartDAO>> call, Throwable t) {

                Toasty.error(StokSparepartActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}
