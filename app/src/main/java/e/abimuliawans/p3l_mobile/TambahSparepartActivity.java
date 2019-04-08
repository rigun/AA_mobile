package e.abimuliawans.p3l_mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class TambahSparepartActivity extends AppCompatActivity {
    private TextView idSupplier;
    private String setIdSup;
    private EditText codeSpare,nameSpare,merkSpare,typeSpare;

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

        //Pengambilan Data Supplier
        SharedPreferences prefSub = getApplication().getSharedPreferences("MySupplier", Context.MODE_PRIVATE);
        setIdSup= prefSub.getString("id",null);

        //Set Data ID Supplier
        idSupplier.setText(setIdSup);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_add,menu);

        return true;
    }
}
