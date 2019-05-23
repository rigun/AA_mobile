package e.abimuliawans.p3l_mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import java.util.Collections;
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

public class SparepartWithCabangActivity extends AppCompatActivity {

    private RecycleAdapterStokSparepart recycleAdapterStokSparepart;
    private RecyclerView.LayoutManager layoutManager;
    private String token,BASE_URL;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private List<StokSparepartDAO> mStokSparepart = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    private CardView cardViewMahal,cardViewMurah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sparepart_with_cabang);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);
        BASE_URL = pref.getString("BASE_URL",null);

        //Inisialisasi Progres Bar
        progressBar = findViewById(R.id.progress_bar_sparepart_with_cabang);

        //Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarSparepartWithCabang);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //Inisialisasi Recycle
        recyclerView =findViewById(R.id.recyclerViewSparepartWithCabang);
        recycleAdapterStokSparepart= new RecycleAdapterStokSparepart(SparepartWithCabangActivity.this,mStokSparepart);
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

        //Floating Action button
        floatingActionButton = findViewById(R.id.btnSortingSparepart);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SparepartWithCabangActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_option_sort_sparepart,null);

                cardViewMahal = mView.findViewById(R.id.cardViewMahalKeMurah);
                cardViewMurah = mView.findViewById(R.id.cardViewMurahKeMahal);

                //Click Mahal
                cardViewMahal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Function Sort
                        setRecycleViewSortingMahal(httpClient);
                    }
                });

                //Click Murah
                cardViewMurah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Function Sort
                        setRecycleViewSortingMurah(httpClient);
                    }
                });

                mBuilder.setView(mView)
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Batal
                            }
                        });

                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        //Set View Stok Kurang
        setRecycleViewSparepartCabang(httpClient);

    }

    private void setRecycleViewSortingMahal(OkHttpClient.Builder httpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<StokSparepartDAO>> listCall = apiClient.getSparepartWithCabang("1");

        listCall.enqueue(new Callback<List<StokSparepartDAO>>() {
            @Override
            public void onResponse(Call<List<StokSparepartDAO>> call, retrofit2.Response<List<StokSparepartDAO>> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(SparepartWithCabangActivity.this,R.anim.layout_anim_recycle);
                List<StokSparepartDAO> stokSparepartDAOS = response.body();
                Collections.sort(stokSparepartDAOS, StokSparepartDAO.BY_HARGA_SORT);
                recyclerView.setLayoutAnimation(animationController);
                recycleAdapterStokSparepart.notifyDataSetChanged();
                recycleAdapterStokSparepart=new RecycleAdapterStokSparepart(SparepartWithCabangActivity.this,stokSparepartDAOS);
                recyclerView.setAdapter(recycleAdapterStokSparepart);

                Toasty.success(SparepartWithCabangActivity.this, "Sorting dari Harga Termahal",
                        Toast.LENGTH_SHORT, true).show();

            }

            @Override
            public void onFailure(Call<List<StokSparepartDAO>> call, Throwable t) {

                Toasty.error(SparepartWithCabangActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    private void setRecycleViewSortingMurah(OkHttpClient.Builder httpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<StokSparepartDAO>> listCall = apiClient.getSparepartWithCabang("1");

        listCall.enqueue(new Callback<List<StokSparepartDAO>>() {
            @Override
            public void onResponse(Call<List<StokSparepartDAO>> call, retrofit2.Response<List<StokSparepartDAO>> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(SparepartWithCabangActivity.this,R.anim.layout_anim_recycle);
                List<StokSparepartDAO> stokSparepartDAOS = response.body();
                Collections.sort(stokSparepartDAOS, StokSparepartDAO.BY_HARGA_SORT_MURAH);
                recyclerView.setLayoutAnimation(animationController);
                recycleAdapterStokSparepart.notifyDataSetChanged();
                recycleAdapterStokSparepart=new RecycleAdapterStokSparepart(SparepartWithCabangActivity.this,stokSparepartDAOS);
                recyclerView.setAdapter(recycleAdapterStokSparepart);

                Toasty.success(SparepartWithCabangActivity.this, "Sorting dari Harga Termahal",
                        Toast.LENGTH_SHORT, true).show();

            }

            @Override
            public void onFailure(Call<List<StokSparepartDAO>> call, Throwable t) {

                Toasty.error(SparepartWithCabangActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    private void setRecycleViewSparepartCabang(OkHttpClient.Builder httpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<StokSparepartDAO>> listCall = apiClient.getSparepartWithCabang("1");

        listCall.enqueue(new Callback<List<StokSparepartDAO>>() {
            @Override
            public void onResponse(Call<List<StokSparepartDAO>> call, retrofit2.Response<List<StokSparepartDAO>> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(SparepartWithCabangActivity.this,R.anim.layout_anim_recycle);
                recyclerView.setLayoutAnimation(animationController);
                recycleAdapterStokSparepart.notifyDataSetChanged();
                List<StokSparepartDAO> stokSparepartDAOS = response.body();

                recycleAdapterStokSparepart=new RecycleAdapterStokSparepart(SparepartWithCabangActivity.this,stokSparepartDAOS);
                recyclerView.setAdapter(recycleAdapterStokSparepart);

            }

            @Override
            public void onFailure(Call<List<StokSparepartDAO>> call, Throwable t) {

                Toasty.error(SparepartWithCabangActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}
