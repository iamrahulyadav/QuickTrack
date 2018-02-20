package app.quicktrack.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import app.quicktrack.R;
import app.quicktrack.adapters.DeviceAdapter;
import app.quicktrack.models.DeviceData;
import app.quicktrack.models.DeviceListRequest;
import app.quicktrack.models.LoginResponse;
import app.quicktrack.utils.RetrofitApiBuilder;
import app.quicktrack.utils.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ListViewActivity extends AppCompatActivity implements DeviceAdapter.ClickListener{
    RecyclerView recyclerView;
    DeviceAdapter deviceAdapter;
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    final OkHttpClient okHttpClient = new OkHttpClient();
        Context mContext;
    public ArrayList<String>device = new ArrayList<>();
    TinyDB tinyDB ;
    LoginResponse loginResponse = new LoginResponse();
    Gson gson = new Gson();
    String resellerid, userId;
    LoginResponse.ResponseBean responseBean = new LoginResponse.ResponseBean();
    int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        mContext = this;
        tinyDB = new TinyDB(getApplicationContext());
        String data = tinyDB.getString("login_Data");

        loginResponse = gson.fromJson(data, LoginResponse.class);
        type = loginResponse.getResponse().getType();
        resellerid = loginResponse.getResponse().getResellerid();
        userId = loginResponse.getResponse().getUserid();
        responseBean = loginResponse.getResponse();

//        find id
        recyclerView = (RecyclerView) findViewById(R.id.recyclerCar_list);
        setupToolBar();
    }

    private TextView tv_title ;
    private ImageView btnHome ;
    private ImageView btnOption ;
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
            setupTitle("List View") ;
            deviceAdapter = new DeviceAdapter(device,mContext);
            RecyclerView.LayoutManager manager= new LinearLayoutManager(this);
            recyclerView.setLayoutManager(manager);
            deviceAdapter.setClickListene(this);
            recyclerView.setAdapter(deviceAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));
            getDeviceList();

    }

    public void getDeviceList(){
        if (device!=null){
            device.clear();
        }
        DeviceListRequest deviceListRequest = new DeviceListRequest();
        deviceListRequest.setResellerid(resellerid);
        deviceListRequest.setUserid(userId);
        deviceListRequest.setType(type);
        String data = gson.toJson(deviceListRequest);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE,data);

        final Request request = new Request.Builder()
                .url(RetrofitApiBuilder.QUICK_BASE+"devices_list")
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
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
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
            public void onResponse(Call call, Response response) throws IOException {
                Utility.hidepopup();
                if (response!=null&&response.body().toString().length()>0){
                    String msg = response.body().string();
                    Log.d("TAG", "onResponse: "+msg);
                    DeviceData deviceData = new DeviceData();
                    deviceData = gson.fromJson(msg,DeviceData.class);
                    DeviceData finalDeviceData = deviceData;

                    runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           ArrayList<String> a = new ArrayList<>();
                           for (DeviceData.ResponseBean responseBean: finalDeviceData.getResponse()){
                               a.add(responseBean.getDeviceid());
                           }
                           device.addAll(a);
                           deviceAdapter.notifyDataSetChanged();
                       }
                   });
                }
            }
        });
    }

    @Override
    public void itemClicked(View view, int postion) {
        String deviceId = device.get(postion);
        Intent intent = new Intent(ListViewActivity.this, DeviceDetailActivity.class);
        intent.putExtra("deviceId", deviceId);
        startActivity(intent);
    }
}
