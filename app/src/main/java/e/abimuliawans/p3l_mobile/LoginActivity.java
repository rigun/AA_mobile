package e.abimuliawans.p3l_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private ImageView imageViewLogin;
    private Animation smallToBing,bbta,bbta2;
    private TextView title,subTitle;
    private EditText txtEmail,txtPass;
    private Button btnLogin;
    private ProgressDialog dialog;
    private String token,testMerk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Load Animation Image
        smallToBing= AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        bbta= AnimationUtils.loadAnimation(this, R.anim.bbta);
        bbta2= AnimationUtils.loadAnimation(this, R.anim.bbta2);

        imageViewLogin=(ImageView)findViewById(R.id.imageviewLogin);
        title=(TextView)findViewById(R.id.title);
        subTitle=(TextView)findViewById(R.id.subTitle);
        txtEmail=(EditText)findViewById(R.id.textEmail);
        txtPass=(EditText)findViewById(R.id.textPass);
        btnLogin=(Button)findViewById(R.id.btnLogin);

        //Passing Animation and Start
        imageViewLogin.startAnimation(smallToBing);
        title.startAnimation(bbta);
        subTitle.startAnimation(bbta);
        txtEmail.startAnimation(bbta2);
        txtPass.startAnimation(bbta2);
        btnLogin.startAnimation(bbta2);

        //Intent
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLogin();
            }
        });

    }

    public void requestLogin(){
        if(txtEmail.getText().toString().isEmpty()|| txtPass.getText().toString().isEmpty())
        {
            Toasty.error(LoginActivity.this, "Field can't be empty",
                    Toast.LENGTH_SHORT, true).show();
        }
        else{

            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setTitle("Please Wait");
            dialog.setMessage("Loading..");
            dialog.show();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit.Builder builder=new Retrofit.
                    Builder().baseUrl("http://10.53.15.204/auth/").
                    addConverterFactory(GsonConverterFactory.create(gson));
            Retrofit retrofit=builder.build();
            ApiClient apiClient=retrofit.create(ApiClient.class);

            Call<LoginResponse> userDAOCall=apiClient.loginRequest(txtEmail.getText().toString(),txtPass.getText().toString());

            userDAOCall.enqueue(new Callback<LoginResponse>()
            {
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response)
                {
                    if(response.isSuccessful())
                    {
                        dialog.dismiss();
                        token = response.body().getToken();

                        //Save Token and URL
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyToken", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("token_access", token);
                        editor.putString("BASE_URL","http://10.53.15.204/");
                        editor.commit();

                        Toasty.success(LoginActivity.this, "Login Success",
                                Toast.LENGTH_SHORT, true).show();
                        Intent i = new Intent(LoginActivity.this,DasboardActivity.class);
                        startActivity(i);
                    }
                    else{
                        dialog.dismiss();
                        Toasty.error(LoginActivity.this, "Incorrect email and password",
                                Toast.LENGTH_SHORT, true).show();
                    }
                }

                public void onFailure(Call<LoginResponse> call, Throwable t)
                {
                    dialog.dismiss();
                    Toasty.error(LoginActivity.this, "Network Not Connected",
                            Toast.LENGTH_SHORT, true).show();
                }
            });
        }
    }
}
