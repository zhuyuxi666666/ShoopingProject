package com.china.lhf.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.china.lhf.app.application.MyApplication;
import com.china.lhf.app.entity.User;
import com.china.lhf.app.http.Contants;
import com.china.lhf.app.http.OkHttpHelper;
import com.china.lhf.app.http.SpostsCallback;
import com.china.lhf.app.msg.LoginRespMsg;
import com.china.lhf.app.utiles.CountTimeUtile;
import com.china.lhf.app.utiles.MD5Utils;
import com.china.lhf.app.utiles.ManifestUtils;
import com.china.lhf.app.utiles.ToastUtils;
import com.china.lhf.app.widget.ClearEditText;
import com.china.lhf.app.widget.HomeToolbar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import dmax.dialog.SpotsDialog;
import okhttp3.Response;

/**
 * Created by C罗 on 2016/10/20.
 */
public class WanChengActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.toolbar_shoop_list)
    HomeToolbar homeToolbar;
    @Bind(R.id.register_phone_tv)
    TextView mphone;
    @Bind(R.id.register_phone_ce)
    ClearEditText messagecode;
    @Bind(R.id.register_code_btn)
    Button mcodebtn;

    private String phone;
    private String pwd;
    private String countrycode;

    private CountTimeUtile countTimeUtile;
    private static final String DEFAULT_COUNTRY_ID = "42";
    private SMSEvenHandler evenHandler;
    private OkHttpHelper okHttpHelper=OkHttpHelper.getInstance();
    private SpotsDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);
        ButterKnife.bind(this);

        initToolbar();

        phone=getIntent().getStringExtra("phone");
        pwd=getIntent().getStringExtra("pwd");
        countrycode=getIntent().getStringExtra("countrycode");

        String formatePhone="+"+countrycode+" "+splitPhoneNum(phone);
        String text=getString(R.string.smssdk_send_mobile_detail)+formatePhone;
        mphone.setText(Html.fromHtml(text));

        countTimeUtile=new CountTimeUtile(mcodebtn);
        countTimeUtile.start();

        String[] value= ManifestUtils.getMetaDataValueArray(this,"mob_sms_appKey","mob_sms_appSecret");

        SMSSDK.initSDK(this,value[0],value[1]);
        evenHandler=new SMSEvenHandler();
        SMSSDK.registerEventHandler(evenHandler);

        dialog=new SpotsDialog(this);
    }

    private void initToolbar() {
        homeToolbar.setmLeftButtonClickListener(this);
        homeToolbar.setmRightButtonClickListener(this);
    }

    private void submitCode(){
        String code=messagecode.getText().toString().trim();
        if(TextUtils.isEmpty(code)){
            ToastUtils.show(this,R.string.smssdk_write_identify_code);
            return;
        }
        SMSSDK.submitVerificationCode(countrycode,phone,code);
        dialog=new SpotsDialog(this,"正在效验验证码");
        dialog.show();
    }

    private String splitPhoneNum(String phone){
        StringBuffer builder=new StringBuffer(phone);
        builder.reverse();
        for (int i=4,len=builder.length();i<len;i+=5){
            builder.insert(i,' ');
        }
        builder.reverse();
        return builder.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_leftButton:
                finish();
                break;
            case R.id.toolbar_rightButton:
                submitCode();
                break;
        }
    }

    @OnClick(R.id.register_code_btn)
    public void resendCode(View view){
        SMSSDK.getVerificationCode("+"+countrycode,phone);
        countTimeUtile=new CountTimeUtile(mcodebtn);
        countTimeUtile.start();
        dialog=new SpotsDialog(this,"正在重新获取验证码");
        dialog.show();
    }


    class SMSEvenHandler extends EventHandler {
        @Override
        public void afterEvent(final int event, final int result, final Object data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(dialog!=null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //回调完成
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            //提交验证码成功
                            HashMap<String,Object> phoneMap= (HashMap<String, Object>) data;
                            String country= (String) phoneMap.get("country");
                            String phone= (String) phoneMap.get("phone");
                            ToastUtils.show(WanChengActivity.this,"验证成功"+phone+country);

                            doReg();
                        }
                    } else {
                        try {
                            Throwable throwable = (Throwable) data;
                            Throwable resId = (Throwable) data;
                            JSONObject object = new JSONObject(resId.getMessage());
                            String des = object.optString("detail");
                            int status = object.optInt("status");
                            if (status > 0 && !TextUtils.isEmpty(des)) {
                                ToastUtils.show(WanChengActivity.this, des);
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }
                    }
                }
            });
        }
    }

    private void doReg(){
        Map<String, Object> params = new HashMap<>(2);
        params.put("phone", phone);
        params.put("password", MD5Utils.ecoder(pwd));

        okHttpHelper.post(Contants.API.REG, params, new SpostsCallback<LoginRespMsg<User>>(this) {

            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {
                if(userLoginRespMsg.getStatus()==LoginRespMsg.STATUS_ERROR){
                    ToastUtils.show(WanChengActivity.this,"注册失败"+userLoginRespMsg.getMessage());
                    Log.e("WanChengActivity","注册失败"+userLoginRespMsg.getMessage());
                    return;
                }
                MyApplication application = MyApplication.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());
                startActivity(new Intent(WanChengActivity.this,MainActivity.class));
                finish();
            }

            @Override
            public void onErroe(Response response, int code, Exception e) {
                Log.e("WanChengActivity",response.message().toString()+",code"+code);
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(evenHandler);
    }

}
