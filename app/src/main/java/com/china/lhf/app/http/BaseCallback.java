package com.china.lhf.app.http;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by C罗 on 2016/9/18.
 */
public abstract class BaseCallback<T> {

    public Type mType;
    public abstract void onRequestBefore(Request request);
    public abstract void onFailure(Request request, IOException e);
    public abstract void onSuccess(Response response,T t);
    public abstract void onErroe(Response response,int code,Exception e);
    public abstract void onTokenError(Response response,int code);
    public abstract void onResponse(Response response);

    /**
     * 将泛型T转为Type
     */
    static Type getSuperclassTypeParameter(Class<?> subclass){
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class){
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType)superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public BaseCallback(){
        mType = getSuperclassTypeParameter(getClass());
    }
}
