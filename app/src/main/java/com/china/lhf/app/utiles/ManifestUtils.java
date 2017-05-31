package com.china.lhf.app.utiles;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.china.lhf.app.application.MyApplication;

/**
 * Created by C罗 on 2016/10/17.
 */
public class ManifestUtils  {

    public static String[] getMetaDataValueArray(Context context,String keyName,String secretName){
        String[] value=new String[2];
        PackageManager packageManager=context.getPackageManager();
        try {
            ApplicationInfo applicationInfo=packageManager.getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
            if (applicationInfo!=null&&applicationInfo.metaData!=null){
                value[0]=applicationInfo.metaData.getString(keyName);
                value[1] =applicationInfo.metaData.getString(secretName);
            }

        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not read the name in the manifest file.",e);

        }
        if (value[0]==null){
            throw new RuntimeException("The name"+keyName+"is not find keyName");
        }else if (value[1]==null){
            throw new RuntimeException("The name"+secretName+"secretName");
        }
        return value;
    }

}
