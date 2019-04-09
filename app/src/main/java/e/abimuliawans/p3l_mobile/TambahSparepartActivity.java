package e.abimuliawans.p3l_mobile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TambahSparepartActivity extends AppCompatActivity {
    private TextView idSupplier,idVehicle;
    private ImageView imageAddSpa;
    private Spinner spinnerKen;
    private String setIdSup,token,id;
    private EditText codeSpare,nameSpare,merkSpare,typeSpare;
    private OkHttpClient.Builder httpClientAdd;
    Uri imageUri;

    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_sparepart);

        //Set Toolbar
        Toolbar toolbarSpare = findViewById(R.id.toolbarAddSparepart);
        setSupportActionBar(toolbarSpare);

        idVehicle=findViewById(R.id.txtIDVehSapre);
        idSupplier=findViewById(R.id.txtIDSupAddSpare);
        codeSpare=findViewById(R.id.txtAddCodeSpare);
        nameSpare=findViewById(R.id.txtAddNameSpare);
        merkSpare=findViewById(R.id.txtAddMerkSpare);
        typeSpare=findViewById(R.id.txtAddTypeSpare);
        imageAddSpa=findViewById(R.id.imgAddSpare);



        //Pengambilan Data Supplier
        SharedPreferences prefSub = getApplication().getSharedPreferences("MySupplier", Context.MODE_PRIVATE);
        setIdSup= prefSub.getString("id",null);

        //Pengambilan Token
        SharedPreferences pref = getApplication().getSharedPreferences("MyToken", Context.MODE_PRIVATE);
        token = pref.getString("token_access", null);

        //Ambil Data Kendaraan
        Bundle bundle = getIntent().getExtras();
         id = bundle.getString("id");

        //Set Data ID Supplier dan ID Vehicle
        idSupplier.setText(setIdSup);
        idVehicle.setText(id);

        //On Click Image
        imageAddSpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery,"Select Picture"),PICK_IMAGE);
            }
        });

        //Pengecekan Bearer Token
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
                return chain.proceed(request);
            }
        });

        httpClientAdd=httpClient;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_edit,menu);

        MenuItem menuItem = menu.findItem(R.id.action_edit);
        return true;
    }

    public void actionClickEdit(MenuItem menuItem){
        //Fungsi Edit
        addSparepart(httpClientAdd);
    }

    public void addSparepart(OkHttpClient.Builder httpClient)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api1.thekingcorp.org/")
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<SparepartDAO> sparepartDAOCall = apiClient.addSparepart(codeSpare.getText().toString(),nameSpare.getText().toString(),
                merkSpare.getText().toString(),typeSpare.getText().toString(),setIdSup,id
                ,".jpg");

        sparepartDAOCall.enqueue(new Callback<SparepartDAO>() {
            @Override
            public void onResponse(Call<SparepartDAO> call, retrofit2.Response<SparepartDAO> response) {

                if(response.isSuccessful())
                {
                    Toasty.success(TambahSparepartActivity.this, "Sparepart Berhasil Ditambah",
                            Toast.LENGTH_SHORT, true).show();

                    Intent intent = new Intent(TambahSparepartActivity.this,DasboardActivity.class);
                    startActivity(intent);
                }
                else{

                    Toasty.error(TambahSparepartActivity.this, "Gagal Menmabha Data",
                            Toast.LENGTH_SHORT, true).show();
                }

            }

            @Override
            public void onFailure(Call<SparepartDAO> call, Throwable t) {
                Toasty.error(TambahSparepartActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageUri =  data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                imageAddSpa.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
