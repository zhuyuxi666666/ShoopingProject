package com.china.lhf.app;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.china.lhf.app.application.MyApplication;
import com.china.lhf.app.city.CityModel;
import com.china.lhf.app.city.DistrictModel;
import com.china.lhf.app.city.ProvinceModel;
import com.china.lhf.app.entity.Address;
import com.china.lhf.app.http.Contants;
import com.china.lhf.app.http.OkHttpHelper;
import com.china.lhf.app.http.SpostsCallback;
import com.china.lhf.app.msg.BaseRespMsg;
import com.china.lhf.app.utiles.XmlParseHandler;
import com.china.lhf.app.widget.ClearEditText;
import com.china.lhf.app.widget.HomeToolbar;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * 新建收件地址
 */
public class CreateAddressActivity extends BaseActivity
        implements View.OnClickListener {

    @Bind(R.id.create_addr_toolbar)
    HomeToolbar addAddrToolbar;
    @Bind(R.id.recipient_name_cet)
    ClearEditText recipientCET;
    @Bind(R.id.phone_num_cet)
    ClearEditText phoneNumCET;
    @Bind(R.id.addr_cet)
    ClearEditText addrCET;
    @Bind(R.id.contact_addr_tv)
    TextView contactAddrTV;

    private OptionsPickerView pvOptions;
    private List<ProvinceModel> mProvinceModels;
    private ArrayList<String> mProvinces = new ArrayList<String>();
    private ArrayList<ArrayList<String>> mCities = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> mDistricts = new ArrayList<ArrayList<ArrayList<String>>>();
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private Address address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_address);
        ButterKnife.bind(this);
        address = (Address) getIntent().getSerializableExtra("address");
        initAddrToolbar();
        initAddrData();
    }


    private void initAddrToolbar() {
        addAddrToolbar.setTitle("新建收货地址");
        addAddrToolbar.setmLeftButtonClickListener(this);
        addAddrToolbar.setmRightButtonClickListener(this);
        //新建或修改收货地址
        if (address == null) {
            addAddrToolbar.setTitle(getString(R.string.add_or_edit_addresses,
                    new Object[]{getString(R.string.add)}));
        } else {
            addAddrToolbar.setTitle(getString(R.string.add_or_edit_addresses,
                    new Object[]{getString(R.string.edit)}));
        }
    }

    private void initAddrData() {
        initProvinceData();

        pvOptions = new OptionsPickerView(this);      //选项选择器
        //三级联动效果
        pvOptions.setPicker(mProvinces, mCities, mDistricts, true);
        pvOptions.setTitle("选择城市");     //设置标题
        pvOptions.setCyclic(false, false, false);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String address = mProvinces.get(options1) + "  " +
                        mCities.get(options1).get(option2) + "  "
                        + mDistricts.get(options1).get(option2).get(options3);
                contactAddrTV.setText(address);
            }
        });

        if (address != null) {
            recipientCET.setText(address.getConsignee());
            phoneNumCET.setText(address.getPhone());
            String[] allAddress = address.getAddr().split("  ");
            int a = mProvinces.indexOf(allAddress[0]);
            int b = mCities.get(a).indexOf(allAddress[1]);
            int c = mDistricts.get(a).get(b).indexOf(allAddress[2]);
            pvOptions.setSelectOptions(a, b, c);
            addrCET.setText(allAddress[3]);
            contactAddrTV.setText(allAddress[0] + "  " + allAddress[1] + "  " + allAddress[2]);
        }
    }

    protected void initProvinceData() {
        AssetManager assetManager = getAssets();
        try {
            InputStream ism = assetManager.open("province_data.xml");
            //创建解析Xml的工厂对象
            SAXParserFactory factory = SAXParserFactory.newInstance();
            //解析Xml
            SAXParser parser = factory.newSAXParser();
            XmlParseHandler handler = new XmlParseHandler();
            parser.parse(ism, handler);
            ism.close();
            //获取解析出来的数据
            mProvinceModels = handler.getDataList();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }

        if (mProvinceModels != null) {
            for (ProvinceModel p : mProvinceModels) {
                mProvinces.add(p.getName());
                List<CityModel> cities = p.getCityList();
                ArrayList<String> cityStrs = new ArrayList<>(cities.size());    //城市名
                ArrayList<ArrayList<String>> dts = new ArrayList<>(cities.size());
                for (CityModel c : cities) {
                    cityStrs.add(c.getName());  //把城市数据放入cityStrs
                    List<DistrictModel> districts = c.getDistrictList();
                    ArrayList<String> districtStrs = new ArrayList<>(districts.size());
                    for (DistrictModel d : districts) {
                        districtStrs.add(d.getName());  //把城市数据放入districtStrs
                    }
                    dts.add(districtStrs);
                }
                mDistricts.add(dts);
                mCities.add(cityStrs);   //组装城市数据
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_leftButton:
                finish();
                break;
            case R.id.toolbar_rightButton:
                if (address == null) {
                    createAddress();    //添加地址
                } else {
                    updateAddress();
                }
                break;
        }
    }

    @OnClick(R.id.addr_add_ll)
    public void showCityPickerView(View view) {
        pvOptions.show();
    }

    public void createAddress() {
        String consignee = recipientCET.getText().toString();
        String phone = phoneNumCET.getText().toString();
        String address = contactAddrTV.getText().toString() + "  " +
                addrCET.getText().toString();

        Map<String, Object> params = new HashMap<>(1);
        params.put("user_id", MyApplication.getInstance().getUser().getId());
        params.put("consignee", consignee);
        params.put("phone", phone);
        params.put("addr", address);
        params.put("zip_code", "000000");

        okHttpHelper.post(Contants.API.ADDR_CREATE, params, new
                SpostsCallback<BaseRespMsg>(this, "加速加载信息中") {

                    @Override
                    public void onSuccess(Response response, BaseRespMsg baseResponseMsg) {
                        if (baseResponseMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                    @Override
                    public void onErroe(Response response, int code, Exception e) {
                    }
                });
    }

    private void updateAddress() {
        String consignee = recipientCET.getText().toString();
        String phone = phoneNumCET.getText().toString();
        String addr = contactAddrTV.getText().toString() + "  " + addrCET.getText().toString();

        Map<String, Object> params = new HashMap<>(6);
        params.put("id", address.getId());
        params.put("consignee", consignee);
        params.put("phone", phone);
        params.put("addr", addr);
        params.put("zip_code", address.getZipCode() == null ? "000000" : address.getZipCode());
        params.put("is_default", address.getIsDefault());

        okHttpHelper.post(Contants.API.ADDR_UPDATE, params, new
                SpostsCallback<BaseRespMsg>(this) {

                    @Override
                    public void onSuccess(Response response, BaseRespMsg baseResponseMsg) {
                        if (baseResponseMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                    @Override
                    public void onErroe(Response response, int code, Exception e) {
                        super.onErroe(response, code, e);
                    }
                });
    }

}
