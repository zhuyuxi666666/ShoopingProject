package com.china.lhf.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.china.lhf.app.application.MyApplication;
import com.china.lhf.app.entity.User;
import com.china.lhf.app.http.Contants;
import com.china.lhf.app.http.OkHttpHelper;
import com.china.lhf.app.http.SpostsCallback;
import com.china.lhf.app.msg.LoginRespMsg;
import com.china.lhf.app.utiles.MD5Utils;
import com.china.lhf.app.utiles.ToastUtils;
import com.china.lhf.app.widget.ClearEditText;
import com.china.lhf.app.widget.HomeToolbar;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * Created by C罗 on 2016/10/13.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.toolbar_shoop_list)
    HomeToolbar mhomeToolbar;
    @Bind(R.id.login_name)
    ClearEditText mname;
    @Bind(R.id.login_pass)
    ClearEditText mpass;
    @Bind(R.id.login_btn)
    Button mlogin;
    @Bind(R.id.register_txt)
    TextView register;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);

        initView();
        initToobar();
    }

    private void initView() {
        register.setOnClickListener(this);
        mlogin.setOnClickListener(this);
    }

    private void initToobar() {
        mhomeToolbar.setmLeftButtonClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_leftButton:
                this.finish();
                break;
            case R.id.login_btn:
                String phone = mname.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.show(this, "请输入手机号码");
                    return;
                }
                String pwd = mpass.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.show(this, "请输入密码");
                    return;
                }
                Map<String, Object> params = new HashMap<>(2);
                params.put("phone", phone);
                params.put("password", MD5Utils.ecoder(pwd));

                okHttpHelper.post(Contants.API.LOGIN, params, new SpostsCallback<LoginRespMsg<User>>(this) {

                    @Override
                    public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {
                        MyApplication application = MyApplication.getInstance();
                        application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());
                        if (application.getIntent() == null) {
                            setResult(RESULT_OK);
                        } else {
                            application.jumpToTargetActivity(LoginActivity.this);
                        }
                        finish();
                    }

                    @Override
                    public void onErroe(Response response, int code, Exception e) {
                        super.onErroe(response, code, e);
                    }
                });
                break;
            case R.id.register_txt:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
