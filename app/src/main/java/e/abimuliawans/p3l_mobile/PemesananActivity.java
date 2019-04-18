package e.abimuliawans.p3l_mobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

public class PemesananActivity extends AppCompatActivity {

    private ProgressBar progressBar;

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
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
