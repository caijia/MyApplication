package com.example.administrator.myapplication.rx.retrofit;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by cai.jia on 2017/4/14 0014
 */

public interface ApiService {

    @FormUrlEncoded
    @POST
    Flowable<Result> getText(@Url String url,@FieldMap Map<String,Object> params);
}
