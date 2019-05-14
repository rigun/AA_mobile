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
import android.util.Log;
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

public class DetailSparepartActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private Spinner spinnerCodeSpare;
    private List<TransactionDAO> mListDetailSparepart = new ArrayList<>();
    private String token,BASE_URL,id_cabang,idDetailTrans;
    private ProgressBar progressBar;
    private EditText txtTotal;
    private List<String> listSpinnerCode = new ArrayList<>();
    private RecycleAdapterDetailSparepart recycleAdapterDetailSparepart;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sparepart);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);
        BASE_URL = pref.getString("BASE_URL",null);

        //Inisialisasi Progres Bar
        progressBar = findViewById(R.id.progress_bar_detail_sparepart);

        //Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarDetailSparepart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Inisialisasi Recycle
        recyclerView =findViewById(R.id.recyclerViewDetailSparepart);
        recycleAdapterDetailSparepart= new RecycleAdapterDetailSparepart(DetailSparepartActivity.this,mListDetailSparepart);
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycleAdapterDetailSparepart);

        //Ambil ID Cabang
        SharedPreferences pref2 = getApplicationContext().getSharedPreferences("MyCabang", Context.MODE_PRIVATE);
        id_cabang = pref2.getString("id_cabang",null);

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
        setRecycleViewDetailSparepart(httpClient);

        //Floating Button
        floatingActionButton=findViewById(R.id.btnAddDetailSparepart);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //New Alert
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailSparepartActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_detail_sparepart,null);
                spinnerCodeSpare = mView.findViewById(R.id.spinnerCodeSpareByCabangDetail);
                txtTotal = mView.findViewById(R.id.txtTotalDetailSpare);

                loadCodeSparepartSpinner(httpClient,id_cabang);

                mBuilder.setView(mView)
                        .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Tambah
                                addDetailSparepart(httpClient,idDetailTrans,spinnerCodeSpare.getSelectedItem().toString(),
                                        txtTotal.getText().toString(),id_cabang);
                                listSpinnerCode.clear();
                                spinnerCodeSpare.setAdapter(null);

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


    private void loadCodeSparepartSpinner(OkHttpClient.Builder httpClient, String id_cabang) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<SparepartCabangSupDAO>> sparepartCabang = apiClient.getSparepartCabang(id_cabang);

        sparepartCabang.enqueue(new Callback<List<SparepartCabangSupDAO>>() {
            @Override
            public void onResponse(Call<List<SparepartCabangSupDAO>> call, retrofit2.Response<List<SparepartCabangSupDAO>> response) {
                List<SparepartCabangSupDAO> sparepartDAOS = response.body();
                for(int i=0; i < sparepartDAOS.size(); i++)
                {
                    String code = sparepartDAOS.get(i).getCodeSpareScp();
                    listSpinnerCode.add(code);
                }
                listSpinnerCode.add(0,"-SELECT CODE-");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DetailSparepartActivity.this,
                        android.R.layout.simple_spinner_item,listSpinnerCode);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCodeSpare.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<SparepartCabangSupDAO>> call, Throwable t) {
                //
                Toasty.error(DetailSparepartActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    private void addDetailSparepart(OkHttpClient.Builder httpClient, String idDetailTrans, String codeSpare,
                                    String total, String id_cabang) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<TransactionDAO> transactionDAOCall = apiClient.addDetailSparepart(idDetailTrans,codeSpare,total,
                id_cabang);
        transactionDAOCall.enqueue(new Callback<TransactionDAO>() {
            @Override
            public void onResponse(Call<TransactionDAO> call, retrofit2.Response<TransactionDAO> response) {
                Toasty.success(DetailSparepartActivity.this, "Detail Sparepart Berhasil Ditambah",
                        Toast.LENGTH_SHORT, true).show();

                Intent intent = new Intent(DetailSparepartActivity.this,DasboardActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<TransactionDAO> call, Throwable t) {
                Toasty.error(DetailSparepartActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });

    }

    private void setRecycleViewDetailSparepart(OkHttpClient.Builder httpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<TransactionDAO>> detailSparepartCall = apiClient.getDetailSparepart("3","1");

        detailSparepartCall.enqueue(new Callback<List<TransactionDAO>>() {
            @Override
            public void onResponse(Call<List<TransactionDAO>> call, retrofit2.Response<List<TransactionDAO>> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(DetailSparepartActivity.this,R.anim.layout_anim_recycle);
                recyclerView.setLayoutAnimation(animationController);
                List<TransactionDAO> hasil = response.body();

                recycleAdapterDetailSparepart.notifyDataSetChanged();
                recycleAdapterDetailSparepart=new RecycleAdapterDetailSparepart(DetailSparepartActivity.this,hasil);
                recyclerView.setAdapter(recycleAdapterDetailSparepart);

            }

            @Override
            public void onFailure(Call<List<TransactionDAO>> call, Throwable t) {

                Toasty.error(DetailSparepartActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}
