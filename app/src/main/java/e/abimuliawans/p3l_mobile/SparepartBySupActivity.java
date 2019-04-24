package e.abimuliawans.p3l_mobile;

import android.content.Context;
import android.content.Intent;
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

public class SparepartBySupActivity extends AppCompatActivity {

    private List<SparepartDAO> mListSparepart = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecycleAdapterSparepartBySup recycleAdapterSparepartBySup;
    private RecyclerView.LayoutManager layoutManager;
    private String token,BASE_URL,idSupplier,idCabang;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sparepart_by_sup);

        //Inisialisasi Progres Bar
        progressBar = findViewById(R.id.progress_bar_sparepart_by_sup);

        //Set Toolbar
        Toolbar toolbarSparepart = findViewById(R.id.toolbarSparepartBySup);
        setSupportActionBar(toolbarSparepart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Inisialisasi Recycle
        recyclerView =findViewById(R.id.recyclerViewSparepartBySup);
        recycleAdapterSparepartBySup= new RecycleAdapterSparepartBySup(SparepartBySupActivity.this,mListSparepart);
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycleAdapterSparepartBySup);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);
        BASE_URL = pref.getString("BASE_URL",null);

        //Pengambilan Asset Order
        SharedPreferences pref2 = getApplication().getSharedPreferences("MyOrder", Context.MODE_PRIVATE);
        idSupplier = pref2.getString("idSupplier", null);
        idCabang = pref2.getString("idCabang", null);
        Integer idSupplierInteger = Integer.parseInt(idSupplier);
        Integer idCabangInteger = Integer.parseInt(idCabang);

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
        setRecycleViewSparepartBySup(httpClient,idSupplierInteger);
    }

    private void setRecycleViewSparepartBySup(OkHttpClient.Builder httpClient, int idSupplier) {
        //Set Data Sparepart By Supplier
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<SparepartDAO>> sparepartCall = apiClient.getSparepartBySupplier(idSupplier);

        sparepartCall.enqueue(new Callback<List<SparepartDAO>>() {
            @Override
            public void onResponse(Call<List<SparepartDAO>> call, retrofit2.Response<List<SparepartDAO>> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(SparepartBySupActivity.this,R.anim.layout_anim_recycle);
                recyclerView.setLayoutAnimation(animationController);

                List<SparepartDAO> sparepartDAOList = response.body();
                recycleAdapterSparepartBySup.notifyDataSetChanged();
                recycleAdapterSparepartBySup=new RecycleAdapterSparepartBySup(SparepartBySupActivity.this,sparepartDAOList);
                recyclerView.setAdapter(recycleAdapterSparepartBySup);
            }

            @Override
            public void onFailure(Call<List<SparepartDAO>> call, Throwable t) {
                //
            }
        });
    }
}
