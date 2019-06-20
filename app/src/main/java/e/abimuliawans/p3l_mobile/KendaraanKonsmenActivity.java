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

    private List<TransactionByCabangDAO> mListTransaction = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecycleAdapterTransaction recycleAdapterTransaction;
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
        BASE_URL = "https://api1.thekingcorp.org/";
        token = "";

        //Inisialisasi Progres Bar
        progressBar = findViewById(R.id.progress_bar_kendaraan_konsumen);

        //Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarKendaraanKonsumen);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Inisialisasi Recycle
        recyclerView = findViewById(R.id.recyclerViewKendaraanKonsumen);
        recycleAdapterTransaction = new RecycleAdapterTransaction(KendaraanKonsmenActivity.this, mListTransaction);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycleAdapterTransaction);

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

    }

    private void setRecycleViewKendaraanKonsumen(final OkHttpClient.Builder httpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<ValueKonsumen> transaction = apiClient.getTransKonsumen("1","12345");

        transaction.enqueue(new Callback<ValueKonsumen>() {
            @Override
            public void onResponse(Call<ValueKonsumen> call, retrofit2.Response<ValueKonsumen> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(KendaraanKonsmenActivity.this,R.anim.layout_anim_recycle);
                /*recyclerView.setLayoutAnimation(animationController);
                recycleAdapterTransaction.notifyDataSetChanged();
                recycleAdapterTransaction=new RecycleAdapterTransaction(KendaraanKonsmenActivity.this,response.body().getResult());
                recyclerView.setAdapter(recycleAdapterTransaction);*/

            }

            @Override
            public void onFailure(Call<ValueKonsumen> call, Throwable t) {

                Toasty.error(KendaraanKonsmenActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }


}
