package e.abimuliawans.p3l_mobile;

import android.content.Intent;
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
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit.Builder builder=new Retrofit.
                    Builder().baseUrl("https://api1.thekingcorp.org/auth/").
                    addConverterFactory(GsonConverterFactory.create(gson));
            Retrofit retrofit=builder.build();
            ApiClient apiClient=retrofit.create(ApiClient.class);

            Call<EmployeeData> userDAOCall=apiClient.loginRequest(txtEmail.getText().toString(),txtPass.getText().toString());

            userDAOCall.enqueue(new Callback<EmployeeData>()
            {
                public void onResponse(Call<EmployeeData> call, Response<EmployeeData> response)
                {
                    if(response.isSuccessful())
                    {
                        Toasty.success(LoginActivity.this, "Login Success",
                                Toast.LENGTH_SHORT, true).show();

                        Intent i = new Intent(LoginActivity.this,MainAdminActivity.class);
                        startActivity(i);
                    }
                    else{
                        Toasty.error(LoginActivity.this, "Incorrect email and password",
                                Toast.LENGTH_SHORT, true).show();
                    }
                }

                public void onFailure(Call<EmployeeData> call, Throwable t)
                {
                    Log.d("TAG", t.toString());
                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
