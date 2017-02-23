package com.example.administrator.myapplication.http;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;

import okhttp3.Call;

/**
 * 将请求后的结果发到主线程
 * Created by cai.jia on 2016/7/12 0012.
 */
public abstract class AsyncHttpResponseHandler {

    protected static final int SUCCESS_MESSAGE = 0;
    protected static final int FAILURE_MESSAGE = 1;
    protected static final int PROGRESS_MESSAGE = 2;

    private Handler handler;

    public AsyncHttpResponseHandler() {
        handler = new ResponseHandler();
    }

    @SuppressLint("HandlerLeak")
    public class ResponseHandler extends Handler {

        private ResponseHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS_MESSAGE: {
                    Object[] response = (Object[]) msg.obj;
                    onSuccess((Call) response[0], (String) response[1]);
                    break;
                }

                case FAILURE_MESSAGE: {
                    Object[] response = (Object[]) msg.obj;
                    if (response.length > 1) {
                        onFailure((Call) response[0], (IOException) response[1]);

                    } else {
                        onFailure((Call) response[0], null);
                    }
                    break;
                }

                case PROGRESS_MESSAGE: {
                    Object[] response = (Object[]) msg.obj;
                    onProgress((Integer) response[0]);
                    break;
                }
            }
        }
    }

    public void sendSuccessMessage(Call call, String result) {
        handler.sendMessage(handler.obtainMessage(SUCCESS_MESSAGE, new Object[]{call, result}));
    }

    public void sendFailureMessage(Call call, IOException e) {
        if (e == null) {
            handler.sendMessage(handler.obtainMessage(FAILURE_MESSAGE, new Object[]{call}));

        } else {
            handler.sendMessage(handler.obtainMessage(FAILURE_MESSAGE, new Object[]{call, e}));
        }
    }

    public void sendProgressMessage(int progress) {
        handler.sendMessage(handler.obtainMessage(PROGRESS_MESSAGE, new Object[]{progress}));
    }

    public abstract void onSuccess(Call call, String result);

    public void onProgress(int progress) {

    }

    public abstract void onFailure(Call call, IOException e);
}
