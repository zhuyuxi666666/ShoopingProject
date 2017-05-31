package com.china.lhf.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.china.lhf.app.LoginActivity;
import com.china.lhf.app.application.MyApplication;
import com.china.lhf.app.entity.User;

import butterknife.ButterKnife;

/**
 * Created by C罗 on 2016/10/12.
 */
public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=createView(inflater,container,savedInstanceState);
        ButterKnife.bind(this,view);
        initToobar();
        init();
        return view;
    }

    public void initToobar(){

    }

    public abstract View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    public abstract void init();

    public void startActivity(Intent intent,boolean isNeedLogin){
        if(isNeedLogin){
            User user = MyApplication.getInstance().getUser();
            if(user!=null){
                super.startActivity(intent);
            } else {
                MyApplication.getInstance().putIntent(intent);
                Intent loginIntent=new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);
            }
        }else {
            super.startActivity(intent);
        }
    }

    public void startActivityForResult(Intent intent,int requestCode, boolean isNeedLogin){
        if(isNeedLogin){
            User user = MyApplication.getInstance().getUser();
            if(user!=null){
                super.startActivityForResult(intent,requestCode);
            } else {
                MyApplication.getInstance().putIntent(intent);
              Intent loginIntent=new Intent(getActivity(), LoginActivity.class);
                super.startActivityForResult(intent,requestCode);
            }
        }else {
            super.startActivityForResult(intent,requestCode);
        }
    }

}
