package e.abimuliawans.p3l_mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
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
    private String token;
    private FloatingActionButton floatingActionButton;
    private EditText txtNameSup,txtPhoneSup,txtCitySup,txtAddressSup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);

        //Set Toolbar
        Toolbar toolbarSupplier = findViewById(R.id.toolbarSupplier);
        setSupportActionBar(toolbarSupplier);

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

        //Floating Button
        floatingActionButton=findViewById(R.id.btnAddSupplier);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SupplierActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_supplier,null);

                txtNameSup=findViewById(R.id.txtSupName);
                txtAddressSup=findViewById(R.id.txtSupAddress);
                txtCitySup=findViewById(R.id.txtSupCity);
                txtPhoneSup=findViewById(R.id.txtSupPhone);

                mBuilder.setView(mView)
                        .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Tambah
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
