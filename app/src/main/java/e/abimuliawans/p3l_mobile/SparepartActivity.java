package e.abimuliawans.p3l_mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SparepartActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private List<SparepartDAO> mListSparepart = new ArrayList<>();
    private List<String> listSpinnerCabang = new ArrayList<String>();
    private RecyclerView recyclerView;
    private RecycleAdapterSparepart recycleAdapterSparepart;
    private RecyclerView.LayoutManager layoutManager;
    private String token,BASE_URL;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;
    private CardView cardViewStokKurang,cardViewCekStok;
    private Spinner spinnerCabang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sparepart);

        //Inisialisasi Progres Bar
        progressBar = findViewById(R.id.progress_bar_sparepart);

        //Set Toolbar
        Toolbar toolbarSparepart = findViewById(R.id.toolbarSparepart);
        setSupportActionBar(toolbarSparepart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Inisialisasi Recycle
        recyclerView =findViewById(R.id.recyclerViewSparepart);
        recycleAdapterSparepart= new RecycleAdapterSparepart(SparepartActivity.this,mListSparepart);
        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycleAdapterSparepart);

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
        setRecycleViewSparepart(httpClient);

        //Floating Action Button Show Stok
        floatingActionButton = findViewById(R.id.btnShowStokSparepart);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Alert Option Sparepart
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SparepartActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_option_sparepart,null);

                //Set Card View
                cardViewStokKurang = mView.findViewById(R.id.cardViewStokKurang);
                cardViewCekStok = mView.findViewById(R.id.cardViewCekStokSparepart);

                //On Click Stok Kurang
                cardViewStokKurang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SparepartActivity.this,StokSparepartActivity.class);
                        startActivity(intent);
                    }
                });

                //On Click Cek Stok
                cardViewCekStok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder mBuilderCab = new AlertDialog.Builder(SparepartActivity.this);
                        View mViewCab = getLayoutInflater().inflate(R.layout.dialog_pilih_cabang,null);

                        spinnerCabang = mViewCab.findViewById(R.id.spinnerPilihCabangOrder);
                        //Set Spinner
                        loadSpinnerCabang(httpClient);

                        mBuilderCab.setView(mViewCab)
                                .setPositiveButton("Pilih", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Ambil data ID Cabang
                                        String spinnerOrder = spinnerCabang.getSelectedItem().toString();
                                        String inputOrder = Character.toString(spinnerOrder.charAt(0));
                                        Intent intent = new Intent(SparepartActivity.this,SparepartWithCabangActivity.class);
                                        intent.putExtra("idCabang", inputOrder);
                                        listSpinnerCabang.clear();
                                        spinnerCabang.setAdapter(null);
                                        startActivity(intent);

                                        dialog.cancel();

                                    }
                                }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Batal
                            }
                        });


                        AlertDialog dialogCab = mBuilderCab.create();
                        dialogCab.show();
                    }
                });

                mBuilder.setView(mView)
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
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

    public void setRecycleViewSparepart(OkHttpClient.Builder httpClient){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<SparepartDAO>> sparepartCall = apiClient.getSparepart();

        sparepartCall.enqueue(new Callback<List<SparepartDAO>>() {
            @Override
            public void onResponse(Call<List<SparepartDAO>> call, retrofit2.Response<List<SparepartDAO>> response) {
                progressBar.setVisibility(View.GONE);
                LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(SparepartActivity.this,R.anim.layout_anim_recycle);
                recyclerView.setLayoutAnimation(animationController);

                List<SparepartDAO> sparepartDAOList = response.body();
                recycleAdapterSparepart.notifyDataSetChanged();
                recycleAdapterSparepart=new RecycleAdapterSparepart(SparepartActivity.this,sparepartDAOList);
                recyclerView.setAdapter(recycleAdapterSparepart);
            }

            @Override
            public void onFailure(Call<List<SparepartDAO>> call, Throwable t) {

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
        recycleAdapterSparepart.getFilter().filter(s);
        return false;
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SparepartActivity.this,
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

}
