package app.quicktrack.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import app.quicktrack.R;
import app.quicktrack.adapters.DeviceAdapter;
import app.quicktrack.models.DeviceData;
import app.quicktrack.models.DeviceListRequest;
import app.quicktrack.models.LoginResponse;
import app.quicktrack.utils.AppBaseActivity;
import app.quicktrack.utils.RetrofitApiBuilder;
import app.quicktrack.utils.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TrackHistoryActivity extends AppBaseActivity implements DeviceAdapter.ClickListener{
    EditText et_search;
    RecyclerView recyclerView;
    DeviceAdapter deviceAdapter;
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    final OkHttpClient okHttpClient = new OkHttpClient();
    Context mContext;
    public ArrayList<String>device = new ArrayList<>();
    private List<DeviceData.ResponseBean> devicelist ;

    TinyDB tinyDB ;
    LoginResponse loginResponse = new LoginResponse();
    Gson gson = new Gson();
    String resellerid, userId;
    LoginResponse.ResponseBean responseBean = new LoginResponse.ResponseBean();
    int type;
    ImageView imgDate;
    String selectedDate;
    EditText edtDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_history);
        et_search= (EditText) findViewById(R.id.et_search);
      //  et_search.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        setupToolBar();


        mContext = this;
        devicelist = new ArrayList<>();
        tinyDB = new TinyDB(getApplicationContext());
        String data = tinyDB.getString("login_Data");
        imgDate = (ImageView) findViewById(R.id.imgCalender);
        edtDate = (EditText) findViewById(R.id.et_search);
        loginResponse = gson.fromJson(data, LoginResponse.class);
        type = loginResponse.getResponse().getType();
        resellerid = loginResponse.getResponse().getResellerid();
        userId = loginResponse.getResponse().getUserid();
        responseBean = loginResponse.getResponse();


//        find id
        recyclerView = (RecyclerView) findViewById(R.id.recyclerCar_list);

        imgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TrackHistoryActivity.this, date, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


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
        setupTitle("Track History") ;
        deviceAdapter = new DeviceAdapter(devicelist,mContext);
        RecyclerView.LayoutManager manager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        deviceAdapter.setClickListene(this);
        recyclerView.setAdapter(deviceAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));
        getDeviceList();

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        selectedDate = df.format(c.getTime());
        edtDate.setText(selectedDate);
        updateLabel();
    }

    private DeviceData deviceData = new DeviceData();

    private void getDeviceList(){
        if (devicelist!=null){
            devicelist.clear();
        }

        DeviceListRequest deviceListRequest = new DeviceListRequest();
        deviceListRequest.setResellerid(resellerid);
        deviceListRequest.setType(type);
        String data = gson.toJson(responseBean);
        Log.d("TAG", "rakhi: "+data);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE,data);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.SECONDS)
                .writeTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(30000, TimeUnit.SECONDS)
                .build();

        final Request request = new Request.Builder()
                .url(RetrofitApiBuilder.QUICK_BASE+"devices_list")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
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
                if (response!=null){
                    String msg = response.body().string();

                    deviceData = gson.fromJson(msg,DeviceData.class);
                    DeviceData finalDeviceData = deviceData;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<String> a = new ArrayList<>();
                            if (deviceData.isStatus()){
                                devicelist.addAll(deviceData.getResponse());
                                deviceAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });
    }

    Calendar mCalendar = Calendar.getInstance();
    int year, monthOfYear, dayOfMonth;

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        selectedDate = sdf.format(mCalendar.getTime());
        edtDate.setText(selectedDate);

    }


    @Override
    public void itemClicked(View view, int postion) {
        String deviceId = devicelist.get(postion).getDeviceid();
        Intent intent = new Intent(TrackHistoryActivity.this, HistoryDetailsActivity.class);
        intent.putExtra("deviceId", deviceId);
        intent.putExtra("selectedDate",selectedDate);
        startActivity(intent);
    }




}
