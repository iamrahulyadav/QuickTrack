package app.quicktrack.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import app.quicktrack.models.Data;
import app.quicktrack.models.DeviceData;
import app.quicktrack.models.DeviceDetails;
import app.quicktrack.models.DeviceListRequest;
import app.quicktrack.models.DeviceMapRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ask on 1/2/2018.
 */

public class BroadcastService extends Service {
    private static final String TAG = "BroadcastService";
    public static final String BROADCAST_ACTION = "app.quicktrack.utils.displayevent";
    private final Handler handler = new Handler();
    Intent intent;
    int startId;
    public static int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        this.startId=startId;
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 2 second

    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
//            DisplayLoggingInfo();

            getDeviceList();
            handler.postDelayed(this, 5000); // 10 seconds
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
    }

    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    Gson gson = new Gson();
    String deviceId;
    public void getDeviceList(){
        DeviceMapRequest deviceMapRequest = new DeviceMapRequest();
        deviceId = Data.getDeviceId();
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
                String msg = e.getMessage();

//                Utility.message(getApplicationContext(), "Connection error");
                Log.d(TAG, "onFailure: "+msg);
//                intent.putExtra("msg", counter);
//                intent.putExtra("counter", msg);
//                sendBroadcast(intent);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response!=null){
                    String msg = response.body().string();
//                    deviceData = gson.fromJson(request.body().toString(), DeviceDetails.class);
                    Log.d(TAG, "onResponse: "+msg);
                    intent.putExtra("counter", msg);
                    sendBroadcast(intent);
                }
            }
        });
    }


}
