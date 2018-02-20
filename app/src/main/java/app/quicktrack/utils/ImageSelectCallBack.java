package app.quicktrack.utils;

import android.graphics.Bitmap;

/**
 * Created by sony on 17-11-2016.
 */

public interface ImageSelectCallBack {

    void success(String data) ;
    void success(Bitmap data) ;
    void fail(String error) ;

}
