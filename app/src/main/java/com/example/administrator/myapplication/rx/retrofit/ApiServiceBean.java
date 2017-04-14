package com.example.administrator.myapplication.rx.retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cai.jia on 2017/4/14 0014
 */

public class ApiServiceBean {

    private static final String BASE_URL = "";
    private static volatile ApiServiceBean instance = null;
    private ApiService apiService;

    public static ApiServiceBean getInstance() {
        if (instance == null) {
            synchronized (ApiServiceBean.class) {
                if (instance == null) {
                    instance = new ApiServiceBean();
                }
            }
        }
        return instance;
    }

    private ApiServiceBean() {
        Map<String, Object> cParams = new HashMap<>();
        NetworkInterceptor interceptor = new NetworkInterceptor(cParams);

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }
}
