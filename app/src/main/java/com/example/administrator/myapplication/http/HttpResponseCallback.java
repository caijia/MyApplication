package com.example.administrator.myapplication.http;

/**
 * 请求回调处理
 * Created by cai.jia on 2016/7/13 0013.
 */
public interface HttpResponseCallback<T> {

    /**
     * 请求成功
     *
     * @param requestId 唯一请求id
     * @param data      服务端数据序列化成对象
     * @param result    服务端返回未处理的结果
     */
    void onSuccess(long requestId, T data, String result);

    /**
     * 请求失败
     * @param requestId 唯一请求id
     * @param code      响应码
     * @param message   错误信息(如果code为{@link HttpRequestManager#PARSER_ERROR} 时 message
     *                  为服务端返回的完整输入)
     */
    void onFailure(long requestId, String code, String message);

}
