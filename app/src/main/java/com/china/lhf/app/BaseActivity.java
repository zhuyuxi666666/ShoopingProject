package com.china.lhf.app;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.china.lhf.app.application.MyApplication;
import com.china.lhf.app.entity.User;
import com.china.lhf.app.utiles.DialogMaker;

/**
 * Created by C罗 on 2016/10/17.
 */
public class BaseActivity extends AppCompatActivity implements DialogMaker.DialogCallBack{

    protected Dialog dialog;

    public void startActivity(Intent intent,boolean isNeedLogin){
        if(isNeedLogin){
            User user = MyApplication.getInstance().getUser();
            if(user!=null){
                super.startActivity(intent);
            } else {
                MyApplication.getInstance().putIntent(intent);
                Intent loginIntent=new Intent(this, LoginActivity.class);
                super.startActivity(intent);
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
                Intent loginIntent=new Intent(this, LoginActivity.class);
                super.startActivityForResult(intent,requestCode);
            }
        }else {
            super.startActivityForResult(intent,requestCode);
        }
    }

    /**
     * 弹出对话框
     *
     * @param title                  标题
     * @param msg                    内容
     * @param btns                   要显示几个按钮就传几个String[]
     * @param isCanCancelabel        是否可以点击back键消失对话框
     * @param isDismissAfterClickBtn 点击任何按钮后是否消失对话框
     * @param tag
     * @return 显示并返回对话框
     */
    public Dialog showAlertDialog(String title, String msg, String[] btns,
                                  boolean isCanCancelabel,
                                  final boolean isDismissAfterClickBtn, Object tag) {
        if (null == dialog || !dialog.isShowing()) {
            dialog = DialogMaker.showCommonAlertDialog(this, title, msg, btns, this, isCanCancelabel, isDismissAfterClickBtn, tag);
        }
        return dialog;
    }

    /**
     * 关闭对话框
     */
    public void dismissDialog() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected synchronized void onDestroy() {
        //出栈
        dismissDialog();
        super.onDestroy();
    }


    @Override
    public void onButtonClicked(Dialog dialog, int position, Object tag) {

    }

    @Override
    public void onCancelDialog(Dialog dialog, Object tag) {

    }
}
