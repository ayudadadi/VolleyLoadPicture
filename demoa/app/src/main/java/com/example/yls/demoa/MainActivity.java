package com.example.yls.demoa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final int GET_IMG = 1002;
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private ImageView imgv;
    private String imgUrl= "http://ica.gdcp.cn/service?wdApplication=nr&wdService=getData&wdtest=false&path=/upload/image/20190118/1547824760637005178/wpsA302.tmp.png";
    private Handler handler;
    private RequestQueue requestQueue;
    private MyImageCache myImageCache;
    private NetworkImageView netImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.btn_net);
        imgv = findViewById(R.id.imgv);
        myImageCache = new MyImageCache();
        netImg =(NetworkImageView) findViewById(R.id.netImg);
        handler =new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if(message.what ==GET_IMG){
                    Bitmap bitmap = (Bitmap)message.obj;
                    imgv.setImageBitmap(bitmap);
                    return true;
                }
                return false;
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(imgUrl);
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            InputStream inputStream = httpURLConnection.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            Message message = Message.obtain();
                            message.what = GET_IMG;
                            message.obj =bitmap;
                            handler.sendMessage(message);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageRequest imageRequest = new ImageRequest(imgUrl,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                imgv.setImageBitmap(bitmap);
                            }
                        }, 0, 0, Bitmap.Config.ARGB_8888,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        });
                requestQueue.add(imageRequest);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageLoader imageLoader = new ImageLoader(requestQueue,myImageCache);
                ImageLoader.ImageListener listener =ImageLoader.getImageListener
                        (imgv,R.drawable.load,R.drawable.error);
                imageLoader.get(imgUrl,listener);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageLoader imageLoader = new ImageLoader(requestQueue,myImageCache);
                netImg.setDefaultImageResId(R.drawable.load);
                netImg.setErrorImageResId(R.drawable.error);
                netImg.setImageUrl(imgUrl,imageLoader);
            }
        });

    }
}


