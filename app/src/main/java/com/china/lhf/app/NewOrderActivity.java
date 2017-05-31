package com.china.lhf.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.china.lhf.app.adapter.CreateOrderAdapter;
import com.china.lhf.app.application.MyApplication;
import com.china.lhf.app.entity.ShoppingCart;
import com.china.lhf.app.entity.WareItem;
import com.china.lhf.app.http.Contants;
import com.china.lhf.app.http.OkHttpHelper;
import com.china.lhf.app.http.SpostsCallback;
import com.china.lhf.app.msg.BaseRespMsg;
import com.china.lhf.app.msg.CreateOrderRespMag;
import com.china.lhf.app.utiles.CartProvider;
import com.china.lhf.app.utiles.FullyLinearLayoutManager;
import com.china.lhf.app.utiles.GsonUtils;
import com.china.lhf.app.utiles.ToastUtils;
import com.china.lhf.app.widget.HomeToolbar;
import com.pingplusplus.android.Pingpp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

public class NewOrderActivity extends BaseActivity implements View.OnClickListener{

    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 百度支付渠道
     */
    private static final String CHANNEL_BFB = "bfb";

    @Bind(R.id.toolbar_shoop_list)
    HomeToolbar homeToolbar;
    @Bind(R.id.alipay_radio)
    RadioButton alipay_radio;
    @Bind(R.id.weixin_radio)
    RadioButton weixin_radio;
    @Bind(R.id.baidu_radio)
    RadioButton baidu_radio;
    @Bind(R.id.commit_btn)
    Button commitbtn;//提交订单按钮
    @Bind(R.id.money_tv)
    TextView money;//应付金额显示
    @Bind(R.id.alipay_rl)
    RelativeLayout alipay_rl;
    @Bind(R.id.weixin_rl)
    RelativeLayout weixin_rl;
    @Bind(R.id.baidu_rl)
    RelativeLayout daidu_rl;
    @Bind(R.id.createorder_rv)
    RecyclerView mRecyclerView;


    private OkHttpHelper okHttpHelper=OkHttpHelper.getInstance();
    private CartProvider cartProvider;
    private CreateOrderAdapter mAdapter;
    private String orderNum;
    private float amount;

    private HashMap<String, RadioButton> hashMap = new HashMap<>(3);
    private String payChannle = CHANNEL_ALIPAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        ButterKnife.bind(this);

        initTooBar();
        showData();
        init();
    }

    private void init() {
        hashMap.put(CHANNEL_ALIPAY, alipay_radio);
        hashMap.put(CHANNEL_WECHAT, weixin_radio);
        hashMap.put(CHANNEL_BFB, baidu_radio);
        alipay_rl.setOnClickListener(this);
        weixin_rl.setOnClickListener(this);
        daidu_rl.setOnClickListener(this);
        amount = mAdapter.getTotalPice();
        money.setText("应付款: ￥" + amount);
    }

    private void showData() {
        cartProvider =CartProvider.getInstance(this);
        mAdapter = new CreateOrderAdapter(this, cartProvider.getAll());
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this);
        linearLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void selectPayChannle(String payChannle) {
        this.payChannle = payChannle;
        for (Map.Entry<String, RadioButton> entry : hashMap.entrySet()) {
            RadioButton rb = entry.getValue();
            if (entry.getKey().equals(payChannle)) {
                boolean isCheck = rb.isChecked();
                rb.setChecked(!isCheck);
            } else {
                rb.setChecked(false);
            }
        }
    }

    private void initTooBar() {
        homeToolbar.setmLeftButtonClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.toolbar_leftButton) {
                finish();
        }else {
            selectPayChannle(v.getTag().toString());
        }
    }

    @OnClick(R.id.commit_btn)
    public void createNewOrder(View view) {
        postNewOrder();
    }

    private void postNewOrder() {
        List<ShoppingCart> cars = mAdapter.getDatas();
        List<WareItem> items = new ArrayList<>(cars.size());
        for (ShoppingCart c : cars) {
            WareItem wareItem = new WareItem(c.getId(), c.getPrice().intValue());
            items.add(wareItem);
        }
        String item_json = GsonUtils.toJson(items);
        Map<String, Object> map = new HashMap<>(5);
        map.put("user_id", MyApplication.getInstance().getUser().getId());
        map.put("item_json", item_json);
        map.put("amount", (int) (amount * 100));
        map.put("addr_id", 2);
        map.put("pay_channel", payChannle);
        commitbtn.setEnabled(false);
        okHttpHelper.post(Contants.API.ORDER_CREATE, map, new SpostsCallback<CreateOrderRespMag>(this) {

            @Override
            public void onSuccess(Response response, CreateOrderRespMag createOrderRespMag) {
                cartProvider.clear();
                if(createOrderRespMag.getStatus()==1) {
                    orderNum = createOrderRespMag.getData().getOrderNum();
                    String charge = createOrderRespMag.getData().getCharge();
                    Pingpp.createPayment(NewOrderActivity.this,charge);
                    Log.e("------>>", "charge: "+charge );
                }else {
                    commitbtn.setEnabled(true);
                    ToastUtils.show(NewOrderActivity.this,createOrderRespMag.getMessage());
                    Log.e("------>>", "orderNum: "+orderNum );
                }
            }

            @Override
            public void onErroe(Response response, int code, Exception e) {
                commitbtn.setEnabled(true);
                e.printStackTrace();
            }

            @Override
            public void onTokenError(Response response, int code) {
                super.onTokenError(response, code);
                commitbtn.setEnabled(true);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");

                // 处理返回值
                // "success" - 支付成功
                // "fail"    - 支付失败
                // "cancel"  - 取消支付
                // "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
                if(result.equals("success")){
                    changeorderstatus(1);
                }else if(result.equals("fail")){
                    changeorderstatus(-1);
                }else if(result.equals("cancel")){
                    changeorderstatus(-2);
                }else {
                    changeorderstatus(0);
                }
//                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
//                showMsg(result, errorMsg, extraMsg);
            }
        }
    }

    private void changeorderstatus(final int status){
        Map<String,Object> params=new HashMap<>(2);
        params.put("order_num",orderNum);
        params.put("status",status);
        okHttpHelper.post(Contants.API.ORDER_COMPLETE, params, new SpostsCallback<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                   toPayResultActivity(status);
            }

            @Override
            public void onErroe(Response response, int code, Exception e) {
                   toPayResultActivity(-1);
            }
        });
    }

    private void toPayResultActivity(int status){
        Intent intent=new Intent(NewOrderActivity.this,PatResultActivity.class);
        intent.putExtra("status",status);
        startActivity(intent);
        finish();
    }
}
