package e.abimuliawans.p3l_mobile;

import android.content.Context;
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

public class KendaraanKonsmenActivity extends AppCompatActivity {

    private List<KendaraanKonsumenDAO> mListKendaraanKonsumen = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecycleAdapterKendaraanKonsumen recycleAdapterKendaraanKonsumen;
    private RecyclerView.LayoutManager layoutManager;
    private String token,BASE_URL,inputID,noTelp;
    private EditText txtNameTrans,txtPhoneTrans,txtCityTrans,txtAddressTrans;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kendaraan_konsmen);

        //Pengambilan Token
        //Set Token and Base URL
        BASE_URL = "http://10.53.0.196/";
        token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xMC41My4" +
                "wLjE5NlwvYXV0aFwvbG9naW4iLCJpYXQiOjE1NTg0ODcwMTYsIm5iZiI6MTU1ODQ4NzAxNiwianRpIjoibUpVS" +
                "G03YzlvNDR1SEJCaSIsInN1Y" +
                "iI6MSwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.lR1MLziMcqMadRFXU7xp3OMRvZ25qX81zHxapXYqTTg";

        //Inisialisasi Progres Bar
        progressBar = findViewById(R.id.progress_bar_kendaraan_konsumen);

        //Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarKendaraanKonsumen);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Inisialisasi Recycle
        recyclerView =findViewById(R.id.recyclerViewKendaraanKonsumen);
        recycleAdapterKendaraanKonsumen= new RecycleAdapterKendaraanKonsumen(KendaraanKonsmenActivity.this,mListKendaraanKonsumen);
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycleAdapterKendaraanKonsumen);

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
        setRecycleViewKendaraanKonsumen(httpClient);
        setListKendaraanKonsumen(httpClient,"8");

    }

    private void setRecycleViewKendaraanKonsumen(final OkHttpClient.Builder httpClient2) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient2.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<KonsumenDAO> transaction = apiClient.getCekKonsumen("123456");

        transaction.enqueue(new Callback<KonsumenDAO>() {
            @Override
            public void onResponse(Call<KonsumenDAO> call, retrofit2.Response<KonsumenDAO> response) {
                KonsumenDAO hasil = response.body();
            }

            @Override
            public void onFailure(Call<KonsumenDAO> call, Throwable t) {
                //
            }
        });
    }

    public void setListKendaraanKonsumen(OkHttpClient.Builder httpClient,String inputId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<KendaraanKonsumenDAO> transaction = apiClient.getKendaraanKonsumen(inputId);

        transaction.enqueue(new Callback<KendaraanKonsumenDAO>() {
            @Override
            public void onResponse(Call<KendaraanKonsumenDAO> call, retrofit2.Response<KendaraanKonsumenDAO> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(KendaraanKonsmenActivity.this,R.anim.layout_anim_recycle);
                recyclerView.setLayoutAnimation(animationController);
                recycleAdapterKendaraanKonsumen.notifyDataSetChanged();
                KendaraanKonsumenDAO kendaraanKonsmenActivity = response.body();
                //recycleAdapterKendaraanKonsumen=new RecycleAdapterKendaraanKonsumen(KendaraanKonsmenActivity.this,response.body());
                recyclerView.setAdapter(recycleAdapterKendaraanKonsumen);

            }

            @Override
            public void onFailure(Call<KendaraanKonsumenDAO> call, Throwable t) {

                Toasty.error(KendaraanKonsmenActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}
