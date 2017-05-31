package com.china.lhf.app;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.china.lhf.app.Fragment.CarFragment;
import com.china.lhf.app.Fragment.CategoryFragment;
import com.china.lhf.app.Fragment.HomeFragment;
import com.china.lhf.app.Fragment.HotFragment;
import com.china.lhf.app.Fragment.MineFragment;
import com.china.lhf.app.entity.Tab;
import com.china.lhf.app.event.MessageEvent;
import com.china.lhf.app.widget.FragmentTabHost;
import com.china.lhf.app.widget.HomeToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity {

   // @Bind(android.R.id.tabhost)
    FragmentTabHost mTabhost;
    private LayoutInflater mlayoutInflater;
    private List<Tab> mTabs=new ArrayList<>(5);
    private CarFragment carFragment;
    //@Bind(R.id.toobar)
    HomeToolbar homeToolbar;

    private boolean b=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setContentView(R.layout.activity_test);
        EventBus.getDefault().register(this);

        iniTab();
        }

    private void iniTab() {
        Tab tab_name=new Tab(HomeFragment.class,R.drawable.selector_icon_home,R.string.home);
        Tab tab_hot=new Tab(HotFragment.class,R.drawable.selector_icon_hot,R.string.hot);
        Tab tab_category=new Tab(CategoryFragment.class,R.drawable.selector_icon_cageory,R.string.category);
        Tab tab_cart=new Tab(CarFragment.class,R.drawable.selector_icon_cart,R.string.cart);
        Tab tab_mine=new Tab(MineFragment.class,R.drawable.selector_icon_mine,R.string.mine);

        mTabs.add(tab_name);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);

        mlayoutInflater=LayoutInflater.from(this);
        mTabhost= (FragmentTabHost) findViewById(android.R.id.tabhost);
        homeToolbar= (HomeToolbar) findViewById(R.id.toobar);
        mTabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);

        for (Tab tab:mTabs) {
            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            //1下面导航项  2上面对应的fragment  3，bundle传参，用于activity之间传递
            mTabhost.addTab(tabSpec, tab.getFragement(), null);
        }

        mTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals(getString(R.string.cart))){
                    refData();
                }else{
                    homeToolbar.showSearchView();
                    homeToolbar.hideTitleView();
                }
            }

            private void refData() {
                if(carFragment==null) {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
                    if (fragment != null) {
                        carFragment = (CarFragment) fragment;
                        carFragment.refData();
                        carFragment.changeToobar();
                    }
                }else{
                    carFragment.refData();
                    carFragment.changeToobar();
                }
            }
        });


        //去掉分割线
        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabhost.setCurrentTab(0);

    }

    private View buildIndicator(Tab tab){
        View view = mlayoutInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = (ImageView) view.findViewById(R.id.tab_indicator_icon_iv);
        TextView text = (TextView) view.findViewById(R.id.tab_indicator_title_tv);
        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
         if("pay".equals(event.getMessage())){
             b=true;
         }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(b){
            mTabhost.setCurrentTab(0);
            b=false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

