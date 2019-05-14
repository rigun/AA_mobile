package e.abimuliawans.p3l_mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
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

public class DetailLayananActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private Spinner spinnerIdService;
    private List<DetailLayananDAO> mListDetailLayanan = new ArrayList<>();
    private String token,BASE_URL,id_cabang,idDetailTrans;
    private ProgressBar progressBar;
    private EditText txtTotal;
    private List<String> listSpinnerIDService = new ArrayList<>();
    private RecycleAdapterDetailLayanan recycleAdapterDetailLayanan;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_layanan);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);
        BASE_URL = pref.getString("BASE_URL",null);

        //Inisialisasi Progres Bar
        progressBar = findViewById(R.id.progress_bar_detail_service);

        //Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarDetailService);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Inisialisasi Recycle
        recyclerView =findViewById(R.id.recyclerViewDetailService);
        recycleAdapterDetailLayanan= new RecycleAdapterDetailLayanan(DetailLayananActivity.this,mListDetailLayanan);
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycleAdapterDetailLayanan);

        //Ambil ID Detail Trans
        SharedPreferences prefIDTrans = getApplicationContext().getSharedPreferences("MyIdDetailTrans", Context.MODE_PRIVATE);
        idDetailTrans = prefIDTrans.getString("id_detail_trans",null);

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
        setRecycleViewDetailLayanan(httpClient);

        //Floating Button
        floatingActionButton=findViewById(R.id.btnAddDetailService);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //New Alert
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailLayananActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_detail_service,null);
                spinnerIdService = mView.findViewById(R.id.spinnerIdService);
                txtTotal = mView.findViewById(R.id.txtTotalDetailService);

                // Load Spinner Data
                loadSpinnerService(httpClient);

                mBuilder.setView(mView)
                        .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Tambah
                                String spinnerService = spinnerIdService.getSelectedItem().toString();
                                String inputService = Character.toString(spinnerService.charAt(0));

                                addDetailLayanan(httpClient,idDetailTrans,inputService,txtTotal.getText().toString());

                                listSpinnerIDService.clear();
                                spinnerIdService.setAdapter(null);

                            }
                        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                             //Batal
                    }
                });

                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

    }

    public void loadSpinnerService(OkHttpClient.Builder httpClient)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<ServiceDAO>> listCall = apiClient.getService();

        listCall.enqueue(new Callback<List<ServiceDAO>>() {
            @Override
            public void onResponse(Call<List<ServiceDAO>> call, retrofit2.Response<List<ServiceDAO>> response) {
                List<ServiceDAO> serviceDAOS = response.body();
                for(int i=0; i < serviceDAOS.size(); i++)
                {
                    String idSer = serviceDAOS.get(i).getIdService();
                    String nameSer = serviceDAOS.get(i).getNameService();
                    String inputSer = idSer+" - "+nameSer;
                    listSpinnerIDService.add(inputSer);
                }
                listSpinnerIDService.add(0,"-SELECT ID SERVICE-");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DetailLayananActivity.this,
                        android.R.layout.simple_spinner_item,listSpinnerIDService);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerIdService.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<ServiceDAO>> call, Throwable t) {
                //
            }
        });
    }

    public void setRecycleViewDetailLayanan(OkHttpClient.Builder httpClient){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<DetailLayananDAO>> detailCall = apiClient.getDetailService(idDetailTrans);

        detailCall.enqueue(new Callback<List<DetailLayananDAO>>() {
            @Override
            public void onResponse(Call<List<DetailLayananDAO>> call, retrofit2.Response<List<DetailLayananDAO>> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(DetailLayananActivity.this,R.anim.layout_anim_recycle);
                recyclerView.setLayoutAnimation(animationController);
                List<DetailLayananDAO> detailLayananDAOS = response.body();

                recycleAdapterDetailLayanan.notifyDataSetChanged();
                recycleAdapterDetailLayanan=new RecycleAdapterDetailLayanan(DetailLayananActivity.this,detailLayananDAOS);
                recyclerView.setAdapter(recycleAdapterDetailLayanan);

            }

            @Override
            public void onFailure(Call<List<DetailLayananDAO>> call, Throwable t) {

                Toasty.error(DetailLayananActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    private void addDetailLayanan(OkHttpClient.Builder httpClient, String idDetailTrans, String idService,
                                    String total) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<TransactionDAO> transactionDAOCall = apiClient.addDetailService(idDetailTrans,idService,total);
        transactionDAOCall.enqueue(new Callback<TransactionDAO>() {
            @Override
            public void onResponse(Call<TransactionDAO> call, retrofit2.Response<TransactionDAO> response) {
                Toasty.success(DetailLayananActivity.this, "Detail Layanan Berhasil Ditambah",
                        Toast.LENGTH_SHORT, true).show();

                Intent intent = new Intent(DetailLayananActivity.this,DasboardActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<TransactionDAO> call, Throwable t) {
                Toasty.error(DetailLayananActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });

    }
}
