package com.china.lhf.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.china.lhf.app.entity.HomeCategory;
import com.china.lhf.app.entity.Wares;
import com.china.lhf.app.http.Contants;
import com.china.lhf.app.utiles.CartProvider;
import com.china.lhf.app.utiles.ToastUtils;
import com.china.lhf.app.widget.HomeToolbar;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import dmax.dialog.SpotsDialog;

/**
 * Created by C罗 on 2016/10/11.
 */
public class WareDetailActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.xiangiqng_web)
    WebView mWebView;
    @Bind(R.id.toolbar_shoop_list)
    HomeToolbar mhomeToolbar;

    private Wares wares;
    private WebAppInterface mappInterface;
    private CartProvider cartProvider;
    private SpotsDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_xiangqing);
//        setContentView(R.layout.test_xml);
        ButterKnife.bind(this);
        Serializable serializable = getIntent().getSerializableExtra(Contants.WARE);
        if(serializable==null){
            this.finish();
        }
        wares= (Wares) serializable;

        cartProvider=CartProvider.getInstance(this);
        mDialog=new SpotsDialog(this,"loading.....");
        mDialog.show();
        initToobar();
        initWebView();

    }

    private void initToobar() {
        mhomeToolbar.setmLeftButtonClickListener(this);
        mhomeToolbar.setRightButtonText("分享");
        mhomeToolbar.setmRightButtonClickListener(this);
    }

    private void initWebView(){
        WebSettings settings=mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);//设置是否阻止图像的显示，默认为true  是阻止
        settings.setAppCacheEnabled(true);//设置缓存
        mWebView.loadUrl(Contants.API.WARES_DETAIL);
        mappInterface=new WebAppInterface(this);
        mWebView.addJavascriptInterface(mappInterface,"appInterface");
        mWebView.setWebViewClient(new WVC());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_leftButton:
                this.finish();
                break;
            case R.id.toolbar_rightButton:
                showShare();
                break;
        }
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.ssdk_oks_share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(wares.getName());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(wares.getImgUrl());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

    class WebAppInterface{

        private Context context;

        public WebAppInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void showDetail(){
              mWebView.loadUrl("javascript:showDetail("+wares.getId()+")");
        }
        @JavascriptInterface
        public void buy(long id){
             cartProvider.put(wares);
            ToastUtils.show(context,"已添加到购物车");
        }

        @JavascriptInterface
        public void addToFavoriten(long id){

        }

    }

    class WVC extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.startsWith("phoenix://")){
                Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:15011306030"));
                try {
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(mDialog!=null && mDialog.isShowing()){
                mDialog.dismiss();
            }
            mappInterface.showDetail();
        }
    }

}
