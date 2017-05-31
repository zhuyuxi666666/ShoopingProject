package com.china.lhf.app;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.china.lhf.app.adapter.BaseAdapter;
import com.china.lhf.app.adapter.MyOrderAdapter;
import com.china.lhf.app.adapter.decoration.CardViewItemDecoration;
import com.china.lhf.app.application.MyApplication;
import com.china.lhf.app.entity.Order;
import com.china.lhf.app.http.Contants;
import com.china.lhf.app.http.OkHttpHelper;
import com.china.lhf.app.http.SpostsCallback;
import com.china.lhf.app.widget.HomeToolbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * 订单页
 */
public class MyOrderActivity extends BaseActivity implements View.OnClickListener,
        TabLayout.OnTabSelectedListener {

    public static final int STATUS_ALL = 1000;  //全部订单
    public static final int STATUS_PAY_SUCCESS = 1; //支付成功的订单
    public static final int STATUS_PAY_FAIL = 2;    //支付失败的订单
    public static final int STATUS_PAY_WAIT = 0;    //待支付的订单
    private int status = STATUS_ALL;

    @Bind(R.id.my_order_toolbar)
    HomeToolbar mOrderToolbar;
    @Bind(R.id.my_order_tl)
    TabLayout mOrderTL;
    @Bind(R.id.my_order_rv)
    RecyclerView mOrderRV;

    private MyOrderAdapter mOrderAdapter;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        initToolbar();
        initOrderTab();
    }


    private void initToolbar() {
        mOrderToolbar.setTitle("我的订单");
        mOrderToolbar.setmLeftButtonClickListener(this);
    }

    private void initOrderTab() {
        TabLayout.Tab tab = mOrderTL.newTab();
        tab.setText(R.string.text_order_all);
        tab.setTag(STATUS_ALL);
        mOrderTL.addTab(tab);

        tab = mOrderTL.newTab();
        tab.setText(R.string.text_order_success);
        tab.setTag(STATUS_PAY_SUCCESS);
        mOrderTL.addTab(tab);

        tab = mOrderTL.newTab();
        tab.setText(R.string.text_order_prepare);
        tab.setTag(STATUS_PAY_WAIT);
        mOrderTL.addTab(tab);

        tab = mOrderTL.newTab();
        tab.setText(R.string.text_order_fail);
        tab.setTag(STATUS_PAY_FAIL);
        mOrderTL.addTab(tab);
        mOrderTL.setOnTabSelectedListener(this);
    }

    private void getOrders() {
        Long userId = MyApplication.getInstance().getUser().getId();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("status", status);
        okHttpHelper.post(Contants.API.ORDER_LIST, params, new SpostsCallback<List<Order>>(this) {
            @Override
            public void onSuccess(Response response, List<Order> orders) {
                showOrders(orders);
            }

            @Override
            public void onErroe(Response response, int code, Exception e) {
                Log.e("MyOrderActivity", "------code" + code);
            }
        });
    }

    private void showOrders(List<Order> orders) {
        if (mOrderAdapter == null) {
            mOrderAdapter = new MyOrderAdapter(this, orders);
            mOrderRV.setAdapter(mOrderAdapter);
            mOrderRV.setLayoutManager(new LinearLayoutManager(this));
            mOrderRV.addItemDecoration(new CardViewItemDecoration());
            mOrderAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    toDetialActivity(position);
                }
            });
        } else {
            mOrderAdapter.refreshData(orders);
            mOrderRV.setAdapter(mOrderAdapter);
        }
    }

    private void toDetialActivity(int positon) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_leftButton:
                finish();
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        status = (int) tab.getTag();
        getOrders();
    }


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
