package e.abimuliawans.p3l_mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText emailLogin;
    private EditText passLogin;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailLogin = (EditText)findViewById(R.id.emailLogin);
        passLogin = (EditText)findViewById(R.id.passwordLogin);
        btnLogin = (Button)findViewById(R.id.btnLogin);

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
            Toast.makeText(this, "Field can't be empty", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this,"Succeeded",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this,"Incorrect email and password",Toast.LENGTH_SHORT).show();
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
