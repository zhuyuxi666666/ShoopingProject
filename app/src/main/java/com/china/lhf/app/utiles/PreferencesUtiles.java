package com.china.lhf.app.utiles;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.SoftReference;

/**
 * Created by C罗 on 2016/9/26.
 */
public class PreferencesUtiles {

    public static final String PREFERENCES_NAME="shop_common";
    public static boolean putString(Context context,String key,String value){
        //设为私有   其他应用无法访问
        SharedPreferences settings=context.getSharedPreferences(PREFERENCES_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=settings.edit();
        editor.putString(key,value);
        return editor.commit();
    }

    public static String getString(Context context,String key){
        return getString(context,key,null);
    }

    public static String getString(Context context,String key,String defaultValue){
        SharedPreferences settings=context.getSharedPreferences(PREFERENCES_NAME,Context.MODE_PRIVATE);
        return settings.getString(key,defaultValue);
    }

}
