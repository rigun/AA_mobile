package e.abimuliawans.p3l_mobile;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class DasboardActivity extends AppCompatActivity {

    private Animation atg, atgtwo, atgthree, fabOpen, fabClose, fabClockwise, fabAntiClockwise;
    private TextView pagetitle,pagesubtitle;
    private ImageView imageView3,imgSparepart,imgSupplier;
    private FloatingActionButton fabMenuLainya,fabVehicle;
    private String token;
    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        token=(String)bundle.get("token");


        //Main Menu Button On Clicked
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
                Intent i = new Intent(DasboardActivity.this, SupplierActivity.class);
                i.putExtra("token",token);
                startActivity(i);
            }
        });

        atg = AnimationUtils.loadAnimation(DasboardActivity.this, R.anim.atg);
        atgtwo = AnimationUtils.loadAnimation(DasboardActivity.this, R.anim.atgtwo);
        atgthree = AnimationUtils.loadAnimation(DasboardActivity.this, R.anim.atgthree);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        fabClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        fabAntiClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);

        pagetitle = findViewById(R.id.pagetitle);
        pagesubtitle = findViewById(R.id.pagesubtitle);
        imageView3 = findViewById(R.id.imageView3);

        fabMenuLainya= findViewById(R.id.btnMenuLainya);
        fabVehicle= findViewById(R.id.btnMenuVehicle);


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
                    isOpen=false;
                }else
                {
                    fabVehicle.startAnimation(fabOpen);
                    fabVehicle.setClickable(true);
                    isOpen=true;
                }
            }
        });

        fabVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DasboardActivity.this,VehicleActivity.class);
                intent.putExtra("token",token);
                startActivity(intent);
            }
        });
    }
}
