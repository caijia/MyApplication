package com.example.administrator.myapplication.http.parser;

import java.lang.reflect.Type;

/**
 * 服务端数据解析器
 * Created by cai.jia on 2016/7/13 0013.
 */
public interface Parser {

    /**
     * 将服务端返回的结果解析成对于的对象
     *
     * @param result 服务端返回的数据
     * @param type   对象类型
     */
    <T> T parser(String result, Type type) throws Exception;
}
