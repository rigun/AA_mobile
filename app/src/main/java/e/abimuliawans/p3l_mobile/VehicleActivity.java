package e.abimuliawans.p3l_mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.renderscript.Sampler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
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

public class VehicleActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private FloatingActionButton floatingActionButton;
    private EditText txtMerkVeh, txtTipeVeh;
    private Button btnTambahVeh;

    private List<VehicleDAO> mListVehicle = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecycleAdapterVehicle recycleAdapterVehicle;
    private RecyclerView.LayoutManager layoutManager;
    private String token,inputMerk,inputType,stringSend,BASE_URL;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);


        //Inisialisasi Progres Bar
        progressBar = findViewById(R.id.progress_bar_vehicle);

        //Set Toolbar
        Toolbar toolbarSupplier = findViewById(R.id.toolbarSupplier);
        setSupportActionBar(toolbarSupplier);

        //Inisialisasi Recycle
        recyclerView =findViewById(R.id.recyclerViewVehicle);
        recycleAdapterVehicle= new RecycleAdapterVehicle(VehicleActivity.this,mListVehicle);
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycleAdapterVehicle);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);
        BASE_URL = pref.getString("BASE_URL",null);


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

        //Floating Button
        floatingActionButton=findViewById(R.id.btnAdd);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(VehicleActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_vehicle,null);

                txtMerkVeh= mView.findViewById(R.id.txtMerkVeh);
                txtTipeVeh= mView.findViewById(R.id.txtTipeVeh);


                mBuilder.setView(mView)
                .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Tambah
                        addNewVehicle(httpClient);
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


    public void setRecycleViewVehicle(OkHttpClient.Builder httpClient){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<VehicleDAO>> vehicleCall = apiClient.getVehicle();

        vehicleCall.enqueue(new Callback<List<VehicleDAO>>() {
            @Override
            public void onResponse(Call<List<VehicleDAO>> call, retrofit2.Response<List<VehicleDAO>> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(VehicleActivity.this,R.anim.layout_anim_recycle);
                recyclerView.setLayoutAnimation(animationController);
                List<VehicleDAO> vehicleDAOList = response.body();

                recycleAdapterVehicle.notifyDataSetChanged();
                recycleAdapterVehicle=new RecycleAdapterVehicle(VehicleActivity.this,vehicleDAOList);
                recyclerView.setAdapter(recycleAdapterVehicle);

            }

            @Override
            public void onFailure(Call<List<VehicleDAO>> call, Throwable t) {

                Toasty.error(VehicleActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    public void addNewVehicle(final OkHttpClient.Builder httpClient){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<VehicleDAO> vehicleCall = apiClient.addVehicleReq(txtMerkVeh.getText().toString(),txtTipeVeh.getText().toString());

        vehicleCall.enqueue(new Callback<VehicleDAO>() {
            @Override
            public void onResponse(Call<VehicleDAO> call, retrofit2.Response<VehicleDAO> response) {

                if(response.isSuccessful())
                {
                    Toasty.success(VehicleActivity.this, "Kendaraan Berhasil Ditambah",
                            Toast.LENGTH_SHORT, true).show();

                    setRecycleViewVehicle(httpClient);
                }
                else{

                    Toasty.error(VehicleActivity.this, "Gagal Menambahkan Data",
                            Toast.LENGTH_SHORT, true).show();
                }

            }

            @Override
            public void onFailure(Call<VehicleDAO> call, Throwable t) {
                Toasty.error(VehicleActivity.this, t.getMessage(),
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
        recycleAdapterVehicle.getFilter().filter(s);
        return false;
    }


}
