package app.quicktrack.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.mukesh.permissions.AppPermissions;
import com.mukesh.tinydb.TinyDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import app.quicktrack.R;
import app.quicktrack.models.EditProfileRequest;
import app.quicktrack.models.LoginData;
import app.quicktrack.models.LoginResponse;
import app.quicktrack.models.ProfileData;
import app.quicktrack.utils.ApiResponse;
import app.quicktrack.utils.AppGlobal;
import app.quicktrack.utils.RetrofitApi;
import app.quicktrack.utils.RetrofitApiBuilder;
import app.quicktrack.utils.Utility;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.quicktrack.utils.RetrofitApiBuilder.IMG_BASE_URL;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_title,txtCancel,txtSave, txtName ;
    private ImageView btnHome ;
    String img, userid,resellerid;
    int type;
    Gson gson;
    private final int  REQUEST_CAMERA=0, SELECT_FILE = 1;
    private Button btnSelect;
    private String userChoosenTask;

    boolean result;
    TinyDB tinyDB;
    AppPermissions appPermissions;
    LoginResponse loginData;
    ImageView imgUser;
    EditText edtCompanyName, edtEmail, edtPhone, edtSMS;
    private ImageView  imgCamera ;
    ProfileData profileData;
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    final OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setupToolBar();

//        initialize
        appPermissions = new AppPermissions(this);
        gson = new Gson();
        tinyDB = new TinyDB(getApplicationContext());
        loginData = new LoginResponse();
        profileData = new ProfileData();
        appGlobal.context=getApplicationContext();
        String data =  getIntent().getStringExtra("profile_data");
        profileData = gson.fromJson(data, ProfileData.class);


      /*  if (appPermissions.hasPermission(Manifest.permission.CAMERA)){
            Toast.makeText(EditProfileActivity.this, "Granted", Toast.LENGTH_SHORT).show();
        }else {
            appPermissions.requestPermission(Manifest.permission.CAMERA, 1);
        }
        if (appPermissions.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(EditProfileActivity.this, "Granted", Toast.LENGTH_SHORT).show();
        }else {
            appPermissions.requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 2);
        }*/
//        find id
        txtName= (TextView) findViewById(R.id.edit_profile_txtName);
        imgUser = findViewById(R.id.edt_img_profile);
        imgCamera = (ImageView) findViewById(R.id.edt_img_camera);
        txtCancel = (TextView) findViewById(R.id.edit_cancel);
        txtSave = (TextView) findViewById(R.id.edit_save) ;
        edtCompanyName = (EditText) findViewById(R.id.profile_txtCompanyName);
        edtEmail = (EditText) findViewById(R.id.edit_email);
        edtPhone = (EditText) findViewById(R.id.edit_phone);
        edtSMS = (EditText) findViewById(R.id.edt_sms);
        edtCompanyName = (EditText) findViewById(R.id.profile_txtCompanyName);
        Utility.isNetworkConnected(getApplicationContext());

        if (profileData!=null){
            txtName.setText(profileData.getResponse().getUserID());
            edtPhone.setText(profileData.getResponse().getContactPhone());
            edtEmail.setText(profileData.getResponse().getContactEmail());
            edtCompanyName.setText("QuickTrack Solutions");
            if (profileData.getResponse().getUser_profile_pic()!=null
                    && profileData.getResponse().getUser_profile_pic().length()>0){
//            Glide.with(getApplicationContext()).load(RetrofitApiBuilder.IMG_BASE_URL+profileData.getResponse().getUser_profile_pic())
//                    .apply(RequestOptions.circleCropTransform()).into(imgUser);
                String url = RetrofitApiBuilder.IMG_BASE_URL+profileData.getResponse().getUser_profile_pic();
                GetImage task = new GetImage();
                // Execute the task
                task.execute(new String[] { url });

            } else {
                Glide.with(getApplicationContext()).load(R.drawable.gfgf)
                        .apply(RequestOptions.circleCropTransform()).into(imgUser);
            }
//        img = Utility.BitMapToString(convertImageViewToBitmap(imgCamera));
        }

        if (imgUser.getDrawable()!=null){
            Drawable drawable= imgUser.getDrawable();
            BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
            Bitmap bitmap = bitmapDrawable .getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageInByte = stream.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);
            img = Utility.BitMapToString(bitmap);
        }

        imgCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

//        set onclick listener
        txtCancel.setOnClickListener(this);
        txtSave.setOnClickListener(this);

    }

    public class GetImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showloadingPopup(EditProfileActivity.this);
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
            imgUser.setImageBitmap(result);

            Glide.with(getApplicationContext()).load(result)
                    .apply(RequestOptions.circleCropTransform()).into(imgUser);
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
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
    }


    AppGlobal appGlobal= AppGlobal.getInstancess();

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if (appPermissions.hasPermission( Manifest.permission.CAMERA)){
                        result = true;
                        cameraIntent();
                    } else {

                        appPermissions.requestPermission(EditProfileActivity.this,  Manifest.permission.CAMERA, REQUEST_CAMERA);

                    }
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if (appPermissions.hasPermission( Manifest.permission.READ_EXTERNAL_STORAGE)){
                        result = true;
                        galleryIntent();
                    } else {

                        appPermissions.requestPermission(EditProfileActivity.this,  Manifest.permission.READ_EXTERNAL_STORAGE, SELECT_FILE);

                    }

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imgUser.setImageBitmap(thumbnail);
        Glide.with(getApplicationContext()).load(thumbnail)
                .apply(RequestOptions.circleCropTransform()).into(imgUser);
        img = Utility.BitMapToString(thumbnail);

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imgUser.setImageBitmap(bm);
        Glide.with(getApplicationContext()).load(bm)
                .apply(RequestOptions.circleCropTransform()).into(imgUser);
        img = Utility.BitMapToString(bm);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void setupToolBar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_title = (TextView) myToolbar.findViewById(R.id.tv_title);
        btnHome = (ImageView) myToolbar.findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onBackPressed(); }
        });
        setSupportActionBar(myToolbar);
    }

    private void setupTitle(String title) {
        tv_title.setText(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupTitle("Edit Profile") ;
        String login = tinyDB.getString("login_Data");
        loginData = gson.fromJson(login, LoginResponse.class);
        userid = loginData.getResponse().getUserid();
        resellerid = loginData.getResponse().getResellerid();
        type = loginData.getResponse().getType();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_cancel:
                finish();
                break;
            case R.id.edit_save:
                if (Utility.checkemail(edtEmail.getText().toString())) {
                    if (Utility.checkphone(edtPhone.getText().toString())){
                        if (!edtCompanyName.getText().toString().isEmpty()){
                            if (img!=null){
                                updateProfile();
                            } else {
                                Utility.message(getApplicationContext(), "Please select profile pic");
                            }
                        } else {
                            Utility.message(getApplicationContext(), "Please enter company name");
                        }
                    } else {
                        Utility.message(getApplicationContext(), "Please enter valid phone number");
                    }
                } else {
                    Utility.message(getApplicationContext(), "Please enter valid email ");
                }
                break;
        }
    }

    public void updateProfile(){

        Utility.showloadingPopup(this);
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String name = txtName.getText().toString().trim();
        String companyName = edtCompanyName.getText().toString();
        /*userid : test123
     * phone : 4545454545
     * email : test@gmail.com
     * type : 2
     * profile_image : */
        EditProfileRequest editProfileRequest = new EditProfileRequest();
        editProfileRequest.setEmail(email);
        editProfileRequest.setPhone(phone);
        editProfileRequest.setProfile_image(img);
        editProfileRequest.setUserid(userid);
        editProfileRequest.setType(type);
        String data = gson.toJson(editProfileRequest);

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE,Utility.stripLeadingAndTrailingQuotes(data));

        final Request request = new Request.Builder()
                .url(RetrofitApiBuilder.QUICK_BASE+"profile_edit")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();

        Utility.showloadingPopup(this);
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
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
                if (response!=null && response.body().toString().length()>0){
                    String msg = response.body().string();
                    Log.d("TAG", "onResponse: "+msg);
                    try {
                        JSONObject jsonObject = new JSONObject(msg);
                        if (jsonObject.getString("status").equals("true")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utility.message(getApplicationContext(), "User profile update successful");
                                    finish();
                                }
                            });

                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utility.message(getApplicationContext(), "User profile not updated");
                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Camera Permissions not granted", Toast.LENGTH_SHORT).show();
                } else {
                    result =true;
                    if(result){
                        cameraIntent();
                    }
                }
                break;
            case SELECT_FILE:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "file Permissions not granted", Toast.LENGTH_SHORT).show();
                } else {
                    result =true;
                    if(result)
                        galleryIntent();
                }
                break;
        }
    }

}
