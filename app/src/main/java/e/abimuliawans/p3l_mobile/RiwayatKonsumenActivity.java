package e.abimuliawans.p3l_mobile;

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

public class RiwayatKonsumenActivity extends AppCompatActivity {

    private List<TransactionByCabangDAO> mListTransaction = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecycleAdapterTransactionKonsumen recycleAdapterTransactionKonsumen;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerViewTransaction;
    private String token,BASE_URL,inputIDcabang;
    private List<String> listSpinnerCity = new ArrayList<>();
    private List<String> listSpinnerCabang = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    private EditText txtNameTrans,txtPhoneTrans,txtCityTrans,txtAddressTrans;
    private ProgressBar progressBar;
    private Spinner spinnerCity,spinnerCabang,spinnerJenisTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_konsumen);

        //Set Token and Base URL
         BASE_URL = "https://api1.thekingcorp.org/";
         token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvYX" +
                 "BpMS50aGVraW5nY29ycC5vcmdcL2F1dGhcL2xvZ2luIiwiaWF0IjoxNTU3OTY0Nzg0LCJuYmYiOjE1NTc5NjQ3OD" +
                 "QsImp0aSI6IjNnRVpDZVJ3N2xYc0VneXEiLCJzdWIiOjEsInBydi" +
                 "I6Ijg3ZTBhZjFlZjlmZDE1ODEyZmRlYzk3MTUzYTE0ZTBiMDQ3NTQ2YWE" +
                 "ifQ.EOhX-R8xWgrKys39o3YJR-BD0YPUP96ScNrw6tx7HCk";

        //Inisialisasi Progres Bar
        progressBar = findViewById(R.id.progress_bar_transaction_konsumen);

        //Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarTransactionKonsumen);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Inisialisasi Recycle
        recyclerView =findViewById(R.id.recyclerViewTransactionKonsumen);
        recycleAdapterTransactionKonsumen= new RecycleAdapterTransactionKonsumen(RiwayatKonsumenActivity.this,mListTransaction);
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycleAdapterTransactionKonsumen);

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
        setRecycleViewTransaction(httpClient);
    }

    public void setRecycleViewTransaction(OkHttpClient.Builder httpClient)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<ValueTransaksiKonsumen> transaction = apiClient.getTransactionKonsumen("1","123456");

        transaction.enqueue(new Callback<ValueTransaksiKonsumen>() {
            @Override
            public void onResponse(Call<ValueTransaksiKonsumen> call, retrofit2.Response<ValueTransaksiKonsumen> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(RiwayatKonsumenActivity.this,R.anim.layout_anim_recycle);
                recyclerView.setLayoutAnimation(animationController);
                recycleAdapterTransactionKonsumen.notifyDataSetChanged();
                recycleAdapterTransactionKonsumen=new RecycleAdapterTransactionKonsumen(RiwayatKonsumenActivity.this,response.body().getResult());
                recyclerView.setAdapter(recycleAdapterTransactionKonsumen);

            }

            @Override
            public void onFailure(Call<ValueTransaksiKonsumen> call, Throwable t) {

                Toasty.error(RiwayatKonsumenActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}
