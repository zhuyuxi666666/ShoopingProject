package com.china.lhf.app.http;

import android.content.Context;
import android.content.Intent;

import com.china.lhf.app.LoginActivity;
import com.china.lhf.app.R;
import com.china.lhf.app.application.MyApplication;
import com.china.lhf.app.utiles.ToastUtils;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by C罗 on 2016/9/19.
 */
public abstract class SpostsCallback<T> extends BaseCallback<T> {

    private Context mContext;
    private SpotsDialog mDialog;


    public SpostsCallback(Context context) {
            this(context,"拼命加载中。。。");
    }

    public SpostsCallback(Context context,String message) {
          mContext=context;
        mDialog=new SpotsDialog(mContext,message);
    }

    public void showDialog(){
           mDialog.show();
    }

    public void dissmissDialog(){
        if(mDialog!=null){
            mDialog.dismiss();
        }
    }

    public void setMessage(String msg){
        mDialog.setMessage(msg);
    }

    @Override
    public void onRequestBefore(Request request) {
           showDialog();
    }

    @Override
    public void onFailure(Request request, IOException e) {
             dissmissDialog();
    }

    public void onResponse(Response response){
        dissmissDialog();
    }

    @Override
    public void onErroe(Response response, int code, Exception e) {

    }

    @Override
    public void onTokenError(Response response,int code) {
        ToastUtils.show(mContext, R.string.token_error);
        Intent intent=new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
        MyApplication.getInstance().clearUser();
    }
}
