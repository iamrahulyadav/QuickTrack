package app.quicktrack.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import app.quicktrack.R;
import app.quicktrack.models.ContactUs;
import app.quicktrack.models.LoginData;
import app.quicktrack.models.LoginResponse;
import app.quicktrack.utils.ApiResponse;
import app.quicktrack.utils.RetrofitApi;
import app.quicktrack.utils.RetrofitApiBuilder;
import app.quicktrack.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends AppCompatActivity {

    private TextView tv_title, txtCustomerCareNo, txtSupportNo, txtSupportEmail;
    private ImageView btnHome ;
    String userid;
    TinyDB tinyDB;
    Gson gson;
    LoginResponse loginData;
    private ImageView btnOption ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        setupToolBar();

//        initialize
        gson = new Gson();
        tinyDB = new TinyDB(getApplicationContext());
        loginData = new LoginResponse();

//        find id
        txtCustomerCareNo = (TextView) findViewById(R.id.contact_txtCustomer);
        txtSupportNo = (TextView) findViewById(R.id.contact_txtSupport);
        txtSupportEmail = (TextView) findViewById(R.id.contact_txtSupportMail);

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

    private void setupTitle(String title) {
        tv_title.setText(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupTitle("Contact Us") ;
    }
}
