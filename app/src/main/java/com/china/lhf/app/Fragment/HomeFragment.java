package com.china.lhf.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.china.lhf.app.R;
import com.china.lhf.app.WareListActivity;
import com.china.lhf.app.adapter.HomeCategoryAdapter;
import com.china.lhf.app.adapter.decoration.CardViewItemDecoration;
import com.china.lhf.app.adapter.decoration.DividerItemDecoration;
import com.china.lhf.app.entity.Banner;
import com.china.lhf.app.entity.Campaign;
import com.china.lhf.app.entity.HomeCampaign;
import com.china.lhf.app.entity.HomeCategory;
import com.china.lhf.app.http.BaseCallback;
import com.china.lhf.app.http.Contants;
import com.china.lhf.app.http.OkHttpHelper;
import com.china.lhf.app.http.SpostsCallback;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 主页的fragment
 *
 */
public class HomeFragment extends Fragment {

    private static final  String TAG ="HomeFragment";
    private SliderLayout mSliderLayout;
    private PagerIndicator mIndicator;
    private RecyclerView mRecyclerView;
    private HomeCategoryAdapter mAdapter;
    private Gson mGson=new Gson();
    private List<Banner> mBanners;
    private OkHttpHelper okHttpHelper=OkHttpHelper.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        mSliderLayout= (SliderLayout) view.findViewById(R.id.slider);
        mIndicator= (PagerIndicator) view.findViewById(R.id.custom_indicator);
        requestImages();
        //initSlider();
        iniRecyclerview(view);

        return view;

    }

    private void requestImages(){
        String url= Contants.API.BANNER_URL+"?type=1";
//        String url="http://101.200.167.75:8080/phoenixshop/banner/query";
        okHttpHelper.get(url, new SpostsCallback<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanners=banners;
                initSlider();
            }

            @Override
            public void onErroe(Response response, int code, Exception e) {
            }
        });


//        OkHttpClient client=new OkHttpClient();
//        RequestBody body=new FormBody.Builder().add("type","1").build();
//        Request request=new Request.Builder().url(url).post(body).build();
//        // Request request=new Request.Builder().url(url).build();
//        client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                        Toast.makeText(getActivity(),"访问网络失败",Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    if(response.isSuccessful()){
//                        final String json=response.body().string();
//
//                        Type type=new TypeToken<List<Banner>>(){}.getType();
//                        mBanners=mGson.fromJson(json,type);
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.e(TAG, "数据："+json);
//                                initSlider();
//                            }
//                        });
//                    }
//                }
//            });

    }

    private void iniRecyclerview(View view) {
        mRecyclerView= (RecyclerView) view.findViewById(R.id.home_iv);
        okHttpHelper.get(Contants.API.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>() {
            @Override
            public void onRequestBefore(Request request) {
            }

            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                   initData(homeCampaigns);
            }

            @Override
            public void onErroe(Response response, int code, Exception e) {
            }

            @Override
            public void onTokenError(Response response, int code) {

            }

            @Override
            public void onResponse(Response response) {
            }
        });
//        List<HomeCategory> datas=new ArrayList<>();
//        HomeCategory category=new HomeCategory("热门活动",R.drawable.img_big_1,R.drawable.img_1_small1,R.drawable.img_1_small2);
//        datas.add(category);
//        category=new HomeCategory("有利可图",R.drawable.img_big_4,R.drawable.img_4_small1,R.drawable.img_4_small2);
//        datas.add(category);
//        category=new HomeCategory("品牌街",R.drawable.img_big_2,R.drawable.img_2_small1,R.drawable.img_2_small2);
//        datas.add(category);
//        category=new HomeCategory("金融街  包赚翻",R.drawable.img_big_3,R.drawable.img_3_small1,R.drawable.img_3_small2);
//        datas.add(category);
//        category=new HomeCategory("超值购",R.drawable.img_big_0,R.drawable.img_0_small1,R.drawable.img_0_small2);
//        datas.add(category);
//
//        mAdapter=new HomeCategoryAdapter(datas);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration());
    }

    private void initData(List<HomeCampaign> homeCampaigns) {
        mAdapter=new HomeCategoryAdapter(getActivity(),homeCampaigns);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerView.addItemDecoration(new CardViewItemDecoration());

        mAdapter.setOnCampaignClickListener(new HomeCategoryAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {
                Toast.makeText(getActivity(),"title+++"+campaign.getTitle(),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getActivity(), WareListActivity.class);
                intent.putExtra(Contants.CAMPAIGN_ID,campaign.getId());
                startActivity(intent);
            }
        });
    }

    public void initSlider(){
//        TextSliderView textSliderView1 = new TextSliderView(getActivity());
//        textSliderView1.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//            @Override
//            public void onSliderClick(BaseSliderView slider) {
//                Toast.makeText(getActivity(),"新品推荐！",Toast.LENGTH_LONG).show();
//            }
//        });
//        textSliderView1
//                .description("新品推荐")
//                .image("http://101.200.167.75:8080/phoenixshop/img/banner/55e6d1b9Ne6fd6d8f.jpg")
//                .setScaleType(BaseSliderView.ScaleType.Fit);
//
//        TextSliderView textSliderView2 = new TextSliderView(getActivity());
//        textSliderView2.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//            @Override
//            public void onSliderClick(BaseSliderView slider) {
//                Toast.makeText(getActivity(),"时尚男装！",Toast.LENGTH_LONG).show();
//            }
//        });
//        textSliderView2
//                .description("时尚男装")
//                .image("http://101.200.167.75:8080/phoenixshop/img/banner/558d2fbaNb3c2f349.jpg")
//                .setScaleType(BaseSliderView.ScaleType.Fit);
//
//        TextSliderView textSliderView3 = new TextSliderView(getActivity());
//        textSliderView3.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//            @Override
//            public void onSliderClick(BaseSliderView slider) {
//                Toast.makeText(getActivity(),"家电秒杀！",Toast.LENGTH_LONG).show();
//            }
//        });
//        textSliderView3
//                .description("家电秒杀")
//                .image("http://101.200.167.75:8080/phoenixshop/img/banner/55dd271aN49efd216.jpg")
//                .setScaleType(BaseSliderView.ScaleType.Fit);
//
//        mSliderLayout.addSlider(textSliderView1);
//        mSliderLayout.addSlider(textSliderView2);
//        mSliderLayout.addSlider(textSliderView3);

        for (Banner banner:mBanners){
            TextSliderView textSliderView1 = new TextSliderView(getActivity());
                 textSliderView1
                .description(banner.getName())
                .image(banner.getImgUrl())
                .setScaleType(BaseSliderView.ScaleType.Fit);
            mSliderLayout.addSlider(textSliderView1);
        }

        //设置默认指示器，在底部中央，样式为三个圆点
        //mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderLayout.setCustomIndicator(mIndicator);
        //设置自定义动画   文字动画由下向上冒出来
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        //Fade为平面效果  这可以设置图片轮播样式
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        mSliderLayout.setDuration(3000);

        mSliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.e(TAG, "onPageScrolled滚动中调用" );
            }

            @Override
            public void onPageSelected(int position) {
                //一个信页卡被调用时调用  仍未原来
               // Log.e(TAG, "onPageSelected点击时调用" );
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Log.e(TAG, "onPageScrollStateChanged滚动状态改变时调用  开始滚动 停止滚动" );
            }
        });
    }

    @Override
    public void onStart() {
        mSliderLayout.startAutoCycle();
        super.onStart();
    }

    //绑定生命周期
    @Override
    public void onStop() {
        mSliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(hidden){
            mSliderLayout.stopAutoCycle();
        }else{
            mSliderLayout.startAutoCycle();
        }
        super.onHiddenChanged(hidden);
    }

    //测试frece的代码
    public void frescotest(){
//        Uri uri = Uri.parse("http://android-artworks.25pp.com/fs01/2014/09/05/102_12a82453c0ba192f5b11892ba47ae2b7.png");
//        SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.my_image_view);
//        draweeView.setImageURI(uri);
    }
}
