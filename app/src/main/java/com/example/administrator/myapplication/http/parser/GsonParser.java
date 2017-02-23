package com.example.administrator.myapplication.http.parser;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Gson解析器
 * Created by cai.jia on 2016/7/13 0013.
 */
public class GsonParser implements Parser {

    private Gson gson;

    public GsonParser() {
        GsonBuilder gsonb = new GsonBuilder();
        gsonb.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

            @SuppressLint("UseValueOf")
            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                String date = json.getAsJsonPrimitive().getAsString();
                String JSONDateToMilliseconds = "\\/(Date\\((.*?)(\\+.*)?\\))\\/";
                Pattern pattern = Pattern.compile(JSONDateToMilliseconds);
                Matcher matcher = pattern.matcher(date);
                String result = matcher.replaceAll("$2");
                try {
                    return new Date(new Long(result));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        gson = gsonb.create();
    }

    @Override
    public <T> T parser(String result, Type type) throws Exception {
        return gson.fromJson(result, type);
    }
}
