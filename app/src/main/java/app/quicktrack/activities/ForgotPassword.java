package app.quicktrack.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import app.quicktrack.R;
import app.quicktrack.models.LoginData;
import app.quicktrack.models.LoginResponse;
import app.quicktrack.models.ProfileData;
import app.quicktrack.utils.ApiResponse;
import app.quicktrack.utils.AppBaseActivity;
import app.quicktrack.utils.RetrofitApi;
import app.quicktrack.utils.RetrofitApiBuilder;
import app.quicktrack.utils.Utility;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppBaseActivity {
    String username;
    TinyDB tinyDB;
    Gson gson;
    LoginResponse loginData;
    EditText edtEmail;
    ImageView imgBack;
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    final OkHttpClient okHttpClient = new OkHttpClient();
    TextView txtReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        loginData = new LoginResponse();
        gson = new Gson();
        tinyDB =new TinyDB(getApplicationContext());
        imgBack = (ImageView) findViewById(R.id.imgback);
        edtEmail = (EditText) findViewById(R.id.forgot_edtEmail);
        txtReset = (TextView) findViewById(R.id.forgot_txtResetPass);

        txtReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendemail();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    private void sendemail() {
        final  ForgotPassword _this = this ;
        InputMethodManager methodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(edtEmail.getWindowToken(),0);
        String email=edtEmail.getText().toString().trim();
        if(!email.isEmpty())
        {
            if(Utility.checkemail(email))
            {
                String x = "{"+'"'+"email"+'"'+":"+'"'+ email +'"'+"}";

                RequestBody requestBody = RequestBody.create(MEDIA_TYPE,Utility.stripLeadingAndTrailingQuotes(x));

                final Request request = new Request.Builder()
                        .url(RetrofitApiBuilder.QUICK_BASE+"forgetpassword")
                        .post(requestBody)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .build();
                okHttpClient.retryOnConnectionFailure();
                Utility.showloadingPopup(this);
                okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String msg = e.getMessage();
                                Utility.message(getApplicationContext(), "Connection error ");
                                Utility.hidepopup();
                            }
                        });
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                        Utility.hidepopup();
                        if (response!=null&&response.body().toString().length()>0){
                            String msg = response.body().string();
                            Log.d("TAG", "onResponse: "+msg);
                            try {
                                JSONObject jsonObject = new JSONObject(msg);
                                if (jsonObject.getString("status").equals("true")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Utility.message(getApplicationContext(), "Password sent your email");
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Utility.message(getApplicationContext(), "Email id not registered");
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                });
            }
            else
            {
                Utility.message(this,"Please enter valid Email");
            }
        }
        else
        {
            Utility.message(this,"Please enter email");
        }

    }

}
