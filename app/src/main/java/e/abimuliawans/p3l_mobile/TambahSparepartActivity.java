package e.abimuliawans.p3l_mobile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.TextView;

import java.io.IOException;

public class TambahSparepartActivity extends AppCompatActivity {
    private TextView idSupplier;
    private ImageView imageAddSpa;
    private String setIdSup;
    private EditText codeSpare,nameSpare,merkSpare,typeSpare;
    Uri imageUri;

    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_sparepart);

        //Set Toolbar
        Toolbar toolbarSpare = findViewById(R.id.toolbarAddSparepart);
        setSupportActionBar(toolbarSpare);

        idSupplier=findViewById(R.id.txtIDSupAddSpare);
        codeSpare=findViewById(R.id.txtAddCodeSpare);
        nameSpare=findViewById(R.id.txtAddNameSpare);
        merkSpare=findViewById(R.id.txtAddTypeSpare);
        typeSpare=findViewById(R.id.txtAddTypeSpare);
        imageAddSpa=findViewById(R.id.imgAddSpare);

        //Pengambilan Data Supplier
        SharedPreferences prefSub = getApplication().getSharedPreferences("MySupplier", Context.MODE_PRIVATE);
        setIdSup= prefSub.getString("id",null);

        //Set Data ID Supplier
        idSupplier.setText(setIdSup);

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_add,menu);

        return true;
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
