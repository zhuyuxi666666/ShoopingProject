package com.china.lhf.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.china.lhf.app.utiles.ManifestUtils;
import com.china.lhf.app.utiles.ToastUtils;
import com.china.lhf.app.widget.ClearEditText;
import com.china.lhf.app.widget.HomeToolbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.country_id)
    TextView countryid;
    @Bind(R.id.register_phone_ed)
    ClearEditText mphone;
    @Bind(R.id.register_pass_ed)
    ClearEditText mpass;
    @Bind(R.id.register_country_tv)
    TextView mcountry;
    @Bind(R.id.toolbar_shoop_list)
    HomeToolbar homeToolbar;


    private static final String DEFAULT_COUNTRY_ID = "42";
    private SMSEvenHandler evenHandler;
    private SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        initToolbar();

        String[] value= ManifestUtils.getMetaDataValueArray(this,"mob_sms_appKey","mob_sms_appSecret");

        SMSSDK.initSDK(this,value[0],value[1]);
        evenHandler=new SMSEvenHandler();
        SMSSDK.registerEventHandler(evenHandler);

        String[] country=getCurrentCountry();
        if(country!=null){
            countryid.setText("+"+country[1]);
            mcountry.setText(country[0]);
        }

        SMSSDK.getSupportedCountries();

        dialog=new SpotsDialog(this);



    }

    private void initToolbar() {
        homeToolbar.setmRightButtonClickListener(this);
        homeToolbar.setmLeftButtonClickListener(this);
    }

    private String[] getCurrentCountry() {
        String mcc = this.getMCC();
        String[] country = null;
        if(!TextUtils.isEmpty(mcc)) {
            country = SMSSDK.getCountryByMCC(mcc);
        }

        if(country == null) {
            Log.w("SMSSDK", "no country found by MCC: " + mcc);
            country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        }

        return country;
    }

    private String getMCC() {
        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = tm.getNetworkOperator();
        return !TextUtils.isEmpty(networkOperator)?networkOperator:tm.getSimOperator();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_leftButton:
                finish();
                break;
            case R.id.toolbar_rightButton:
                getCode();
                break;
        }
    }

    private void getCode() {
         String phone=mphone.getText().toString().trim().replace("\\s*","");//  \表示字符 \s表示空格
        String code=countryid.getText().toString().trim();
        checkPhoneNum(phone,code);
        SMSSDK.getVerificationCode(code, phone);

        dialog=new SpotsDialog(this,"正在获取验证码");
        dialog.show();
    }
//检查手机号码输入的正确性
    private void checkPhoneNum(String phone,String code){
        if(code.startsWith("+")){
            code=code.substring(1);
        }
        if(TextUtils.isEmpty(phone)){
            ToastUtils.show(this,"请输入手机号码");
            return;
        }
        if("86".equals(code)){
            if(phone.length()!=11){
                ToastUtils.show(this,"手机位数有误");
                return;
            }
        }
        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p=Pattern.compile(rule);
        Matcher m=p.matcher(phone);
        if(!m.matches()){
            ToastUtils.show(this,"您输入的手机号码格式不正确");
            return;
        }
    }

    private void onCountryListGot(ArrayList<HashMap<String,Object>> data){
        for(HashMap<String,Object> country:data){
            String code= (String) country.get("zone");
            String rule= (String) country.get("rule");
            if(TextUtils.isEmpty(code)||TextUtils.isEmpty(rule)){
                continue;
            }
            Log.e("RegActivity","code="+code+",rule="+rule);
        }
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
                            ToastUtils.show(RegisterActivity.this, "提交成功");
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //获取验证码成功
                            afterVerificationCodeRequested((Boolean) data);
                        } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            //返回支持发送验证码的国家列表
                            onCountryListGot((ArrayList<HashMap<String,Object>>) data);
                        }
                    } else {
                        try {
                            Throwable throwable = (Throwable) data;
                            Throwable resId = (Throwable) data;
                            JSONObject object = new JSONObject(resId.getMessage());
                            String des = object.optString("detail");
                            int status = object.optInt("status");
                            if (status > 0 && !TextUtils.isEmpty(des)) {
                                ToastUtils.show(RegisterActivity.this, des);
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

    private void afterVerificationCodeRequested(boolean smart) {
        ToastUtils.show(this, "获取验证码成功");
        String phone = mphone.getText().toString().trim().replaceAll("\\s*", "");
        String pwd=mpass.getText().toString().trim();
        String code = countryid.getText().toString().trim();
        if(code.startsWith("+")) {
            code = code.substring(1);
        }

        Intent intent=new Intent(this,WanChengActivity.class);
        intent.putExtra("phone",phone);
        intent.putExtra("pwd",pwd);
        intent.putExtra("countrycode",code);
        startActivity(intent);
        finish();



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(evenHandler);
    }


}
