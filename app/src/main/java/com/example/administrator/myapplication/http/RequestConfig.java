package com.example.administrator.myapplication.http;

import android.content.Context;

/**
 * 请求额外参数构造器
 * Created by cai.jia on 2016/7/14 0014.
 */
public final class RequestConfig {

    private final long requestId;

    private final Context context;

    public RequestConfig(Builder builder) {
        this.requestId = builder.requestId;
        this.context = builder.context;
    }

    public long requestId() {
        return requestId;
    }

    public Context context(){
        return context;
    }

    public static class Builder {

        private long requestId;

        private Context context;

        public Builder context(Context context){
            this.context = context;
            return this;
        }

        public Builder requestId(long requestId) {
            this.requestId = requestId;
            return this;
        }

        public RequestConfig build() {
            return new RequestConfig(this);
        }
    }
}
