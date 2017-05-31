

        package com.china.lhf.app.http;

        import android.os.Handler;
        import android.os.Looper;
        import android.text.TextUtils;

        import com.china.lhf.app.application.MyApplication;
        import com.google.gson.Gson;
        import com.google.gson.JsonParseException;
        import com.google.gson.JsonSyntaxException;

        import java.io.IOException;
        import java.util.HashMap;
        import java.util.Map;
        import java.util.concurrent.TimeUnit;


        import okhttp3.Call;
        import okhttp3.Callback;
        import okhttp3.FormBody;
        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.RequestBody;
        import okhttp3.Response;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class OkHttpHelper {
    private static OkHttpHelper mInstance;
    private static OkHttpClient okHttpClient;
    private Gson mGson;
    private Handler mHandler;

    public static final int TOKEN_MISSING=401;//token丢失
    public static final int TOKEN_ERROR=402;//错误
    public static final int TOKEN_EXPIRE=403;//过期


    static {
        mInstance = new OkHttpHelper();
    }

    private OkHttpHelper() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).build();
        mGson = new Gson();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpHelper getInstance() {
        return mInstance;
    }

    public void doRequest(final Request request, final BaseCallback callback) {
        callback.onRequestBefore(request);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(request, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //  mGson.fromJson(response.body().string(),callback.mType);
                if (response.isSuccessful()) {
                    String resultStr = response.body().string();
                    if (callback.mType == String.class) {
                        //  callback.onSuccess(response, resultStr);
                        callbackSuccess(callback, response, resultStr);
                    } else {
                        try {
                            Object object = mGson.fromJson(resultStr, callback.mType);
                            //      callback.onSuccess(response, object);
                            callbackSuccess(callback, response, object);
                        } catch (JsonSyntaxException e) {//JSON语法异常
                            //callback.onError(response,response.code(),e);
                            callbackError(callback, response, e);
                        } catch (JsonParseException e) {//JSON解析的错误
                            //  callback.onError(response,response.code(),e);
                            callbackError(callback, response, e);
                        }
                    }
                } else if(response.code()==TOKEN_MISSING || response.code()==TOKEN_ERROR || response.code()==TOKEN_EXPIRE){
                    callbackTokenError(callback,response);
                } else {
                    //   callback.onError(response, response.code(), null);
                    callbackError(callback, response, null);
                }
                callback.onResponse(response);
            }

            private void callbackTokenError(final BaseCallback callback, final Response response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onTokenError(response,response.code());
                    }
                });
            }

        });
    }



    public void get(String url, BaseCallback callBack) {
          get(url,null,callBack);
    }

    public void get(String url, Map<String, Object> params, BaseCallback callBack) {
        Request request = buildRequest(url, params, HttpMethodType.Get);
        doRequest(request, callBack);
    }

    public void post(String url, Map<String, Object> params, BaseCallback callBack) {
        Request request = buildRequest(url, params, HttpMethodType.Post);
        doRequest(request, callBack);
    }

    private Request buildRequest(String url, Map<String, Object> params, HttpMethodType methodType) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (methodType == HttpMethodType.Get) {
            url=buildUrlParams(url,params);
            builder.url(url);
            builder.get();
        } else if (methodType == HttpMethodType.Post) {
            RequestBody body = buildFormData(params);
            builder.post(body);
        }
        return builder.build();
    }

    private RequestBody buildFormData(Map<String, Object> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue().toString());
            }
            String token= MyApplication.getInstance().getToken();
            if(!TextUtils.isEmpty(token)){
                builder.add("token",token);
            }
        }
        return builder.build();
    }

    private void callbackSuccess(final BaseCallback callback, final Response response, final Object object) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, object);
            }
        });
    }

    private void callbackError(final BaseCallback callback, final Response response, final Exception e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onErroe(response, response.code(), e);
            }
        });
    }

    enum HttpMethodType {
        Get, Post
    }

    private String buildUrlParams(String url,Map<String ,Object> params){
        if(params==null){
            params=new HashMap<>(1);
        }
        String token=MyApplication.getInstance().getToken();
        if(!TextUtils.isEmpty(token)){
            params.put("token",token);
        }
        StringBuffer sb=new StringBuffer();
        for (Map.Entry<String ,Object> entry:params.entrySet()){
            sb.append(entry.getKey()+"="+entry.getValue());
            sb.append("&");
        }
        String s=sb.toString();
        if(s.endsWith("&")){
            s=s.substring(0,s.length()-1);
        }
        if(url.indexOf("?")>0){
            url+="&"+s;
        }else {
            url+="?"+s;
        }
        return url;
    }
}

 