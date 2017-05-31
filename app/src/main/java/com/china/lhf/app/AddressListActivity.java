package com.china.lhf.app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.china.lhf.app.adapter.AddressAdapter;
import com.china.lhf.app.adapter.decoration.DividerItemDecoration;
import com.china.lhf.app.application.MyApplication;
import com.china.lhf.app.entity.Address;
import com.china.lhf.app.http.Contants;
import com.china.lhf.app.http.OkHttpHelper;
import com.china.lhf.app.http.SpostsCallback;
import com.china.lhf.app.msg.BaseRespMsg;
import com.china.lhf.app.widget.HomeToolbar;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * Created by C罗 on 2016/10/26.
 */
public class AddressListActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.address_list_toolbar)
    HomeToolbar addrToolbar;
    @Bind(R.id.address_list_rl)
    RecyclerView addrListRL;

    private AddressAdapter addrAdapter;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ButterKnife.bind(this);
        initToolbar();
        initAddress();
    }


    private void initToolbar() {
        addrToolbar.setTitle("收货地址");
        addrToolbar.setmLeftButtonClickListener(this);
        addrToolbar.setmRightButtonClickListener(this);
    }

    private void initAddress() {
        Map<String, Object> params = new HashMap<>(1);
        params.put("user_id", MyApplication.getInstance().getUser().getId());
        okHttpHelper.post(Contants.API.ADDR_LIST, params,
                new SpostsCallback<List<Address>>(this) {
                    @Override
                    public void onSuccess(Response response, List<Address> addresses) {
                        showAddress(addresses);
                    }

                    @Override
                    public void onErroe(Response response, int code, Exception e) {
                    }
                });
    }

    private void showAddress(List<Address> addresses) {

        Collections.sort(addresses);    //对地址进行排序
        if (addresses.size() >= 5) {
            addrToolbar.getmRightButton().setVisibility(View.INVISIBLE);
        } else {
            addrToolbar.getmRightButton().setVisibility(View.VISIBLE);
        }
        if (addrAdapter == null) {
            addrAdapter = new AddressAdapter(this, addresses, new AddressAdapter.AddressListener() {
                        @Override
                        public void setDefault(Address address) {
                            updateAddress(address);
                        }

                        @Override
                        public void deleteAddress(Long id) {
                            showAlertDialog("删除收货地址", "您确定要删除吗？", new String[]{"取消", "确定"}, true, true, id);
                        }

                        @Override
                        public void editAddress(Address address) {
                            Log.e("AddressListActivity", "------updateAddress---IsDefault" +
                                    address.getIsDefault() + ", consignee" +
                                    address.getConsignee());
                            toAddActivity(address);
                        }
                    });
            addrListRL.setAdapter(addrAdapter);
            addrListRL.setLayoutManager(new LinearLayoutManager(AddressListActivity.this));
            addrListRL.addItemDecoration(new DividerItemDecoration(this,
                    DividerItemDecoration.HORIZONTAL_LIST));
        } else {
            addrAdapter.refreshData(addresses);
        }
    }

    public void updateAddress(Address address) {
        Log.e("AddressListActivity", "------updateAddress---IsDefault" + address.getIsDefault() + ", consignee" + address.getConsignee());
        Map<String, Object> params = new HashMap<>(6);
        params.put("user_id", address.getId());
        params.put("consignee", address.getConsignee());
        params.put("phone", address.getPhone());
        params.put("addr", address.getAddr());
        params.put("zip_code", address.getZipCode() == null ? "000000" : address.getZipCode());
        params.put("is_default", address.getIsDefault());
        okHttpHelper.post(Contants.API.ADDR_UPDATE, params, new
                SpostsCallback<BaseRespMsg>(this) {

                    @Override
                    public void onSuccess(Response response, BaseRespMsg baseResponseMsg) {
                        if (baseResponseMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS) {
                            initAddress();
                        }
                    }

                    @Override
                    public void onErroe(Response response, int code, Exception e) {
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_leftButton:
                finish();
                break;
            case R.id.toolbar_rightButton:
                toAddActivity(null);
                break;
        }
    }

    @Override
    public void onButtonClicked(Dialog dialog, int position, Object tag) {
        if (position == 1) { //点击了确定按钮
            deleteAddrById((Long) tag);
        }
    }

    private void deleteAddrById(Long id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);

        okHttpHelper.post(Contants.API.ADDR_DEL, params,
                new SpostsCallback<BaseRespMsg>(this, "狠心删除...") {
                    @Override
                    public void onSuccess(Response response, BaseRespMsg baseResponseMsg) {
                        if (baseResponseMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS) {
                            initAddress();
                        }
                    }

                    @Override
                    public void onErroe(Response response, int code, Exception e) {
                    }
                });
    }

    private void toAddActivity(Address address) {
        Intent intent = new Intent(this, CreateAddressActivity.class);
        if (address != null) {
            //-----------
            intent.putExtra("address", address);
        }
        startActivityForResult(intent, Contants.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initAddress();
    }
}
