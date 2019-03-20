package com.example.yls.demoa;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by yls on 2019/3/18.
 */

public class MyImageCache implements ImageLoader.ImageCache{
    private static final String TAG = "MyImageCache";
    private LruCache<String, Bitmap> lruCache;
    public MyImageCache(){
        int maxSize = 10*1024*1024;
        lruCache = new LruCache<String, Bitmap>(maxSize){
            protected int sizeOf(String key,Bitmap bitmap){
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }



    @Override
    public Bitmap getBitmap(String s) {
        Bitmap bitmap =lruCache.get(s);
        if(bitmap ==null){
            Log.e(TAG,"getBitmap failed bitmap ==null");
        }else {
            Log.e(TAG,"getBitmap success bitmap !=null");

        }
        return bitmap;
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        lruCache.put(s,bitmap);
    }
}
