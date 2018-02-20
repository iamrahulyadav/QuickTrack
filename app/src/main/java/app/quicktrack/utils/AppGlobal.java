package app.quicktrack.utils;

import android.app.Application;
import android.content.Context;

import com.mukesh.tinydb.TinyDB;

import app.quicktrack.models.LoginData;


/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class AppGlobal extends Application {

    public static final int ERROR_CODE = 102;
    private static AppGlobal appGlobal ;
    public Context context;
    TinyDB sharedpref;

    public String loginData = "";
    public String user_name="" ;
    public String user_password="" ;


    public String getLoginData() {
        return loginData;
    }

    public void setLoginData(String loginData) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("login_Data",loginData);
        String s = sharedpref.getString("login_Data");
        this.loginData = loginData;
    }

    public void setUser_name(String user_name) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_name",user_name);
        this.user_name = user_name;
    }

    public void setUser_password(String user_password) {
        sharedpref=new TinyDB(context);
        sharedpref.putString("user_password",user_password);
        this.user_name = user_name;
    }


    public String getUser_name() {
        return user_name;
    }
    public String getUser_lname() {
        return user_password;
    }



    public static AppGlobal getInstancess(){
        if(appGlobal==null){ appGlobal=new AppGlobal(); }
        return appGlobal;
    }


}
