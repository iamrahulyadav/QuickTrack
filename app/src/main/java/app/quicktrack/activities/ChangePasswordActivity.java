package app.quicktrack.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import app.quicktrack.R;
import app.quicktrack.models.LoginData;
import app.quicktrack.utils.ApiResponse;
import app.quicktrack.utils.RetrofitApi;
import app.quicktrack.utils.RetrofitApiBuilder;
import app.quicktrack.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private TextView tv_title , txtUpdate;
    private ImageView btnHome ;
    private ImageView btnOption ;
    String userID;
    LoginData loginData = new LoginData();
    TinyDB tinyDB;
    Gson gson = new Gson();
    EditText edtNewPaasword, edtoldPass, edtConfirmNewPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setupToolBar();
        tinyDB= new TinyDB(getApplicationContext());
        String login = tinyDB.getString("loginData");
        loginData = gson.fromJson(login, LoginData.class);
        userID = loginData.getUser_detail().getUser_id();
//        find id
        edtoldPass = (EditText) findViewById(R.id.change_password_edtCurrentPass);
        edtNewPaasword = (EditText) findViewById(R.id.change_password_edtNewPass);
        edtConfirmNewPass = (EditText) findViewById(R.id.change_password_edtConfirmNewPass);
        txtUpdate = (TextView) findViewById(R.id.change_password_btnUpdatePass);


        txtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPass = edtoldPass.getText().toString().trim() ;
                String newPass =  edtNewPaasword.getText().toString().trim() ;
                String newConfirmPass = edtConfirmNewPass.getText().toString().trim() ;
                if(!currentPass.isEmpty() && !newPass.isEmpty() && !newConfirmPass.isEmpty() && newPass.equals(newConfirmPass))
                {
                    changePassword(currentPass,newPass, userID);
                    Snackbar.make(view, "Saved Successfully", Snackbar.LENGTH_LONG).setAction("Finish",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            }).show();
                }
                else {
                    edtoldPass.setError("Check your Current password ");
                    edtNewPaasword.setError("Check your Confirm password ");
                    edtConfirmNewPass.setError("Check your New password ");

                }
            }
        });

    }

    private void setupToolBar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_title = (TextView) myToolbar.findViewById(R.id.tv_title);
        btnHome = (ImageView) myToolbar.findViewById(R.id.btnHome);
        btnOption = (ImageView) myToolbar.findViewById(R.id.btnOption);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onBackPressed(); }
        });
        btnOption.setVisibility(View.INVISIBLE);
        setSupportActionBar(myToolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupTitle("Change Password") ;
    }

    public void changePassword(String oldpass, String  newpass, String userId){
       /* RetrofitApi retroFitApis = RetrofitApiBuilder.getRetrofitApiBuilder() ;
        Call<ApiResponse> responseCall = retroFitApis.change_pass(oldpass,newpass,userId) ;
        responseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Utility.message(getApplicationContext(), response.body().msg);
                finish();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Utility.message(getApplicationContext(),"Connection Error");
            }
        });*/
    }

    private void setupTitle(String title) {
        tv_title.setText(title);
    }

}
