package app.quicktrack.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import app.quicktrack.R;

/**
 * Created by Rakhi.
 * Contact Number : +91 9958187463
 */
public class Utility {
    static ProgressDialog progressDialog;

    public static Bitmap getCircularBitmap(Bitmap bitmap)
    {
        final Bitmap circularimage=Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(circularimage);
        int mColor= Color.RED;
        Paint paint=new Paint();
        Rect rect=new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        RectF rectF=new RectF(rect);
        paint.setAntiAlias(true);
        paint.setColor(mColor);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawARGB(0,0,0,0);
        canvas.drawOval(rectF,paint);
        canvas.drawBitmap(bitmap,rect,rect,paint);
        bitmap.recycle();

        return bitmap;
    }

    public static BitmapDescriptor bitmapDescriptorFromVectorR(Context context) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_a);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        //  vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    public static BitmapDescriptor bitmapDescriptorFromVectorY(Context context) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_marker_yellow);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        //   Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        //    vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        //  vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    public static BitmapDescriptor bitmapDescriptorFromVectorG(Context context) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_marker_green);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        //   Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        //    vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        //  vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public  static String stripLeadingAndTrailingQuotes(String str)
    {
        if (str.startsWith("\""))
        {
            str = str.substring(1, str.length());
        }
        if (str.endsWith("\""))
        {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static String address;
    public static String getAddress(Context mContext, double latitude, double longitude){

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            Log.d("TAG", "getInfoContents: "+address);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }


    public static double getDouble(double d){
        BigDecimal bd = new BigDecimal(d);
        bd = bd.round(new MathContext(8));
        double rounded = bd.doubleValue();
        return rounded;
    }
    public static double getSpeedDouble(double d){
        BigDecimal bd = new BigDecimal(d);
        bd = bd.round(new MathContext(4));
        double rounded = bd.doubleValue();
        return rounded;
    }

    public static String BitMapToString(Bitmap bitmap){
        String temp="";
        if(bitmap!=null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
            byte[] b = baos.toByteArray();
            temp = Base64.encodeToString(b, Base64.DEFAULT);
        }
        return temp;
    }

    public static String getTime(long timestamp){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000L);
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss a", cal).toString();
        return date;
    }

    public static Boolean isNetworkConnected(Context context)
    {
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if (networkInfo!=null && networkInfo.isConnectedOrConnecting())
            return true;
        return false;
    }

    public static Boolean checkemail(final String emai)
    {
        if(emai!=null)
        {
            Pattern pattern= Patterns.EMAIL_ADDRESS;
            if(pattern.matcher(emai).matches())
            {
                return pattern.matcher(emai).matches();
            }
        }
        return false;

    }


    public static Boolean checkphone(final String phone)
    {
        if(phone!=null)
        {
            Pattern pattern= Patterns.PHONE;
            if(pattern.matcher(phone).matches())
            {
                return pattern.matcher(phone).matches();
            }
        }
        return false;

    }
    public static void showloadingPopup(Activity activity)
    {
        if(progressDialog!=null)
        {
            progressDialog.dismiss();
        }
        progressDialog=new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
    }

    public static void showLoading(Activity activity,String message)
    {
        if(progressDialog!=null)
        {
            progressDialog.dismiss();
        }
        progressDialog=new ProgressDialog(activity);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public  static void hidepopup()
    {
        if(progressDialog!=null)
        {
            progressDialog.dismiss();
        }
        progressDialog=null;
    }
    public static void message(Context context,String Msg)
    {

        Toast.makeText(context,Msg,Toast.LENGTH_SHORT).show();
    }

    public static boolean checkGooglePlayService(Activity activity)
    {
        int checkGooglePlayService= GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        int Requestcode=200;
        if(checkGooglePlayService!= ConnectionResult.SUCCESS)
        {
            GooglePlayServicesUtil.getErrorDialog(checkGooglePlayService,activity,Requestcode);
            Toast.makeText(activity," not working",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkCameraPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}
