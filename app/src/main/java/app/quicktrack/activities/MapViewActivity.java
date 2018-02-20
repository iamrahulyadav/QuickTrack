package app.quicktrack.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import app.quicktrack.R;
import app.quicktrack.models.DeviceData;
import app.quicktrack.models.DeviceDetails;
import app.quicktrack.models.DeviceListRequest;
import app.quicktrack.models.DeviceMapRequest;
import app.quicktrack.models.LoginResponse;
import app.quicktrack.utils.AppBaseActivity;
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

public class MapViewActivity extends AppBaseActivity implements OnMapReadyCallback {

    public ArrayList<String>device = new ArrayList<>();
    TinyDB tinyDB ;
    ArrayAdapter arrayAdapter;
    Spinner spinner;
    String deviceId;
    private GoogleMap mMap;
    RadioGroup rgViews;
    LatLngBounds.Builder builder;
    Activity mContext;
    DeviceDetails deviceData;
    LoginResponse loginResponse = new LoginResponse();
    String resellerid, userId;
    LoginResponse.ResponseBean responseBean = new LoginResponse.ResponseBean();
    int type;
    ArrayList<LatLng>latLngArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        setupToolBar();
        tinyDB = new TinyDB(getApplicationContext());
        mContext = this;
        String data = tinyDB.getString("login_Data");

        loginResponse = gson.fromJson(data, LoginResponse.class);
        type = loginResponse.getResponse().getType();
        resellerid = loginResponse.getResponse().getResellerid();
        userId = loginResponse.getResponse().getUserid();
        responseBean = loginResponse.getResponse();
        spinner = (Spinner) findViewById(R.id.device_spinner);
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,device);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(arrayAdapter);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        rgViews = (RadioGroup) findViewById(R.id.rg_views);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                deviceId = arrayAdapter.getItem(i).toString();
                if (deviceId!=null){
                    setUpMap();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
        setupTitle("Map View") ;
        if (PermissionRequestHandler.requestPermissionToLocation(MapViewActivity.this, null)) {
            checkGPSStatus();
        }
        getDeviceList();
    }


    public static final MediaType MEDIA_TYPE = MediaType.parse("application/xml");
    OkHttpClient okHttpClient = new OkHttpClient();

    ArrayList<Marker> markerArrayList = new ArrayList<>();

    public void getDeviceDetails(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mMap!=null){
                    mMap.clear();
                }
            }
        });

        deviceData = new DeviceDetails();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Utility.showloadingPopup(MapViewActivity.this);
                DeviceMapRequest deviceMapRequest = new DeviceMapRequest();
                deviceMapRequest.setDeviceid(deviceId);
                deviceMapRequest.setLimit(1);
                String data = gson.toJson(deviceMapRequest);

                RequestBody requestBody = RequestBody.create(MEDIA_TYPE,data);
                final Request request = new Request.Builder()
                        .url(RetrofitApiBuilder.QUICK_BASE+"mapdata")
                        .post(requestBody)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .build();
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(10000, TimeUnit.SECONDS)
                        .writeTimeout(10000, TimeUnit.SECONDS)
                        .readTimeout(30000, TimeUnit.SECONDS)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               String msg = e.getMessage();
                               Utility.hidepopup();
                               Log.d("TAG", "onFailure: "+msg);
                               Utility.message(getApplicationContext(), "Connection error");
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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    Marker marker;

    public void setma(){

        if (mMap!=null){
            mMap.clear();
        }
        if (deviceData.getMessage().equals("success")){
            deviceData.getResponse().size();
            for (DeviceDetails.ResponseBean responseBean: deviceData.getResponse()){
                String conten = responseBean.getAddress();
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
                double speedR = round(Double.parseDouble(speed),3);
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
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

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

    private void setUpMap() {
        // Retrieve the city data from the web service
        // In a worker thread since it's a network operation.
        new Thread(new Runnable() {
            public void run() {

                getDeviceDetails();
            }
        }).start();
    }

    Gson gson = new Gson();

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


        Utility.showloadingPopup(this);
        okHttpClient.newCall(request).enqueue(new Callback() {
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
                            arrayAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMap != null) {

            return;
        }
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

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
