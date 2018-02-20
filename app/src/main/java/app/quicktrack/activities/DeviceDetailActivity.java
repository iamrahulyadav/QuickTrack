package app.quicktrack.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.mukesh.tinydb.TinyDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import app.quicktrack.R;
import app.quicktrack.models.DeviceDetails;
import app.quicktrack.models.DeviceMapRequest;
import app.quicktrack.utils.AppBaseActivity;
import app.quicktrack.utils.BroadcastService;
import app.quicktrack.utils.RetrofitApiBuilder;
import app.quicktrack.utils.Utility;
import in.technobuff.helper.utils.PermissionRequestHandler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeviceDetailActivity extends AppBaseActivity implements OnMapReadyCallback {
    public static String deviceId, username, pass;
    TinyDB tinyDB ;
    private GoogleMap mMap;
    private static final String TAG = "ServiceMap";
    private Intent intent;
    LatLngBounds.Builder builder;
    Activity mContext;
    RadioGroup rgViews;
    DeviceDetails deviceData = new DeviceDetails();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        setupToolBar();
        mContext = this;
        rgViews = (RadioGroup) findViewById(R.id.rg_views);

        intent = new Intent(this, BroadcastService.class);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        deviceId = getIntent().getStringExtra("deviceId");
        tinyDB = new TinyDB(getApplicationContext());

    }

    Marker marker;
    ArrayList<LatLng>latLngArrayList = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        setupTitle("Device "+deviceId) ;
        Utility.showloadingPopup(mContext);

        if (PermissionRequestHandler.requestPermissionToLocation(DeviceDetailActivity.this, null)) {
            checkGPSStatus();
        }
        setUpMap();
/*        startService(intent);
        registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.BROADCAST_ACTION));*/
    }

    @Override
    public void onPause() {
        super.onPause();
       /* unregisterReceiver(broadcastReceiver);
        stopService(intent);*/
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

    ArrayList<Marker> markerArrayList = new ArrayList<>();

    public static final MediaType MEDIA_TYPE = MediaType.parse("application/xml");
    OkHttpClient okHttpClient = new OkHttpClient();
    public void getDeviceDetails(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Utility.showloadingPopup(DeviceDetailActivity.this);
                DeviceMapRequest deviceMapRequest = new DeviceMapRequest();
                deviceMapRequest.setDeviceid(deviceId);
                deviceMapRequest.setLimit(1);
                Gson gson = new Gson();
                String data = gson.toJson(deviceMapRequest);

                RequestBody requestBody = RequestBody.create(MEDIA_TYPE,data);
                final Request request = new Request.Builder()
                        .url(RetrofitApiBuilder.QUICK_BASE+"mapdata")
                        .post(requestBody)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String msg = e.getMessage();
                                Utility.hidepopup();
                                Log.d("TAG", "onFailure: "+msg);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Utility.hidepopup();
                        String msg = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                deviceData = gson.fromJson(msg, DeviceDetails.class);
                                if (deviceData.isStatus()==true){
                                    setma();
                                } else {
                                    Utility.message(mContext, "No data found");

                                }
                            }
                        });
                    }
                });
            }
        });

    }

    private void setUpMap() {

        new Thread(new Runnable() {
            public void run() {
                getDeviceDetails();
            }
        }).start();
    }

    Gson gson = new Gson();

    public void setma(){

        if (deviceData.getMessage().equals("success")){
            for (DeviceDetails.ResponseBean responseBean: deviceData.getResponse()){
                String type = responseBean.getType();
                int red=0, yellow=0, green=0;
                switch (type){
                    case "Car":
                        red = R.drawable.car_r;
                        yellow = R.drawable.car_y;
                        green = R.drawable.car_g;
                        break;
                    case "Humane":
                        red = R.drawable.human_r;
                        yellow = R.drawable.human_y;
                        green = R.drawable.human_g;
                        break;
                    case "Bike":
                        red = R.drawable.bike_r;
                        yellow = R.drawable.bike_b;
                        green = R.drawable.bike_g;
                        break;
                    case "Bus":
                        red = R.drawable.bus_r;
                        yellow = R.drawable.bus_y;
                        green = R.drawable.bus_g;
                        break;
                    case "Truck":
                        red = R.drawable.truck_r;
                        yellow = R.drawable.truck_y;
                        green = R.drawable.truck_g;
                        break;
                }

                String lat = responseBean.getLatitude();
                String lang = responseBean.getLongitude();
                String address = responseBean.getAddress();
                String date = responseBean.getDatetime();
                String speed = responseBean.getSpeed();
                double speedR = Utility.getSpeedDouble(Double.parseDouble(speed));
                String name = responseBean.getDeviceid();
                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));
                latLngArrayList.add(latLng);
                int finalRed = red;
                int finalYellow = yellow;
                int finalGreen = green;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (speedR ==0){

                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(address+"|"+date+"|"
                                    +speedR+"|"+lat+"|"+lang+"|"+name)
                                    .icon(BitmapDescriptorFactory.fromResource(finalRed)));
                            markerArrayList.add(marker);
                        } else if (speedR<10){
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(address+"|"+date+"|"
                                    +speedR+"|"+lat+"|"+lang+"|"+name)
                                    .icon(BitmapDescriptorFactory.fromResource(finalYellow)));
                            markerArrayList.add(marker);
                        } else {
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(address+"|"+date+"|"
                                    +speedR+"|"+lat+"|"+lang+"|"+name)
                                    .icon(BitmapDescriptorFactory.fromResource(finalGreen)));
                            markerArrayList.add(marker);
                        }
                        builder = new LatLngBounds.Builder();
                        for (Marker m : markerArrayList) {
                            builder.include(m.getPosition());
                        }
                        LatLngBounds bounds = builder.build();
                  //      mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 70));

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lang)), 12.0f));

                        ArrayList<LatLng> points = new ArrayList<LatLng>();
                        PolylineOptions lineOptions = new PolylineOptions();
                        for (int i =0; i<latLngArrayList.size();i++){
                            points.add(latLngArrayList.get(i));
                        }
                        //positions are the latlng;
                        lineOptions.addAll(latLngArrayList);
                        lineOptions.width(8);
                        lineOptions.color(Color.RED);
                        mMap.addPolyline(lineOptions);
                    }
                });
            }
        } else {
            Utility.message(getApplicationContext(), "No data found");
        }
    }

   /* private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };*/

    private void updateUI(Intent intent1) {
        String counter = intent1.getStringExtra("counter");
        Utility.hidepopup();
        try {
            JSONObject jsonObject = new JSONObject(counter);
            if (jsonObject.has("status")){
                if (jsonObject.getString("status").equals("true")){
                    deviceData = gson.fromJson(counter, DeviceDetails.class);
                    setma();
                } else {
                    Utility.message(mContext, "No data found");
                    mContext.stopService(intent);
                }
            } else {
                Utility.message(mContext, "Connection error");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMap != null) {
            return;
        }
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        rgViews.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_normal){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }else if(checkedId == R.id.rb_satellite){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
            }
        });
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = layoutInflater.inflate(R.layout.marker_view,null);
                TextView txtName, txtGps, txtSpeed, txtAddress, txtDate;
                txtName = (TextView) view.findViewById(R.id.txtDeviceName);
                txtGps = (TextView) view.findViewById(R.id.txtGPS);
                txtSpeed = (TextView) view.findViewById(R.id.txtSpeed);
                txtAddress = (TextView) view.findViewById(R.id.txtAddress);
                txtDate = (TextView) view.findViewById(R.id.txtDate);
                String Title=marker.getTitle();
                String[] value_split = Title.split("\\|");

                long timestamp= Long.parseLong(value_split[1]);
                String date = Utility.getTime(timestamp);
                txtDate.setText("Date/Time: "+date);

                double latitude = Double.parseDouble(value_split[3]);
                double longitude = Double.parseDouble(value_split[4]);

                String address = Utility.getAddress(mContext, latitude, longitude);
                if (value_split[0].length()>0){
                    txtAddress.setText("Address: "+value_split[0]);
                } else {
                    txtAddress.setText("Address: "+address);

                }

                latitude = Utility.getDouble(latitude);
                longitude = Utility.getDouble(longitude);
                txtGps.setText("Lat/Long: "+String.valueOf(latitude)+" , "+ String.valueOf(longitude));
                txtSpeed.setText("Speed: "+value_split[2]+" kmph");
                txtName.setText(value_split[5]);
                return view;
            }
        });
    }

}
