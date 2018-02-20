package app.quicktrack.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import app.quicktrack.R;
import app.quicktrack.models.LoginResponse;
import app.quicktrack.models.UserModel;
import app.quicktrack.utils.AppBaseActivity;
import app.quicktrack.utils.AppGlobal;
import app.quicktrack.utils.RetrofitApiBuilder;
import app.quicktrack.utils.Utility;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class LoginActivity extends AppBaseActivity implements View.OnClickListener{
    TextView txtForgotPass;
    EditText edtUserName, edtPass;
    String email, pass;
    int userType=1;
    AppGlobal appGlobal=AppGlobal.getInstancess();
    Context mContext;
    LoginResponse loginResponse = new LoginResponse();
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    final OkHttpClient okHttpClient = new OkHttpClient();
    RadioButton driverRadio, userRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        appGlobal.context=getApplicationContext();
//        find id

        txtForgotPass = (TextView) findViewById(R.id.txtForgot);
        Button btnLogin  = (Button) findViewById(R.id.btnLogin);
        edtUserName = (EditText) findViewById(R.id.etUsername);
        edtPass = (EditText) findViewById(R.id.etPassword);
        driverRadio = findViewById(R.id.login_radio_driver);
        userRadio = findViewById(R.id.login_radio_user);
//        set click listener
        txtForgotPass.setOnClickListener(this);
        driverRadio.setOnClickListener(this);
        userRadio.setOnClickListener(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logindata();
            }
        });
    }

    public void logindata(){
        Utility.showloadingPopup(this);
        pass = edtPass.getText().toString().trim();
        email = edtUserName.getText().toString().trim();
        if (!email.isEmpty()){
            if (!pass.isEmpty()){
                UserModel userModel = new UserModel();
                userModel.setPassword(pass);
                userModel.setUsername(email);
                userModel.setType(userType);
                Gson gson = new Gson();
                String u = gson.toJson(userModel);
                RequestBody requestBody = RequestBody.create(MEDIA_TYPE,u);
                 final Request request = new Request.Builder()
                        .url(RetrofitApiBuilder.QUICK_BASE+"login")
                        .post(requestBody)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .build();
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(10000, TimeUnit.SECONDS)
                        .writeTimeout(10000, TimeUnit.SECONDS)
                        .readTimeout(30000, TimeUnit.SECONDS)
                        .build();
                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        Utility.hidepopup();
                        String msg = e.getMessage();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utility.message(getApplicationContext(), "Connection Error");
                            }
                        });
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                        if (response!=null){
                            Utility.hidepopup();
                            String msg = response.body().string();
                            Log.d("TAG", "onResponse: "+msg);
                            Gson gson1 = new Gson();
                            loginResponse = gson1.fromJson(msg,LoginResponse.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (loginResponse.isStatus()==true){
                                        Utility.message(getApplicationContext(),"Login Success ");
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        appGlobal.setLoginData(msg);
                                    } else {
                                        Utility.message(getApplicationContext(),"Invalid username or password");
                                    }
                                }
                            });
                        }
                        Log.d("TAG", "onResponse: "+response.body().toString());
                    }
                });
            } else {
                Utility.message(getApplicationContext(), "Please enter password");
            }

        }else {
            Utility.message(getApplicationContext(), "Please enter username");
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtForgot:
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
                break;
            case R.id.login_radio_driver:
                userType = 2;
                userRadio.setChecked(false);
                break;
            case R.id.login_radio_user:
                userType = 1;
                driverRadio.setChecked(false);
                break;
        }
    }
}
