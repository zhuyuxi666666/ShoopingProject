package com.china.lhf.app.utiles;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.china.lhf.app.R;

/**
 * Created by C罗 on 2016/10/20.
 */
public class CountTimeUtile extends CountDownTimer {
    public static final int TIME_COUNT = 61000;
    private TextView tv;
    private int endStrRid;

    /**
     * @param millisInFuture    倒计时总时间
     * @param countDownInterval 渐变时间（每次倒计一秒）
     * @param tv                点击的按钮
     * @param endStrRid         倒计时结束后  按钮显示的文字
     */
    public CountTimeUtile(long millisInFuture
            , long countDownInterval, TextView tv, int endStrRid) {
        super(millisInFuture, countDownInterval);
        this.tv = tv;
        this.endStrRid = endStrRid;
    }

    public CountTimeUtile(TextView tv, int endStrRid) {
        this(TIME_COUNT, 1000, tv, endStrRid);
    }

    public CountTimeUtile(TextView tv) {
        this(TIME_COUNT, 1000, tv, R.string.smssdk_resend_identify_code);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        tv.setEnabled(false);
        tv.setText(millisUntilFinished / 1000 + "秒后可重新发送");
    }

    @Override//计时完毕时触发
    public void onFinish() {
        tv.setEnabled(true);
        tv.setText(endStrRid);
    }

}
