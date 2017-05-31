package com.china.lhf.app.utiles;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by C罗 on 2016/9/26.
 */
public class GsonUtils {


    private static Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static Gson getGson(){
           return gson;
    }

    public static <T> T fromJson(String json,Class<T> clz){
        return gson.fromJson(json,clz);
    }

    public static <T> T fromJson(String json, Type type){
        return gson.fromJson(json,type);
    }

    public static String toJson(Object object){
        return gson.toJson(object);
    }
}
