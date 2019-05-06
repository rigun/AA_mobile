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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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

public class DasboardActivity extends AppCompatActivity {

    private Animation atg, atgtwo, atgthree, fabOpen, fabClose, fabClockwise, fabAntiClockwise
            ,cardOpen,cardClose;
    private TextView pagetitle,pagesubtitle,txtFab;
    private CardView cardViewVeh,cardViewLogout,cardViewSales,cardViewKonsumen;
    private ImageView imageView3,imgSparepart,imgSupplier, imgPemesanan,imgTransaksi;
    private FloatingActionButton fabMenuLainya,fabVehicle,fabLogout,fabSales,fabKonsumen;
    private String token,BASE_URL;
    private Spinner spinnerCabang;
    private List<String> listSpinnerCabang = new ArrayList<String>();
    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard);

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

        //Main Menu Button On Clicked
        imgPemesanan = findViewById(R.id.imgPesanan);
        imgPemesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DasboardActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_pilih_cabang,null);

                loadSpinnerCabang(httpClient);
                spinnerCabang = mView.findViewById(R.id.spinnerPilihCabangOrder);

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

                //Intent intent = new Intent(DasboardActivity.this,PemesananActivity.class);
                //startActivity(intent);
            }
        });

        imgSparepart = findViewById(R.id.imgSparepart);
        imgSparepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DasboardActivity.this, SparepartActivity.class);
                startActivity(i);
            }
        });

        imgSupplier = findViewById(R.id.imgSupplier);
        imgSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefVeh = getApplication().getSharedPreferences("MySupplier", Context.MODE_PRIVATE);
                prefVeh.edit().clear().commit();
                Intent i = new Intent(DasboardActivity.this, SupplierActivity.class);
                startActivity(i);
            }
        });

        imgTransaksi = findViewById(R.id.imgTransaksi);
        imgTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DasboardActivity.this,TransactionActivity.class);
                startActivity(intent);
            }
        });


        atg = AnimationUtils.loadAnimation(DasboardActivity.this, R.anim.atg);
        atgtwo = AnimationUtils.loadAnimation(DasboardActivity.this, R.anim.atgtwo);
        atgthree = AnimationUtils.loadAnimation(DasboardActivity.this, R.anim.atgthree);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        cardOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.card_open);
        cardClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.card_close);
        fabClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        fabAntiClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);

        pagetitle = findViewById(R.id.pagetitle);
        pagesubtitle = findViewById(R.id.pagesubtitle);
        imageView3 = findViewById(R.id.imageView3);

        cardViewVeh = findViewById(R.id.cardViewFab1);
        cardViewLogout = findViewById(R.id.cardViewFab2);
        cardViewSales = findViewById(R.id.cardViewFab3);
        cardViewKonsumen = findViewById(R.id.cardViewFab4);

        fabMenuLainya= findViewById(R.id.btnMenuLainya);
        fabVehicle= findViewById(R.id.btnMenuVehicle);
        fabLogout= findViewById(R.id.btnMenuLogOut);
        fabSales = findViewById(R.id.btnMenuSales);
        fabKonsumen = findViewById(R.id.btnMenuKonsu);

        // Pass an animation
        imageView3.startAnimation(atg);
        pagetitle.startAnimation(atgtwo);
        pagesubtitle.startAnimation(atgtwo);

        //Floating Action Bar On Clicked
        fabMenuLainya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen)
                {
                    fabVehicle.startAnimation(fabClose);
                    fabVehicle.setClickable(false);
                    cardViewVeh.startAnimation(cardClose);

                    fabLogout.startAnimation(fabClose);
                    fabLogout.setClickable(false);
                    cardViewLogout.startAnimation(cardClose);

                    fabSales.startAnimation(fabClose);
                    fabSales.setClickable(false);
                    cardViewSales.startAnimation(cardClose);

                    fabKonsumen.startAnimation(fabClose);
                    fabKonsumen.setClickable(false);
                    cardViewKonsumen.setAnimation(cardClose);
                    isOpen=false;
                }else
                {
                    fabVehicle.startAnimation(fabOpen);
                    fabVehicle.setClickable(true);
                    cardViewVeh.startAnimation(cardOpen);

                    fabLogout.startAnimation(fabOpen);
                    fabLogout.setClickable(true);
                    cardViewLogout.startAnimation(cardOpen);

                    fabSales.startAnimation(fabOpen);
                    fabSales.setClickable(true);
                    cardViewSales.startAnimation(cardOpen);

                    fabKonsumen.startAnimation(fabOpen);
                    fabKonsumen.setClickable(true);
                    cardViewKonsumen.setAnimation(cardOpen);
                    isOpen=true;
                }
            }
        });

        fabVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DasboardActivity.this,VehicleActivity.class);
                startActivity(intent);
            }
        });

        fabLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefVeh = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
                prefVeh.edit().clear().commit();

                Toasty.warning(DasboardActivity.this, "Anda Logout", Toast.LENGTH_SHORT, true).show();
                Intent intent = new Intent(DasboardActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        fabSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DasboardActivity.this,SalesActivity.class);
                startActivity(intent);
            }
        });

        fabKonsumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DasboardActivity.this,KomsumenActivity.class);
                startActivity(intent);
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DasboardActivity.this,
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
