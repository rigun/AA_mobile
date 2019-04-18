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

public class KomsumenActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private List<KonsumenDAO> mListKonsumen = new ArrayList<>();
    private List<String> listSpinner = new ArrayList<>();
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private RecycleAdapterKonsumen recycleAdapterKonsumen;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerViewKonsumen;
    private EditText nameKonsumen,phoneKonsumen,alamatKonsumen;
    private Spinner spinnerCity;
    private String token,BASE_URL;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komsumen);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);
        BASE_URL = pref.getString("BASE_URL",null);

        //Inisialisasi Progres Bar
        progressBar = findViewById(R.id.progress_bar_Konsumen);

        //Set Toolbar
        Toolbar toolbarSales = findViewById(R.id.toolbarKonsumen);
        setSupportActionBar(toolbarSales);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Inisialisasi Recycle
        recyclerView =findViewById(R.id.recyclerViewKonsumen);
        recycleAdapterKonsumen= new RecycleAdapterKonsumen(KomsumenActivity.this,mListKonsumen);
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycleAdapterKonsumen);

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
        setRecycleViewKonsumen(httpClient);

        //Floating Button
        floatingActionButton=findViewById(R.id.btnAddKonsumen);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //New Alert
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(KomsumenActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_konsumen,null);

                //Set Spinner City
                loadSpinnerCities(httpClient);

                nameKonsumen = mView.findViewById(R.id.txtKonsumenName);
                phoneKonsumen = mView.findViewById(R.id.txtKonsumenPhone);
                alamatKonsumen = mView.findViewById(R.id.txtKonsumenAddress);
                spinnerCity = mView.findViewById(R.id.spinnerCityKonsuemn);

                mBuilder.setView(mView)
                        .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Tambah
                                addNewKonsumen(httpClient);
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
                    listSpinner.add(name);
                }
                listSpinner.add(0,"-SELECT NAME CITY-");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(KomsumenActivity.this,
                        android.R.layout.simple_spinner_item,listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCity.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<CitiesDAO>> call, Throwable t) {

            }
        });
    }


    public void setRecycleViewKonsumen(OkHttpClient.Builder httpClient)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<ValueKonsumen> apiKonsumen = apiClient.getKonsumen();

        apiKonsumen.enqueue(new Callback<ValueKonsumen>() {
            @Override
            public void onResponse(Call<ValueKonsumen> call, retrofit2.Response<ValueKonsumen> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(KomsumenActivity.this,R.anim.layout_anim_recycle);
                recyclerView.setLayoutAnimation(animationController);
                recycleAdapterKonsumen.notifyDataSetChanged();
                recycleAdapterKonsumen=new RecycleAdapterKonsumen(KomsumenActivity.this,response.body().getResult());
                recyclerView.setAdapter(recycleAdapterKonsumen);

            }

            @Override
            public void onFailure(Call<ValueKonsumen> call, Throwable t) {

                Toasty.error(KomsumenActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    public void addNewKonsumen(final OkHttpClient.Builder httpClient){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<KonsumenDAO> konsumenReq = apiClient.addKonsumenReq(nameKonsumen.getText().toString(),phoneKonsumen.getText().toString(),
                alamatKonsumen.getText().toString(),spinnerCity.getSelectedItem().toString());

        konsumenReq.enqueue(new Callback<KonsumenDAO>() {
            @Override
            public void onResponse(Call<KonsumenDAO> call, retrofit2.Response<KonsumenDAO> response) {

                if(response.isSuccessful())
                {
                    Toasty.success(KomsumenActivity.this, "Konsumen Berhasil Ditambah",
                            Toast.LENGTH_SHORT, true).show();

                    setRecycleViewKonsumen(httpClient);
                }
                else{

                    Toasty.error(KomsumenActivity.this, "Gagal Menambahkan Data",
                            Toast.LENGTH_SHORT, true).show();
                }

            }

            @Override
            public void onFailure(Call<KonsumenDAO> call, Throwable t) {
                Toasty.error(KomsumenActivity.this, t.getMessage(),
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
        recycleAdapterKonsumen.getFilter().filter(s);
        return false;
    }
}
