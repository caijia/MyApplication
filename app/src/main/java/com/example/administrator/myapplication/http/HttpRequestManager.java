package com.example.administrator.myapplication.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.administrator.myapplication.BuildConfig;
import com.example.administrator.myapplication.http.parser.GsonParser;
import com.example.administrator.myapplication.http.parser.Parser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * okhttp请求的封装类
 * Created by cai.jia on 2016/7/11 0011.
 */
public class HttpRequestManager {

    private static final String TAG = HttpRequestManager.class.getSimpleName();

    public static final String SERVER_ERROR = "server_error";
    public static final String NETWORK_ERROR = "network_error";
    public static final String PARSER_ERROR = "parser_error";
    private static volatile HttpRequestManager manager;
    private OkHttpClient client;
    private ExecutorService executorService;
    private Map<Object, Set<AsyncHttpRequest>> tagRequest;

    private Parser parser;

    private HttpRequestManager() {
        executorService = Executors.newCachedThreadPool();
        tagRequest = new ConcurrentHashMap<>();
        parser = new GsonParser();
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public static HttpRequestManager getInstance() {
        if (manager == null) {
            synchronized (HttpRequestManager.class) {
                manager = new HttpRequestManager();
            }
        }
        return manager;
    }

    public static String getUrlWithQueryString(String url, RequestParams params) {
        if (params != null) {
            String paramString = params.toString();
            if (!url.contains("?")) {
                url += "?" + paramString;
            } else {
                url += "&" + paramString;
            }
        }

        return url;
    }

    @NonNull
    private <T> AsyncHttpResponseHandler getHandler(final long requestId, final Type type,
                                                    final HttpResponseCallback<T> callback) {
        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(Call call, String result) {
                if (callback == null) {
                    return;
                }

                try {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, result);
                    }
                    if (type == null) {
                        callback.onSuccess(requestId, (T) result, result);

                    } else {
                        T t = parser.parser(result, type);
                        if (t != null) {
                            callback.onSuccess(requestId, t, result);

                        } else {
                            callback.onFailure(requestId, PARSER_ERROR, result);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure(requestId, PARSER_ERROR, result);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                if (e != null) {
                    e.printStackTrace();
                }

                if (callback == null) {
                    return;
                }
                callback.onFailure(requestId, SERVER_ERROR, SERVER_ERROR);
            }
        };
    }

    public <T> void get(Context context, long requestId, Object tag, String url,
                        RequestParams params, Type type, HttpResponseCallback<T> callback) {
        RequestConfig config = new RequestConfig.Builder()
                .requestId(requestId)
                .context(context)
                .build();

        Request request = new Request.Builder()
                .get()
                .url(getUrlWithQueryString(url, params))
                .tag(tag)
                .build();
        sendRequest(config, request, type, callback);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, getUrlWithQueryString(url, params));
        }

    }

    public <T> void post(Context context, long requestId, Object tag, String url,
                         RequestParams params, Type type, HttpResponseCallback<T> callback) {
        post(context,requestId,tag,url,params,type,true,callback);
    }

    public <T> void post(Context context, long requestId, Object tag, String url,
                         RequestParams params, Type type, boolean addCommonParams,
                         HttpResponseCallback<T> callback) {
        RequestConfig config = new RequestConfig.Builder()
                .requestId(requestId)
                .context(context)
                .build();

        Request.Builder builder = new Request.Builder();
        if (params != null) {
            builder.post(params.getPostBody());
        }
        Request request = builder
                .url(url)
                .tag(tag)
                .build();
        sendRequest(config, request, type, callback);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, getUrlWithQueryString(url, params));
        }
    }

    public <T> void sendRequest(RequestConfig config, Request request, Type type,
                                HttpResponseCallback<T> callback) {
        if (config != null && config.context() != null) {
            boolean hasNetwork = isConnected(config.context());
            if (!hasNetwork) {
                callback.onFailure(config.requestId(), NETWORK_ERROR, NETWORK_ERROR);
                return;
            }
        }

        AsyncHttpResponseHandler handler = getHandler(config != null ? config.requestId() : -1,
                type, callback);
        sendRequest(request, handler);
    }

    public void sendRequest(final Request request, AsyncHttpResponseHandler handler) {
        final AsyncHttpRequest asyncRequest = new AsyncHttpRequest(client, request, handler);

        if (request != null && request.tag() != null) {
            Set<AsyncHttpRequest> requestList = tagRequest.get(request.tag());
            if (requestList == null) {
                requestList = new HashSet<>();
            }
            requestList.add(asyncRequest);
            tagRequest.put(request.tag(), requestList);
        }

        executorService.submit(new FutureTask<Boolean>(asyncRequest) {

            @Override
            protected void done() {
                if (request != null && request.tag() != null) {
                    Set<AsyncHttpRequest> requestList = tagRequest.get(request.tag());
                    if (requestList != null && !requestList.isEmpty()) {
                        requestList.remove(asyncRequest);
                    }
                }
            }
        });
    }

    public void cancel(Object tag) {
        if (tag == null) {
            return;
        }

        Set<AsyncHttpRequest> requestList = tagRequest.get(tag);
        if (requestList != null && !requestList.isEmpty()) {
            for (AsyncHttpRequest request : requestList) {
                request.cancel(tag);
            }
        }
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
