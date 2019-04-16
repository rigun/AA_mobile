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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class SupplierActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private List<SupplierDAO> mListSupplier = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecycleAdapterSupplier recycleAdapterSupplier;
    private RecyclerView.LayoutManager layoutManager;
    private String token,inputNama,inputPhone,inputCity,inputAddress,BASE_URL;
    private FloatingActionButton floatingActionButton;
    private EditText txtNameSup,txtPhoneSup,txtCitySup,txtAddressSup;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);
        BASE_URL = pref.getString("BASE_URL",null);

        //Inisialisasi Progres Bar
        progressBar = findViewById(R.id.progress_bar_supplier);

        //Set Toolbar
        Toolbar toolbarSupplier = findViewById(R.id.toolbarSupplier);
        setSupportActionBar(toolbarSupplier);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Inisialisasi Recycle
        recyclerView =findViewById(R.id.recyclerViewSupplier);
        recycleAdapterSupplier= new RecycleAdapterSupplier(SupplierActivity.this,mListSupplier);
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycleAdapterSupplier);


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
        setRecycleViewSupplier(httpClient);

        //Floating Button
        floatingActionButton=findViewById(R.id.btnAddSupplier);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SupplierActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_supplier,null);

                txtNameSup=mView.findViewById(R.id.txtSupName);
                txtAddressSup=mView.findViewById(R.id.txtSupAddress);
                txtCitySup=mView.findViewById(R.id.txtSupCity);
                txtPhoneSup=mView.findViewById(R.id.txtSupPhone);

                inputNama=txtNameSup.getText().toString();
                inputPhone=txtPhoneSup.getText().toString();
                inputCity=txtCitySup.getText().toString();
                inputAddress=txtAddressSup.getText().toString();

                mBuilder.setView(mView)
                        .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Tambah
                                addNewSupplier(httpClient);
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

    public void setRecycleViewSupplier(OkHttpClient.Builder httpClient){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<ValueSupplier> apiClientSupplier = apiClient.getSupplier();

        apiClientSupplier.enqueue(new Callback<ValueSupplier>() {
            @Override
            public void onResponse(Call<ValueSupplier> call, retrofit2.Response<ValueSupplier> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(SupplierActivity.this,R.anim.layout_anim_recycle);
                recyclerView.setLayoutAnimation(animationController);
                recycleAdapterSupplier.notifyDataSetChanged();
                recycleAdapterSupplier=new RecycleAdapterSupplier(SupplierActivity.this,response.body().getResult());
                recyclerView.setAdapter(recycleAdapterSupplier);

            }

            @Override
            public void onFailure(Call<ValueSupplier> call, Throwable t) {

                Toasty.error(SupplierActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    public void addNewSupplier(final OkHttpClient.Builder httpClient){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<SupplierDAO> supplierDAOCall = apiClient.addSupplierReq(txtNameSup.getText().toString(),txtPhoneSup.getText().toString(),
                txtAddressSup.getText().toString(),txtCitySup.getText().toString());

        supplierDAOCall.enqueue(new Callback<SupplierDAO>() {
            @Override
            public void onResponse(Call<SupplierDAO> call, retrofit2.Response<SupplierDAO> response) {

                if(response.isSuccessful())
                {
                    Toasty.success(SupplierActivity.this, "Supplier Berhasil Ditambah",
                            Toast.LENGTH_SHORT, true).show();

                    setRecycleViewSupplier(httpClient);
                }
                else{

                    Toasty.error(SupplierActivity.this, "Gagal Menambahkan Data",
                            Toast.LENGTH_SHORT, true).show();
                }

            }

            @Override
            public void onFailure(Call<SupplierDAO> call, Throwable t) {
                Toasty.error(SupplierActivity.this, t.getMessage(),
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
        recycleAdapterSupplier.getFilter().filter(s);
        return false;
    }
}
