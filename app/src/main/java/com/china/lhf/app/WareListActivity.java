package com.china.lhf.app;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.china.lhf.app.adapter.HWadapter;
import com.china.lhf.app.adapter.decoration.DividerItemDecoration;
import com.china.lhf.app.entity.PageRsult;
import com.china.lhf.app.entity.Wares;
import com.china.lhf.app.http.Contants;
import com.china.lhf.app.utiles.PageUtils;
import com.china.lhf.app.widget.HomeToolbar;
import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by C罗 on 2016/10/9.
 */
public class WareListActivity extends BaseActivity implements PageUtils.OnPageListener<Wares>,TabLayout.OnTabSelectedListener,View.OnClickListener{

    @Bind(R.id.ware_list_tl)
    TabLayout mTabLayout;
    @Bind(R.id.ware_list_summary_tv)
    TextView mSummaryTV;
    @Bind(R.id.ware_list_rv)
    RecyclerView mWareRecyclerView;
    @Bind(R.id.ware_list_mrl)
    MaterialRefreshLayout materialRefreshLayout;
    @Bind(R.id.toolbar_shoop_list)
    HomeToolbar homeToolbar;

    private int orderBy=0;
    private long campaignId=0;
    private HWadapter mWaresAdapter;
    private PageUtils pageUtils;

    private static final int TAG_DEFAULT=0;
    private static final int TAG_PRICE=2;
    private static final int TAG_SALE=1;

    private static final int ACTION_LIST=1;
    private static final int ACTION_GIRD=2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wares_list);
        ButterKnife.bind(this);

        campaignId=getIntent().getLongExtra(Contants.CAMPAIGN_ID,0);

        initTab();
        getData();
        initToobar();
    }

    private void initTab(){
        TabLayout.Tab tab=mTabLayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);
        mTabLayout.addTab(tab);

        tab=mTabLayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_SALE);
        mTabLayout.addTab(tab);

        tab=mTabLayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_DEFAULT);
        mTabLayout.addTab(tab);

        mTabLayout.setOnTabSelectedListener(this);
    }

    private void initToobar(){
        homeToolbar.setTitle("商品列表");
        homeToolbar.setmLeftButtonClickListener(this);
        homeToolbar.setRightButtonIcon(R.mipmap.icon_grid_32);
        homeToolbar.setmRightButtonClickListener(this);
        homeToolbar.getmRightButton().setTag(ACTION_LIST);
    }

    private void getData(){
         pageUtils=PageUtils.newBuilder().setUrl(Contants.API.CAMPAIGN_LIST)
                .putParam("campaignId",campaignId)
                .putParam("orderBy",orderBy)
                .setRefreshLayout(materialRefreshLayout)
                .setLoadMore(true)
                .setOnPageListener(this)
                .build(this,new TypeToken<PageRsult<Wares>>(){}.getType());
        pageUtils.request();
    }

    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {
             mSummaryTV.setText("共有"+totalCount+"件商品");
             if(mWaresAdapter==null){
                 mWaresAdapter=new HWadapter(datas,this);
                 mWareRecyclerView.setAdapter(mWaresAdapter);
                 mWareRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                 mWareRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
                 mWareRecyclerView.setItemAnimator(new DefaultItemAnimator());
              }else {
                 mWaresAdapter.refreshData(datas);
             }
    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
            mWaresAdapter.refreshData(datas);
            mWareRecyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
            mWaresAdapter.loadMoreData(datas);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        orderBy= (int) tab.getTag();
        pageUtils.putParams("orderBy",orderBy);
        pageUtils.request();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.toolbar_leftButton){
            WareListActivity.this.finish();
        }else {
            int action= (int) v.getTag();
            switch (action){
                case ACTION_LIST:
                    homeToolbar.setRightButtonIcon(R.mipmap.icon_list_32);
                    homeToolbar.getmRightButton().setTag(ACTION_GIRD);
                    mWaresAdapter.resetLayout(R.layout.item_category_wares);
                    mWareRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
                    break;
                case ACTION_GIRD:
                    homeToolbar.setRightButtonIcon(R.mipmap.icon_grid_32);
                    homeToolbar.getmRightButton().setTag(ACTION_LIST);
                    mWaresAdapter.resetLayout(R.layout.hot_list_item);
                    mWareRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                    break;
            }
        }
    }


}
