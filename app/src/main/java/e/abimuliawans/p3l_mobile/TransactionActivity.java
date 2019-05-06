package e.abimuliawans.p3l_mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
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

public class TransactionActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private List<TransactionByCabangDAO> mListTransaction = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecycleAdapterTransaction recycleAdapterTransaction;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerViewTransaction;
    private String token,BASE_URL;
    private List<String> listSpinnerCity = new ArrayList<>();
    private List<String> listSpinnerCabang = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    private EditText txtNameTrans,txtPhoneTrans,txtCityTrans,txtAddressTrans;
    private ProgressBar progressBar;
    private Spinner spinnerCity,spinnerCabang,spinnerJenisTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);
        BASE_URL = pref.getString("BASE_URL",null);

        //Inisialisasi Progres Bar
        progressBar = findViewById(R.id.progress_bar_transaction);

        //Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarTransaction);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Inisialisasi Recycle
        recyclerView =findViewById(R.id.recyclerViewTransaction);
        recycleAdapterTransaction= new RecycleAdapterTransaction(TransactionActivity.this,mListTransaction);
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getApplicationContext());
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
        setRecycleViewTransaction(httpClient);

        floatingActionButton = findViewById(R.id.btnAddTransaction);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(TransactionActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_transaction,null);

                loadSpinnerCities(httpClient);
                loadSpinnerCabang(httpClient);

                //Inisialisasi
                spinnerCity = mView.findViewById(R.id.spinnerCityTrans);
                spinnerCabang = mView.findViewById(R.id.spinnerCabangTrans);
                spinnerJenisTrans = mView.findViewById(R.id.spinnerJenisTrans);
                txtNameTrans = mView.findViewById(R.id.txtNameTrans);
                txtAddressTrans = mView.findViewById(R.id.txtAddressTrans);
                txtPhoneTrans = mView.findViewById(R.id.txtPhoneTrans);

                mBuilder.setView(mView)
                        .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Tambah
                                addNewTransaction(httpClient);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void loadSpinnerCities(OkHttpClient.Builder httpClient)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<CitiesDAO>> listCall = apiClient.getCities();

        listCall.enqueue(new Callback<List<CitiesDAO>>() {
            @Override
            public void onResponse(Call<List<CitiesDAO>> call, retrofit2.Response<List<CitiesDAO>> response) {
                List<CitiesDAO> citiesDAOS = response.body();
                for(int i=0; i < citiesDAOS.size(); i++ ){
                    String name = citiesDAOS.get(i).getNameCities();
                    listSpinnerCity.add(name);
                }
                listSpinnerCity.add(0,"-SELECT NAME CITY-");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TransactionActivity.this,
                        android.R.layout.simple_spinner_item,listSpinnerCity);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCity.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<CitiesDAO>> call, Throwable t) {

            }
        });
    }

    public void loadSpinnerCabang(OkHttpClient.Builder httpClient)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<CabangDAO>> cabangReq = apiClient.getCabang();

        cabangReq.enqueue(new Callback<List<CabangDAO>>() {
            @Override
            public void onResponse(Call<List<CabangDAO>> call, retrofit2.Response<List<CabangDAO>> response) {
                List<CabangDAO> cabangDAOList = response.body();
                for(int i=0; i < cabangDAOList.size(); i++)
                {
                    String idCab = cabangDAOList.get(i).getIdCabang();
                    String nameCab = cabangDAOList.get(i).getNamaCabang();
                    String inputCab = idCab+" - "+nameCab;
                    listSpinnerCabang.add(inputCab);
                }
                listSpinnerCabang.add(0,"-SELECT ID CABANG-");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TransactionActivity.this,
                        android.R.layout.simple_spinner_item,listSpinnerCabang);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCabang.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<CabangDAO>> call, Throwable t) {
                //
            }
        });
    }

    public void setRecycleViewTransaction(OkHttpClient.Builder httpClient)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<TransactionByCabangDAO>> transaction = apiClient.getTransactionCabang("1");

        transaction.enqueue(new Callback<List<TransactionByCabangDAO>>() {
            @Override
            public void onResponse(Call<List<TransactionByCabangDAO>> call, retrofit2.Response<List<TransactionByCabangDAO>> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(TransactionActivity.this,R.anim.layout_anim_recycle);
                recyclerView.setLayoutAnimation(animationController);
                recycleAdapterTransaction.notifyDataSetChanged();
                recycleAdapterTransaction=new RecycleAdapterTransaction(TransactionActivity.this,response.body());
                recyclerView.setAdapter(recycleAdapterTransaction);

            }

            @Override
            public void onFailure(Call<List<TransactionByCabangDAO>> call, Throwable t) {

                Toasty.error(TransactionActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    public void addNewTransaction(final OkHttpClient.Builder httpClient){

        String idCabang = spinnerCabang.getSelectedItem().toString();
        String inputIDCabang = Character.toString(idCabang.charAt(0));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<TransactionDAO> transactionDAOCall = apiClient.addTransaction(txtNameTrans.getText().toString(),txtPhoneTrans.getText().toString()
        ,txtAddressTrans.getText().toString(),spinnerCity.getSelectedItem().toString(),inputIDCabang,"konsumen"
                ,spinnerJenisTrans.getSelectedItem().toString());

        transactionDAOCall.enqueue(new Callback<TransactionDAO>() {
            @Override
            public void onResponse(Call<TransactionDAO> call, retrofit2.Response<TransactionDAO> response) {

                if(response.isSuccessful())
                {
                    Toasty.success(TransactionActivity.this, "Transaksis Berhasil Ditambah",
                            Toast.LENGTH_SHORT, true).show();

                    //setRecycleViewKonsumen(httpClient);
                }
                else{

                    Toasty.error(TransactionActivity.this, "Gagal Menambahkan Data",
                            Toast.LENGTH_SHORT, true).show();
                }

            }

            @Override
            public void onFailure(Call<TransactionDAO> call, Throwable t) {
                Toasty.error(TransactionActivity.this, t.getMessage(),
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
        recycleAdapterTransaction.getFilter().filter(s);
        return false;
    }
}
