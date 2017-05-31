package com.china.lhf.app.utiles;

import android.content.Context;
import android.text.TextUtils;

import com.china.lhf.app.entity.User;
import com.china.lhf.app.http.Contants;

/**
 *
 */
public class UserLocalData {
    public static void putUser(Context context, User user){
        String user_json=GsonUtils.toJson(user);
        PreferencesUtiles.putString(context, Contants.USER_JSON,user_json);
    }

    public static void putToken(Context context,String token){
        PreferencesUtiles.putString(context,Contants.TOKEN,token);
    }

    public static User getUser(Context context){
        String user_json=PreferencesUtiles.getString(context,Contants.USER_JSON);
        if(!TextUtils.isEmpty(user_json)){
            return GsonUtils.fromJson(user_json,User.class);
        }
        return null;
    }

    public static String getToken(Context context){
            return PreferencesUtiles.getString(context,Contants.TOKEN);
    }

    public static void clearUser(Context context){
        PreferencesUtiles.putString(context,Contants.USER_JSON,"");
    }

    public static void clearToken(Context context){
        PreferencesUtiles.putString(context,Contants.TOKEN,"");
    }
}
