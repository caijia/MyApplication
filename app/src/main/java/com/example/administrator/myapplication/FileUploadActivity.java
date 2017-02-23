package com.example.administrator.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.myapplication.http.HttpRequestManager;
import com.example.administrator.myapplication.http.HttpResponseCallback;
import com.example.administrator.myapplication.http.RequestParams;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;

/**
 * Created by cai.jia on 2017/1/16 0016
 */

public class FileUploadActivity extends AppCompatActivity implements HttpResponseCallback<Object> {

    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);

        imageView = (ImageView) findViewById(R.id.image_view);
        imageView.setImageURI(Uri.fromFile(new File("/mnt/sdcard/test.jpg")));
    }

    public void upLoad2(View view) {
        String url = "http://120.25.120.213:8081/service/mobileAdapter/imageRecon";
        com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
        params.put("userId","69a7b28c80134ab3bc1b845490ff63e4");
        params.put("signature","1666b5f77ad022ca06f3b95fcc03322a");
        params.put("random","615625");
        try {
            params.put("mFile", new File("/mnt/sdcard/test.jpg"), "application/octet-stream");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        new AsyncHttpClient().post(this, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println("onSuccess");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("onFailure");
            }
        });
    }

    public void upLoad(View view) {
        String url = "http://120.25.120.213:8081/service/mobileAdapter/imageRecon";
        RequestParams params = new RequestParams();
        params.put("userId","69a7b28c80134ab3bc1b845490ff63e4");
        params.put("signature","1666b5f77ad022ca06f3b95fcc03322a");
        params.put("random","615625");
        try {
            params.put("mFile", new File("/mnt/sdcard/test.jpg"), "application/octet-stream");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        params.put("content-type", "application/octet-stream");
        Type type = new TypeToken<Object>() {
        }.getType();
        HttpRequestManager.getInstance().post(this,1,"aa",url,params,type,this);
    }

    @Override
    public void onSuccess(long requestId, Object data, String result) {
        System.out.println("onSuccess");
    }

    @Override
    public void onFailure(long requestId, String code, String message) {
        System.out.println("onFailure");
    }
}
