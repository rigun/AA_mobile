package e.abimuliawans.p3l_mobile;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

public class PemesananActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);

        //Inisialisasi Progres Bar
        progressBar = findViewById(R.id.progress_bar_pemesanan);

        //Set Toolbar
        Toolbar toolbarPemesanan = findViewById(R.id.toolbarPemesanan);
        setSupportActionBar(toolbarPemesanan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Floating Button
        floatingActionButton=findViewById(R.id.btnAddPemesanan);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PemesananActivity.this,CabangActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
