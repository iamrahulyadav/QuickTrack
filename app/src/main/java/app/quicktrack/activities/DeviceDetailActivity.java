package app.quicktrack.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import app.quicktrack.R;
import app.quicktrack.models.Data;
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

import static app.quicktrack.utils.Utility.bitmapDescriptorFromVectorG;
import static app.quicktrack.utils.Utility.bitmapDescriptorFromVectorY;
import static app.quicktrack.utils.Utility.getAddress;

public class DeviceDetailActivity extends AppBaseActivity implements OnMapReadyCallback {
    public String deviceId, username, pass;
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
        Data data1 = new Data();
        data1.setDeviceId(deviceId);

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



//.................................. start service .....................................//
        startService(intent);
        registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        stopService(intent);
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

    Gson gson = new Gson();
    String address;
    public void setma(DeviceDetails deviceData){

        if (deviceData.getMessage().equals("success")){
            if (mMap!=null){
                mMap.clear();
            }
            for (DeviceDetails.ResponseBean responseBean: deviceData.getResponse()){
                String type = responseBean.getType();
                int red=0, yellow=0, green=0;
                switch (type){
                    case "Car":
                        red = R.drawable.marker_r;
                        yellow = R.drawable.marker_y;
                        green = R.drawable.marker_g;
                        break;
                    case "Humane":
                        red = R.drawable.marker_r;
                        yellow = R.drawable.marker_y;
                        green = R.drawable.marker_g;
                        break;
                    case "Bike":
                        red = R.drawable.marker_r;
                        yellow = R.drawable.marker_y;
                        green = R.drawable.marker_g;
                        break;
                    case "Bus":
                        red = R.drawable.marker_r;
                        yellow = R.drawable.marker_y;
                        green = R.drawable.marker_g;
                        break;
                    case "Truck":
                        red = R.drawable.marker_r;
                        yellow = R.drawable.marker_y;
                        green = R.drawable.marker_g;
                        break;
                }

                String lat = responseBean.getLatitude();
                String lang = responseBean.getLongitude();
                 address = responseBean.getAddress();
                Log.d("TAG", "getInfoContents: "+address);

                if (address==null){
                     Geocoder geocoder;
                     List<Address> addresses = null;
                     geocoder = new Geocoder(mContext, Locale.getDefault());
                     try {
                         addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lang), 1);
                         // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                         address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                         Log.d("TAG", "getInfoContents: "+address);

                     } catch (IOException e) {
                         e.printStackTrace();
                     }

                 }
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
                                    .icon(Utility.bitmapDescriptorFromVectorR(DeviceDetailActivity.this)));
                            markerArrayList.add(marker);
                        } else if (speedR<10){
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(address+"|"+date+"|"
                                    +speedR+"|"+lat+"|"+lang+"|"+name)
                                    .icon(bitmapDescriptorFromVectorY(DeviceDetailActivity.this)));
                            markerArrayList.add(marker);
                        } else {
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(address+"|"+date+"|"
                                    +speedR+"|"+lat+"|"+lang+"|"+name)
                                    .icon(bitmapDescriptorFromVectorG(DeviceDetailActivity.this)));
                            markerArrayList.add(marker);
                        }
                        builder = new LatLngBounds.Builder();
                        for (Marker m : markerArrayList) {
                            builder.include(m.getPosition());
                        }
//                        CameraPosition cameraPosition = new CameraPosition.Builder()
//                                .target(new LatLng(Double.parseDouble(lat), Double.parseDouble(lang))).zoom(15f).tilt(60).build();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lang)), 15.0f));

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
                        }
                        else {

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
        } else {
            Utility.message(getApplicationContext(), "No data found");
        }
    }

    //.................................. call broadcast  .....................................//


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };


    private void updateUI(Intent intent1) {
        String counter = intent1.getStringExtra("counter");
        Utility.hidepopup();
        try {
            JSONObject jsonObject = new JSONObject(counter);
            if (jsonObject.has("status")){
                if (jsonObject.getString("status").equals("true")){
                    Log.d(TAG, "updateUI: "+counter);
                    deviceData = gson.fromJson(counter, DeviceDetails.class);
                    setma(deviceData);
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
    }

}
