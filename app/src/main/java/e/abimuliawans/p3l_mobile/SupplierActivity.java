package e.abimuliawans.p3l_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class SupplierActivity extends AppCompatActivity {

    private List<SupplierDAO> mListSupplier = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecycleAdapterSupplier recycleAdapterSupplier;
    private RecyclerView.LayoutManager layoutManager;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);

        //Inisialisasi Recycle
        recyclerView =findViewById(R.id.recyclerViewSupplier);
        recycleAdapterSupplier= new RecycleAdapterSupplier(SupplierActivity.this,mListSupplier);
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycleAdapterSupplier);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        token=(String)bundle.get("token");
        setRecycleViewSupplier();

    }

    public void setRecycleViewSupplier(){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
                return chain.proceed(request);
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api1.thekingcorp.org/")
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<SupplierDAO>> apiClientSupplier = apiClient.getSupplier();

        apiClientSupplier.enqueue(new Callback<List<SupplierDAO>>() {
            @Override
            public void onResponse(Call<List<SupplierDAO>> call, retrofit2.Response<List<SupplierDAO>> response) {
                List<SupplierDAO> supplierDAOList = response.body();
                recycleAdapterSupplier.notifyDataSetChanged();
                recycleAdapterSupplier=new RecycleAdapterSupplier(SupplierActivity.this,supplierDAOList);
                recyclerView.setAdapter(recycleAdapterSupplier);

            }

            @Override
            public void onFailure(Call<List<SupplierDAO>> call, Throwable t) {

                Toasty.error(SupplierActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }

}
