package e.abimuliawans.p3l_mobile;

import android.content.Context;
import android.content.SharedPreferences;
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

public class TransaksiDetailActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private List<TransactionDetailDAO> mListTransactionDetail = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecycleAdapterTransactionDetail recycleAdapterTransactionDetail;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerViewTransaction;
    private String token,BASE_URL,inputIdTransaction;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_detail);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);
        BASE_URL = pref.getString("BASE_URL",null);

        //Ambil ID Cabang
        Bundle bundle = getIntent().getExtras();
        inputIdTransaction = bundle.getString("idTrans");

        //Inisialisasi Progres Bar
        progressBar = findViewById(R.id.progress_bar_transaction_detail);

        //Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarTransactionDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Inisialisasi Recycle
        recyclerView =findViewById(R.id.recyclerViewTransactionDetail);
        recycleAdapterTransactionDetail= new RecycleAdapterTransactionDetail(TransaksiDetailActivity.this,mListTransactionDetail);
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycleAdapterTransactionDetail);

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
        setRecycleViewTransactionDetail(httpClient);

    }

    private void setRecycleViewTransactionDetail(OkHttpClient.Builder httpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<TransactionDetailDAO>> transaction = apiClient.getTransactionDetail(inputIdTransaction);

        transaction.enqueue(new Callback<List<TransactionDetailDAO>>() {
            @Override
            public void onResponse(Call<List<TransactionDetailDAO>> call, retrofit2.Response<List<TransactionDetailDAO>> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(TransaksiDetailActivity.this,R.anim.layout_anim_recycle);
                recyclerView.setLayoutAnimation(animationController);
                recycleAdapterTransactionDetail.notifyDataSetChanged();
                recycleAdapterTransactionDetail=new RecycleAdapterTransactionDetail(TransaksiDetailActivity.this,response.body());
                recyclerView.setAdapter(recycleAdapterTransactionDetail);

            }

            @Override
            public void onFailure(Call<List<TransactionDetailDAO>> call, Throwable t) {

                Toasty.error(TransaksiDetailActivity.this, t.getMessage(),
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
        recycleAdapterTransactionDetail.getFilter().filter(s);
        return false;
    }
}
