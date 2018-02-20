package app.quicktrack.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

import app.quicktrack.R;
import app.quicktrack.models.LoginResponse;
import app.quicktrack.models.ProfileData;
import app.quicktrack.utils.RetrofitApiBuilder;
import app.quicktrack.utils.Utility;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MyProfileActivity extends AppCompatActivity {
    private TextView tv_title , txtName, txtEmail, txtNumber, txtSMS, txtCompanyName;
    private ImageView btnHome , imgProfile;
    private ImageView btnOption ;
    TinyDB tinyDB;
    LoginResponse loginData;
    ProfileData profileData = new ProfileData();
    Gson gson;
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    final OkHttpClient okHttpClient = new OkHttpClient();
    String userId,type, resellerid;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        setupToolBar();
//        initialize
        loginData = new LoginResponse();
        mContext = this;
        tinyDB = new TinyDB(mContext);
        gson = new Gson();


//        find id
        txtName = (TextView) findViewById(R.id.my_profile_txtName);
        txtCompanyName = (TextView) findViewById(R.id.my_profile_txtCompanyName);
        txtSMS = (TextView) findViewById(R.id.my_profile_txtSms);
        txtEmail = (TextView) findViewById(R.id.my_profile_txtEmail);
        txtNumber = (TextView) findViewById(R.id.my_profile_txtNumber);
        imgProfile = (ImageView) findViewById(R.id.img_profile);

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
        btnOption.setVisibility(View.VISIBLE);
        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = gson.toJson(profileData);
                startActivity(new Intent(MyProfileActivity.this, EditProfileActivity.class).putExtra("profile_data",data));

            }
        });
        setSupportActionBar(myToolbar);
    }

    private void setupTitle(String title) {
        tv_title.setText(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupTitle("Profile") ;
        String login = tinyDB.getString("login_Data");
        loginData = gson.fromJson(login, LoginResponse.class);
        userId = loginData.getResponse().getUserid();
        resellerid = loginData.getResponse().getResellerid();
        type = String.valueOf(loginData.getResponse().getType());
        getProfile();
    }
    String request="";
    /*{"resellerid":"test123"userid":"test123"type":"2"}*/
    public void getProfile(){
        Utility.showloadingPopup(this);
        /*{
"resellerid":"sysadmin",
"userid":"test123",
"type":2
}*/
        request= "{"+'"'+"resellerid"+'"'+":"+'"'+ resellerid +'"'+","+'"'+"userid"+'"'+":"+'"'+ userId
                +'"'+","+'"'+"type"+'"'+":"+'"'+ type +'"'+"}";

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE,Utility.stripLeadingAndTrailingQuotes(request));

        final Request request = new Request.Builder()
                .url(RetrofitApiBuilder.QUICK_BASE+"profile")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.SECONDS)
                .writeTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(30000, TimeUnit.SECONDS)
                .build();

        Utility.showloadingPopup(this);
        client.newCall(request).enqueue(new okhttp3.Callback() {
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
                    try {
                        JSONObject jsonObject = new JSONObject(msg);
                        if (jsonObject.getString("status").equals("true")){
                            profileData = gson.fromJson(msg,ProfileData.class);
                            ProfileData finalProfileData = profileData;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txtCompanyName.setText("QuickTrack Solutions");
                                    txtEmail.setText(finalProfileData.getResponse().getContactEmail());
                                    txtNumber.setText(finalProfileData.getResponse().getContactPhone());
                                    txtName.setText(finalProfileData.getResponse().getUserID());
                                    String a = RetrofitApiBuilder.IMG_BASE_URL+finalProfileData.getResponse().getUser_profile_pic();
                                    if (finalProfileData.getResponse().getUser_profile_pic()!=null
                                            && finalProfileData.getResponse().getUser_profile_pic().length()>0){

                                        GetImage task = new GetImage();
                                        // Execute the task
                                        task.execute(new String[] { a });

                                    } else {
                                        Glide.with(mContext).load(R.drawable.gfgf)
                                                .apply(RequestOptions.circleCropTransform()).into(imgProfile);
                                    }
                                }
                            });

                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utility.message(getApplicationContext(), "Connection Error");
                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("TAG", "onResponse: "+msg);

                }
            }

        });
    }

    public class GetImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showloadingPopup(MyProfileActivity.this);
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            Utility.hidepopup();
            imgProfile.setImageBitmap(result);

            Glide.with(getApplicationContext()).load(result)
                    .apply(RequestOptions.circleCropTransform()).into(imgProfile);

        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                if (url!=null){
                    stream = getHttpConnection(url);
                    bitmap = BitmapFactory.
                            decodeStream(stream, null, bmOptions);
                    stream.close();
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }}
