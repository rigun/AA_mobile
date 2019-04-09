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
import android.widget.EditText;
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

public class PilihKendaraanSpar extends AppCompatActivity {
    private List<VehicleDAO> mListVehicle = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecycleAdapterPilihSpar recycleAdapterPilihSpar;
    private RecyclerView.LayoutManager layoutManager;
    private String token,inputMerk,inputType,stringSend;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_kendaraan_spar);

        //Inisialisasi Progres Bar
        progressBar = findViewById(R.id.progress_bar_pilihVeh);

        //Set Toolbar
        Toolbar toolbarSupplier = findViewById(R.id.toolbarPilihKendaraan);
        setSupportActionBar(toolbarSupplier);

        //Inisialisasi Recycle
        recyclerView =findViewById(R.id.recyclerViewPilihVeh);
        recycleAdapterPilihSpar= new RecycleAdapterPilihSpar(PilihKendaraanSpar.this,mListVehicle);
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycleAdapterPilihSpar);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);


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
        setRecycleViewVehicle(httpClient);

    }

    public void setRecycleViewVehicle(OkHttpClient.Builder httpClient){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api1.thekingcorp.org/")
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<VehicleDAO>> vehicleCall = apiClient.getVehicle();

        vehicleCall.enqueue(new Callback<List<VehicleDAO>>() {
            @Override
            public void onResponse(Call<List<VehicleDAO>> call, retrofit2.Response<List<VehicleDAO>> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(PilihKendaraanSpar.this,R.anim.layout_anim_recycle);
                recyclerView.setLayoutAnimation(animationController);
                List<VehicleDAO> vehicleDAOList = response.body();

                recycleAdapterPilihSpar.notifyDataSetChanged();
                recycleAdapterPilihSpar=new RecycleAdapterPilihSpar(PilihKendaraanSpar.this,vehicleDAOList);
                recyclerView.setAdapter(recycleAdapterPilihSpar);

            }

            @Override
            public void onFailure(Call<List<VehicleDAO>> call, Throwable t) {

                Toasty.error(PilihKendaraanSpar.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}
