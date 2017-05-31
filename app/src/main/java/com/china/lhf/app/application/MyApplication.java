package com.china.lhf.app.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;

import com.china.lhf.app.entity.User;
import com.china.lhf.app.utiles.UserLocalData;
import com.facebook.drawee.backends.pipeline.Fresco;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by C罗 on 2016/9/7.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;
    private User user;
    private Intent intent;

    // 当前屏幕的高宽
    public int screenW = 0;
    public int screenH = 0;

    public Intent getIntent() {
        return intent;
    }

    public void putIntent(Intent intent) {
        this.intent = intent;
    }

    public static MyApplication getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;

        // 得到屏幕的宽度和高度
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenW = dm.widthPixels;
        screenH = dm.heightPixels;

        Fresco.initialize(this);
        ShareSDK.initSDK(this);
        initUser();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ShareSDK.stopSDK(this);
    }

    private void initUser() {
          this.user= UserLocalData.getUser(this);
    }
    public User getUser(){
        return user;
    }
    public void putUser(User user,String token){
        this.user=user;
        UserLocalData.putUser(this,user);
        UserLocalData.putToken(this,token);
    }

    public void clearUser(){
        this.user=null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    public String getToken(){
        return UserLocalData.getToken(this);
    }

    public void jumpToTargetActivity(Context context) {
         context.startActivity(intent);
         intent=null;
    }
}
