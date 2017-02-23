package com.example.administrator.myapplication.http;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 请求参数
 * Created by cai.jia on 2016/7/13 0013.
 */
public class RequestParams {

    protected LinkedHashMap<String, String> urlParams;
    protected ConcurrentHashMap<String, FileWrapper> fileParams;

    public RequestParams() {
        urlParams = new LinkedHashMap<>();
        fileParams = new ConcurrentHashMap<>();
    }

    public void put(Map<String, String> map) {
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                urlParams.put(entry.getKey(), TextUtils.isEmpty(entry.getValue())?"":entry.getValue());
            }
        }
    }

    public void put(JSONObject jo) {
        if (jo != null) {
            Iterator<String> keys = jo.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                try {
                    String value = jo.getString(key);
                    urlParams.put(key, TextUtils.isEmpty(value)?"":value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getParamsString() {
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String> entry : urlParams.entrySet()) {
            params.append(entry.getValue());
        }
        return params.toString();
    }

    public String getSortParamsString() {
        if (urlParams.isEmpty()) {
            return "";
        }

        Collection<String> keys = urlParams.keySet();
        String[] pArray = keys.toArray(new String[]{});
        Arrays.sort(pArray);
        StringBuilder params = new StringBuilder();
        for (String key : pArray) {
            if (!TextUtils.isEmpty(urlParams.get(key))) {

                if (params.length() > 0) {
                    params.append("&");
                }

                params.append(key);
                params.append("=");
                params.append(urlParams.get(key));
            }
        }
        return params.toString();
    }

    public void put(String key, String value) {
        urlParams.put(key, TextUtils.isEmpty(value) ? "" : value);
    }

    public void put(String key, File file, String contentType) throws FileNotFoundException {
        fileParams.put(key, new FileWrapper(file, contentType));
    }

    public RequestBody getPostBody() {
        return getPostBody(null);
    }

    public RequestBody getPostBody(ProgressRequestBody.Listener listener) {
        RequestBody requestBody;
        if (!fileParams.isEmpty()) {

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            for (Map.Entry<String, String> entry : urlParams.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
                FileWrapper fileWrapper = entry.getValue();
                builder.addFormDataPart(entry.getKey(), fileWrapper.file.getName(),
                        RequestBody.create(MediaType.parse(fileWrapper.contentType),
                        fileWrapper.file));
            }

            requestBody = builder.build();
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : urlParams.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }

            requestBody = builder.build();
        }
        return requestBody;
    }

    public String getParamString() {
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String> entry : urlParams.entrySet()) {
            if (params.length() > 0) {
                params.append("&");
            }

            params.append(entry.getKey());
            params.append("=");
            params.append(entry.getValue());
        }
        return params.toString();
    }

    public String getUrlWithRestString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : urlParams.entrySet()) {
            sb.append("/");
            sb.append(entry.getValue());
        }
        return sb.toString();
    }

    public void remove(String key) {
        urlParams.remove(key);
        fileParams.remove(key);
    }

    @Override
    public String toString() {
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String> entry : urlParams.entrySet()) {
            if (params.length() > 0) {
                params.append("&");
            }

            params.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue());
        }

        for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
            if (params.length() > 0)
                params.append("&");

            params.append(entry.getKey());
            params.append("=");
            params.append("FILE");
        }
        return params.toString();
    }

    private static class FileWrapper {
        public File file;
        public String contentType;

        public FileWrapper(File file, String contentType) {
            this.file = file;
            this.contentType = contentType;
        }
    }
}
