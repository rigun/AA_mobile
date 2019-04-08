package e.abimuliawans.p3l_mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class DasboardActivity extends AppCompatActivity {

    private Animation atg, atgtwo, atgthree, fabOpen, fabClose, fabClockwise, fabAntiClockwise
            ,cardOpen,cardClose;
    private TextView pagetitle,pagesubtitle,txtFab;
    private CardView cardViewVeh,cardViewLogout;
    private ImageView imageView3,imgSparepart,imgSupplier, imgPemesanan;
    private FloatingActionButton fabMenuLainya,fabVehicle,fabLogout;
    private String token;
    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard);

        //Main Menu Button On Clicked
        imgPemesanan = findViewById(R.id.imgPesanan);
        imgPemesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DasboardActivity.this,PemesananActivity.class);
                startActivity(i);
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
        txtFab= findViewById(R.id.txtFab1);
        cardViewVeh = findViewById(R.id.cardViewFab1);
        cardViewLogout = findViewById(R.id.cardViewFab2);

        fabMenuLainya= findViewById(R.id.btnMenuLainya);
        fabVehicle= findViewById(R.id.btnMenuVehicle);
        fabLogout= findViewById(R.id.btnMenuLogOut);


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
                    isOpen=false;
                }else
                {
                    fabVehicle.startAnimation(fabOpen);
                    fabVehicle.setClickable(true);
                    cardViewVeh.startAnimation(cardOpen);

                    fabLogout.startAnimation(fabOpen);
                    fabLogout.setClickable(true);
                    cardViewLogout.startAnimation(fabOpen);
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


    }


}
