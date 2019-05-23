package e.abimuliawans.p3l_mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DasboardKonsumenActivity extends AppCompatActivity {

    private Animation atg, atgtwo, atgthree;
    private TextView pagetitle,pagesubtitle,txtFab;
    private ImageView imageView3,imgSparepart,imgCabang, imgHistory,imgAboutUs;
    private String token,BASE_URL;
    private EditText txtNoTelponKonsumen,txtNoPlat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard_konsumen);

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
    }
}
