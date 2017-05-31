package com.china.lhf.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.china.lhf.app.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by C罗 on 2016/10/26.
 */
public class PatResultActivity extends AppCompatActivity {

    @Bind(R.id.success_bg_iv)
    ImageView payResultIV;
    @Bind(R.id.log_tv)
    TextView payResultTV;
    @Bind(R.id.back_home_btn)
    Button back_home;

    int status=-1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_success_order);
        ButterKnife.bind(this);
        status=getIntent().getIntExtra("status",-1);
        switch (status){
            case 1:
                payResultIV.setImageResource(R.drawable.icon_success_128);
                payResultTV.setText("支付成功");
                break;
            case -1:
                payResultIV.setImageResource(R.drawable.icon_cancel_128);
                payResultTV.setText("支付失败");
                break;
            case -2:
                payResultIV.setImageResource(R.drawable.icon_cancel_128);
                payResultTV.setText("取消支付");
                break;
            case 0:
                payResultIV.setImageResource(R.drawable.icon_cancel_128);
                payResultTV.setText("支付插件未安装");
                break;
        }
    }

    @OnClick(R.id.back_home_btn)
    public void onClick(){
             toHome();
    }

    @Override
    public void onBackPressed() {
        toHome();
    }

    private void toHome(){
        Intent intent=new Intent(PatResultActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        EventBus.getDefault().post(new MessageEvent("pay"));
    }
}
