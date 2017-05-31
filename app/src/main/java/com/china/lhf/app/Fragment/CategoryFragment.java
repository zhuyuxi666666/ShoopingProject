package com.china.lhf.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.china.lhf.app.R;
import com.china.lhf.app.adapter.BaseAdapter;
import com.china.lhf.app.adapter.CategoryAdapter;
import com.china.lhf.app.adapter.CategoryWaresAdapter;
import com.china.lhf.app.adapter.HWadapter;
import com.china.lhf.app.adapter.decoration.DividerGridItemDecoration;
import com.china.lhf.app.adapter.decoration.DividerItemDecoration;
import com.china.lhf.app.entity.Banner;
import com.china.lhf.app.entity.Category;
import com.china.lhf.app.entity.PageRsult;
import com.china.lhf.app.entity.Wares;
import com.china.lhf.app.http.BaseCallback;
import com.china.lhf.app.http.Contants;
import com.china.lhf.app.http.OkHttpHelper;
import com.china.lhf.app.http.SpostsCallback;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 热卖的fragment
 *
 */
public class CategoryFragment extends Fragment {

    @Bind(R.id.category_rv)
    RecyclerView mRecyclerView;
    @Bind(R.id.category_wares_rv)
    RecyclerView mWareRecyclerView;
    @Bind(R.id.category_sl)
    SliderLayout mSliderLayout;
    @Bind(R.id.category_mrl)
    MaterialRefreshLayout mRefreshLayout;

    private OkHttpHelper okHttpHelper=OkHttpHelper.getInstance();
    private CategoryAdapter mCategoryAdapter;
    private List<Banner> mBanners;
    private PagerIndicator mIndicator;

    private int currPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;
    private static final int STATE_NORMAL=0;//正常状态
    private static final int STATE_REFRESH=1;//刷新
    private static final int STATE_MORE=2;//加载更多
    private int state= STATE_NORMAL;//默认状态是正常状态
    private List<Wares> datas;
    private long categoryId;
    private CategoryWaresAdapter mCategoryWaresAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_category,container,false);

        ButterKnife.bind(this,view);
        requestCategoryData();
        requestImages();
        initRefreshLayout();

        return view;
    }

    private void requestCategoryData(){
        okHttpHelper.get(Contants.API.CATEGORY_LIST, new SpostsCallback<List<Category>>(getContext()) {
            @Override
            public void onSuccess(Response response, List<Category> categories) {
                   showcategoryData(categories);
                if (categories != null && categories.size() > 0) {
                    categoryId=categories.get(0).getId();
                    requestWares(categoryId);
                }
            }

            @Override
            public void onErroe(Response response, int code, Exception e) {

            }
        });
    }

    private void showcategoryData(List<Category> categories){
        mCategoryAdapter=new CategoryAdapter(categories,getContext());
        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Category category=mCategoryAdapter.getItem(position);
                categoryId=category.getId();
                currPage=1;
                state=STATE_NORMAL;
                requestWares(category.getId());
            }
        });
        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }

    private void requestImages() {
        String url = Contants.API.BANNER_URL + "?type=1";
//        String url="http://101.200.167.75:8080/phoenixshop/banner/query";
        okHttpHelper.get(url, new SpostsCallback<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanners = banners;
                initSlider();
            }

            @Override
            public void onErroe(Response response, int code, Exception e) {
            }
        });
    }

    private void initSlider(){
        for (Banner banner:mBanners){
            TextSliderView textSliderView1 = new TextSliderView(getActivity());
            textSliderView1
                    .description(banner.getName())
                    .image(banner.getImgUrl())
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            mSliderLayout.addSlider(textSliderView1);
        }
        //设置自定义动画   文字动画由下向上冒出来
        //mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        //Fade为平面效果  这可以设置图片轮播样式
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
        mSliderLayout.setDuration(3000);
    }

    private void requestWares(long categoryId){
        String url = Contants.API.WARES_LIST + "?categoryId=" + categoryId + "&curPage=" + currPage + "&pageSize=" + pageSize;
        okHttpHelper.get(url, new BaseCallback<PageRsult<Wares>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Response response, PageRsult<Wares> waresPageRsult) {
                currPage = waresPageRsult.getCurrentPage();
                totalPage = waresPageRsult.getTotalPage();
                showData(waresPageRsult.getList());
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
    }

    private void showData(List<Wares> datas){
        switch (state){
            case STATE_NORMAL:
                if(mCategoryWaresAdapter==null) {
                    mCategoryWaresAdapter = new CategoryWaresAdapter(datas, getContext());
                    mCategoryWaresAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Toast.makeText(getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                        }
                    });
                    mWareRecyclerView.setAdapter(mCategoryWaresAdapter);
                    mWareRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    //mWareRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mWareRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext()));
                }else{
                    mCategoryWaresAdapter.clearData();
                    mCategoryWaresAdapter.addData(datas);
                }
                break;
            case STATE_REFRESH:
                mCategoryWaresAdapter.clearData();
                mCategoryWaresAdapter.addData(datas);
                mWareRecyclerView.scrollToPosition(0);
                mRefreshLayout.finishRefresh();
                break;
            case STATE_MORE:
                mCategoryWaresAdapter.addData(mCategoryWaresAdapter.getItemCount(), datas);
                mWareRecyclerView.scrollToPosition(mCategoryWaresAdapter.getItemCount());
                mRefreshLayout.finishRefreshLoadMore();
                break;
        }
    }

    private void initRefreshLayout(){
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                if(currPage<=totalPage){
                    loadMoreData();
                }else{
                    Toast.makeText(getActivity(),"没有更多数据了！",Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void refreshData(){
        currPage=1;
        state=STATE_REFRESH;
        requestWares(categoryId);
    }

    private void loadMoreData(){
        currPage+=1;
        state=STATE_MORE;
        requestWares(categoryId);
    }
}
