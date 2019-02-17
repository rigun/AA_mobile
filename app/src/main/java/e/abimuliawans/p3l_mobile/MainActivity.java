package e.abimuliawans.p3l_mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shashank.sony.fancytoastlib.FancyToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout layout;
    private EditText emailLogin;
    private EditText passLogin;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (RelativeLayout)findViewById(R.id.relativeLayout);
        emailLogin = (EditText)findViewById(R.id.emailLogin);
        passLogin = (EditText)findViewById(R.id.passwordLogin);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager= (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLogin();
            }
        });
    }

    public void requestLogin(){
        if(emailLogin.getText().toString().isEmpty()|| passLogin.getText().toString().isEmpty())
        {
            FancyToast.makeText(MainActivity.this,"Field can't be empty",
                    FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
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

            Call<EmployeeData> userDAOCall=apiClient.loginRequest(emailLogin.getText().toString(),passLogin.getText().toString());

            userDAOCall.enqueue(new Callback<EmployeeData>()
            {
                public void onResponse(Call<EmployeeData> call, Response<EmployeeData> response)
                {
                    if(response.isSuccessful())
                    {
                        FancyToast.makeText(MainActivity.this,"Succeeded",
                                FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();

                        Intent i = new Intent(MainActivity.this,MainAdminActivity.class);
                        startActivity(i);
                    }
                    else{
                        FancyToast.makeText(MainActivity.this,"Incorrect email and password",
                                FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                    }
                }

                public void onFailure(Call<EmployeeData> call, Throwable t)
                {
                    Log.d("TAG", t.toString());
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
