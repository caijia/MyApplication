package com.example.administrator.myapplication.http;

import java.io.IOException;
import java.util.concurrent.Callable;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 异步请求处理(将请求后的结果通过handler发到主线程)
 * Created by cai.jia on 2016/7/12 0012.
 */
public class AsyncHttpRequest implements Callable<Boolean> {

    private OkHttpClient client;

    private AsyncHttpResponseHandler handler;

    private Request request;

    private Call call;

    public AsyncHttpRequest(OkHttpClient client, Request request, AsyncHttpResponseHandler handler) {
        this.client = client;
        this.request = request;
        this.handler = handler;
    }

    @Override
    public Boolean call() throws Exception {
        if (request != null && client != null && handler != null) {
            call = client.newCall(request);
            Response response = null;
            try {
                response = call.execute();
                if (response.isSuccessful()) {
                    handler.sendSuccessMessage(call, response.body().string());
                } else {
                    handler.sendFailureMessage(call, new IOException("http io exception " + response));
                }
            } catch (Exception e) {
                handler.sendFailureMessage(call, new IOException("http io exception "));
            }finally {
                if (response != null) {
                    response.close();
                }
            }
        }
        return true;
    }

    public void cancel(Object tag) {
        if (tag != null && call != null) {
            if (tag == call.request().tag()) {
                call.cancel();
            }
        }
    }
}
