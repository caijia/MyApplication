package com.example.administrator.myapplication.rx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.rx.retrofit.ApiService;
import com.example.administrator.myapplication.rx.retrofit.NetworkInterceptor;
import com.example.administrator.myapplication.rx.retrofit.Result;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cai.jia on 2017/4/14 0014
 */

public class RxDemoActivity extends AppCompatActivity {

    private TextView textView;
    private String baseUrl = "https://api.6383.com";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_demo);
        textView = (TextView) findViewById(R.id.text_view);

        NetworkInterceptor interceptor = new NetworkInterceptor();
        Map<String, Object> cParams = new HashMap<>();
        cParams.put("name", "caijia");
        interceptor.setParams(cParams);

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Map<String, Object> params = new HashMap<>();
        params.put("aa", "22");
        Flowable<Result> flowable = apiService.getText(baseUrl+"/Product/isshowdata",params);
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FlowableSubscriber<Result>() {
                    @Override
                    public void onSubscribe(@NonNull Subscription s) {

                    }

                    @Override
                    public void onNext(Result result) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
