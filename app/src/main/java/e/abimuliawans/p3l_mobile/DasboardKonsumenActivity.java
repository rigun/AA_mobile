package e.abimuliawans.p3l_mobile;

import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class DasboardKonsumenActivity extends AppCompatActivity {

    private Animation atg, atgtwo, atgthree;
    private TextView pagetitle,pagesubtitle,txtFab;
    private ImageView imageView3,imgSparepart,imgCabang, imgHistory,imgAboutUs;
    private String token,BASE_URL;
    private EditText txtNoTelponKonsumen,txtNoPlat;
    private List<String> listSpinnerCabang = new ArrayList<String>();
    private Spinner spinnerCabang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard_konsumen);

        //Set Token dan BASE_URL
        BASE_URL="http://192.168.19.140/K_8725/AA_Api/public/";
        token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xOTIuMT" +
                "Y4LjE5LjE0MFwvS184NzI1XC9BQV9BcGlcL3B1YmxpY1wvYXV0aFwvbG9naW4iLCJpYXQiOjE1NTkw" +
                "ODkzOTksIm5iZiI6MTU1OTA4OTM5OSwianRpIjoiUGczR2J0MEdSYjdCbWxNN" +
                "CIsInN1YiI6MSwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhY" +
                "SJ9.EVyiffzmrhyU0MRWZM9uNKG0_PoDgs_3LyoyqghYW44";

        //Save Token and URL
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyToken", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token_access", token);
        editor.putString("BASE_URL","http://192.168.19.140/K_8725/AA_Api/public/");
        editor.commit();

        //For Konsumen
        SharedPreferences pref2 = getApplicationContext().getSharedPreferences("ForKonsumen", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = pref2.edit();
        editor2.putString("answer", "yes");
        editor2.commit();

        //Main Menu Button
        imgSparepart = findViewById(R.id.imgSparepartKonsumen);
        imgHistory = findViewById(R.id.imgHistory);
        imgAboutUs = findViewById(R.id.imgAboutUs);
        imgCabang = findViewById(R.id.imgCabang);

        //Inisialisasi Animation
        atg = AnimationUtils.loadAnimation(DasboardKonsumenActivity.this, R.anim.atg);
        atgtwo = AnimationUtils.loadAnimation(DasboardKonsumenActivity.this, R.anim.atgtwo);
        atgthree = AnimationUtils.loadAnimation(DasboardKonsumenActivity.this, R.anim.atgthree);

        pagetitle = findViewById(R.id.pagetitle2);
        pagesubtitle = findViewById(R.id.pagesubtitle2);
        imageView3 = findViewById(R.id.imageViewCS);

        // Pass an animation
        imageView3.startAnimation(atg);
        pagetitle.startAnimation(atgtwo);
        pagesubtitle.startAnimation(atgtwo);

        //Pengecekan Bearer Token
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
                return chain.proceed(request);
            }
        });

        //On Click History
        imgHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Alert No Telpon
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DasboardKonsumenActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_input_telp,null);

                //Set Edit Text
                txtNoTelponKonsumen = mView.findViewById(R.id.inputNomorTelponKonsumen);
                txtNoPlat = mView.findViewById(R.id.inputNomorPlatKonsumen);

                mBuilder.setView(mView)
                        .setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Lanjut
                                callBackKonsumen(httpClient,txtNoTelponKonsumen.getText().toString(),txtNoPlat.getText().toString());
                                Intent intent = new Intent(DasboardKonsumenActivity.this,KendaraanKonsmenActivity.class);
                                startActivity(intent);
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

        imgCabang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DasboardKonsumenActivity.this,CabangActivity.class);
                startActivity(intent);
            }
        });

        imgSparepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilderCab = new AlertDialog.Builder(DasboardKonsumenActivity.this);
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
                                Intent intent = new Intent(DasboardKonsumenActivity.this,SparepartWithCabangActivity.class);
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

        imgAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DasboardKonsumenActivity.this,AboutUsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void callBackKonsumen(final OkHttpClient.Builder httpClient, String telp, String plat){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<KonsumenDAO> konsumenReq = apiClient.callBackTransaction(telp,plat);

        konsumenReq.enqueue(new Callback<KonsumenDAO>() {
            @Override
            public void onResponse(Call<KonsumenDAO> call, retrofit2.Response<KonsumenDAO> response) {
                KonsumenDAO konsumenDAO = response.body();
                String tampungNoTelp = konsumenDAO.getPhoneKonsumen();
                String tampungIdVehicle = konsumenDAO.getcVehicleId();

                //Save Token and URL
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyCallback", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("NoTelp", tampungNoTelp);
                editor.putString("idVehicle",tampungIdVehicle);
                editor.commit();
            }

            @Override
            public void onFailure(Call<KonsumenDAO> call, Throwable t) {
                Toasty.error(DasboardKonsumenActivity.this, "Tidak Ditemukan",
                        Toast.LENGTH_SHORT, true).show();
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DasboardKonsumenActivity.this,
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
