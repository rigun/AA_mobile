package e.abimuliawans.p3l_mobile;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
    private TextView pagetitle,pagesubtitle,txtFab,roleuser,nameuser;
    private CardView cardViewVeh,cardViewLogout,cardViewSales,cardViewKonsumen,cardViewReportPerbulan,cardViewReportPengeluaran,
    cardViewReportStok;
    private ImageView imageView3,imgSparepart,imgSupplier, imgPemesanan,imgTransaksi;
    private FloatingActionButton fabMenuLainya,fabVehicle,fabLogout,fabSales,fabKonsumen,fabReport;
    private String token,BASE_URL,role;
    private Spinner spinnerCabang,spinnerTahunPengeluaran,spinnerTahunPendapatan,spinnerTahunSparepartTer;
    private List<String> listSpinnerCabang = new ArrayList<String>();
    private List<StokSparepartDAO> listStokSparepart;
    private boolean isOpen = false;
    private DownloadManager downloadManager;

    //Channel Notification
    private static final String CHANNEL_ID = "notifikasi_stok_kurang";
    private static final String CHANNEL_NAME = "Stok Sparepart Kurang";
    private static final String CHANNEL_DECS = "Stok Sparepart ini Kurang";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);
        BASE_URL = pref.getString("BASE_URL",null);
        role = pref.getString("role",null);

        //For Konsumen
        SharedPreferences pref2 = getApplicationContext().getSharedPreferences("ForKonsumen", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = pref2.edit();
        editor2.putString("answer", "no");
        editor2.commit();

        //Pengecekan Bearer Token
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
                return chain.proceed(request);
            }
        });

        //Notification
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DECS);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        //Set Stok Kurang
        setStokKurang(httpClient);

        //Interval Show Notif
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(listStokSparepart.size() == 0)
                {
                    //Tidak Terdapat Stok Kurang
                }
                else
                {
                    //Show Notification
                    displayNotification();
                }
            }
        }, 5000);


        //Main Menu Button On Clicked
        imgPemesanan = findViewById(R.id.imgPesanan);
        imgPemesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(role.equals("owner"))
                {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(DasboardActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_pilih_cabang,null);

                    loadSpinnerCabang(httpClient);
                    spinnerCabang = mView.findViewById(R.id.spinnerPilihCabangOrder);

                    mBuilder.setView(mView)
                            .setPositiveButton("Pilih", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Ambil data ID Cabang
                                    String spinnerOrder = spinnerCabang.getSelectedItem().toString();
                                    String inputOrder = Character.toString(spinnerOrder.charAt(0));
                                    Intent intent = new Intent(DasboardActivity.this,PemesananActivity.class);
                                    intent.putExtra("idCabang", inputOrder);
                                    listSpinnerCabang.clear();
                                    spinnerCabang.setAdapter(null);
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
                else if(role.equals("cs"))
                {
                    Toasty.warning(DasboardActivity.this, "Tidak Mendapat Hak Akses",
                            Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        imgSparepart = findViewById(R.id.imgSparepart);
        imgSparepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(role.equals("owner"))
                {
                    Intent i = new Intent(DasboardActivity.this, SparepartActivity.class);
                    startActivity(i);
                }
                else if(role.equals("cs"))
                {
                    Toasty.warning(DasboardActivity.this, "Tidak Mendapat Hak Akses",
                            Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        imgSupplier = findViewById(R.id.imgSupplier);
        imgSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(role.equals("owner"))
                {
                    SharedPreferences prefVeh = getApplication().getSharedPreferences("MySupplier", Context.MODE_PRIVATE);
                    prefVeh.edit().clear().commit();
                    Intent i = new Intent(DasboardActivity.this, SupplierActivity.class);
                    startActivity(i);
                }
                else if(role.equals("cs"))
                {
                    Toasty.warning(DasboardActivity.this, "Tidak Mendapat Hak Akses",
                            Toast.LENGTH_SHORT, true).show();
                }

            }
        });

        imgTransaksi = findViewById(R.id.imgTransaksi);
        imgTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(role.equals("owner"))
                {
                    Toasty.warning(DasboardActivity.this, "Tidak Mendapat Hak Akses",
                            Toast.LENGTH_SHORT, true).show();
                }
                else if(role.equals("cs"))
                {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(DasboardActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_pilih_cabang,null);

                    loadSpinnerCabang(httpClient);
                    spinnerCabang = mView.findViewById(R.id.spinnerPilihCabangOrder);

                    mBuilder.setView(mView)
                            .setPositiveButton("Pilih", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Ambil data ID Cabang
                                    String spinnerTrans = spinnerCabang.getSelectedItem().toString();
                                    String inputTrans = Character.toString(spinnerTrans.charAt(0));
                                    Intent intent = new Intent(DasboardActivity.this,TransactionActivity.class);
                                    intent.putExtra("idCabang", inputTrans);
                                    listSpinnerCabang.clear();
                                    spinnerCabang.setAdapter(null);
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
            }
        });

        roleuser = findViewById(R.id.roleuser);
        if(role.equals("owner"))
        {
            roleuser.setText("Owner");
        }
        else if(role.equals("cs"))
        {
            roleuser.setText("CS");
        }

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
        fabReport= findViewById(R.id.btnMenuReport);
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

        fabReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Cek role
                if(role.equals("cs"))
                {

                }else if(role.equals("owner"))
                {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(DasboardActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_option_report,null);

                    cardViewReportPengeluaran = mView.findViewById(R.id.cardViewPengeluaranPerbulan);
                    cardViewReportPerbulan = mView.findViewById(R.id.cardViewPendapatanPerbulan);
                    cardViewReportStok = mView.findViewById(R.id.cardViewSparepartTerlaris);

                    mBuilder.setView(mView)
                            .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Batal
                                }
                            });

                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();

                    cardViewReportPengeluaran.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Fungsi Report
                            dialog.cancel();
                            AlertDialog.Builder mBuilderYearsPengeluaran = new AlertDialog.Builder(DasboardActivity.this);
                            View mViewYearsPegeluaran = getLayoutInflater().inflate(R.layout.dialog_input_tahun,null);
                            spinnerTahunPengeluaran = mViewYearsPegeluaran.findViewById(R.id.spinnerTahunReport);

                            mBuilderYearsPengeluaran.setView(mViewYearsPegeluaran)
                                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Batal
                                        }
                                    })
                                    .setPositiveButton("Pilih", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Pilih
                                            downloadRepotPengeluaran(spinnerTahunPengeluaran.getSelectedItem().toString());
                                        }
                                    });

                            AlertDialog dialogPengeluaran = mBuilderYearsPengeluaran.create();
                            dialogPengeluaran.show();

                        }
                    });

                    cardViewReportStok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Fungsi Report
                            dialog.cancel();
                            AlertDialog.Builder mBuilderYearsStok = new AlertDialog.Builder(DasboardActivity.this);
                            View mViewYearsStok = getLayoutInflater().inflate(R.layout.dialog_input_tahun,null);
                            spinnerTahunSparepartTer = mViewYearsStok.findViewById(R.id.spinnerTahunReport);

                            mBuilderYearsStok.setView(mViewYearsStok)
                                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Batal
                                        }
                                    })
                                    .setPositiveButton("Pilih", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Pilih
                                            downloadRepotSparepartTerlaris(spinnerTahunSparepartTer.getSelectedItem().toString());
                                        }
                                    });

                            AlertDialog dialogStok = mBuilderYearsStok.create();
                            dialogStok.show();
                        }
                    });

                    cardViewReportPerbulan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Fugsi Report
                            dialog.cancel();
                            AlertDialog.Builder mBuilderYearsPendapatan = new AlertDialog.Builder(DasboardActivity.this);
                            View mViewYearsPendapatan = getLayoutInflater().inflate(R.layout.dialog_input_tahun,null);
                            spinnerTahunPendapatan = mViewYearsPendapatan.findViewById(R.id.spinnerTahunReport);

                            mBuilderYearsPendapatan.setView(mViewYearsPendapatan)
                                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Batal
                                        }
                                    })
                                    .setPositiveButton("Pilih", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Pilih
                                            downloadRepotPerbulan(spinnerTahunPendapatan.getSelectedItem().toString());
                                        }
                                    });

                            AlertDialog dialogPendapatan = mBuilderYearsPendapatan.create();
                            dialogPendapatan.show();
                        }
                    });
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

                if(role.equals("owner"))
                {
                    Intent intent = new Intent(DasboardActivity.this,SalesActivity.class);
                    startActivity(intent);
                }
                else if(role.equals("cs"))
                {
                    Toasty.warning(DasboardActivity.this, "Tidak Mendapat Hak Akses",
                            Toast.LENGTH_SHORT, true).show();
                }

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

    private void displayNotification()
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.icplug)
                .setContentTitle("Atma Auto")
                .setContentText("Terdapat Stok Sparepart Kurang")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icplug));

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, StokSparepartActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(contentIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1,mBuilder.build());
    }

    public void downloadRepotPerbulan(String years)
    {
        downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(BASE_URL+"pendapatanbulanan/report/"+years);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);
        Toasty.success(DasboardActivity.this, "Download Report....",
                Toast.LENGTH_SHORT, true).show();
    }

    public void downloadRepotPengeluaran(String years)
    {
        downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(BASE_URL+"pengeluaranbulanan/report/"+years);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);
        Toasty.success(DasboardActivity.this, "Download Report....",
                Toast.LENGTH_SHORT, true).show();
    }

    public void downloadRepotSparepartTerlaris(String years)
    {
        downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(BASE_URL+"sparepartTerlaris/report/"+years);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);
        Toasty.success(DasboardActivity.this, "Download Report....",
                Toast.LENGTH_SHORT, true).show();
    }

    public void setStokKurang(OkHttpClient.Builder httpClient)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<StokSparepartDAO>> listCall = apiClient.getStokKurang();

        listCall.enqueue(new Callback<List<StokSparepartDAO>>() {
            @Override
            public void onResponse(Call<List<StokSparepartDAO>> call, retrofit2.Response<List<StokSparepartDAO>> response) {
                List<StokSparepartDAO> stokSparepartDAOS = response.body();
                listStokSparepart = stokSparepartDAOS;
            }

            @Override
            public void onFailure(Call<List<StokSparepartDAO>> call, Throwable t) {

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
