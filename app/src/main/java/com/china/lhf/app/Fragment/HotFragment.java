package com.china.lhf.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.china.lhf.app.R;
import com.china.lhf.app.WareDetailActivity;
import com.china.lhf.app.adapter.BaseAdapter;
import com.china.lhf.app.adapter.BaseViewHoder;
import com.china.lhf.app.adapter.HWadapter;
import com.china.lhf.app.adapter.HotWarsAdapter;
import com.china.lhf.app.adapter.SimpleAdapter;
import com.china.lhf.app.adapter.decoration.DividerItemDecoration;
import com.china.lhf.app.entity.PageRsult;
import com.china.lhf.app.entity.Wares;
import com.china.lhf.app.http.Contants;
import com.china.lhf.app.http.OkHttpHelper;
import com.china.lhf.app.http.SpostsCallback;
import com.china.lhf.app.utiles.PageUtils;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * 热卖的fragment
 *
 */
public class HotFragment extends Fragment {

    private OkHttpHelper okHttpHelper=OkHttpHelper.getInstance();
    private int currPage=1;
    private int totalPage=1;
    private int pageSize=10;
    private List<Wares> datas;
    private HWadapter mAdapter;
    @Bind(R.id.hot_mrl)
    MaterialRefreshLayout mRefreshLayout;
    @Bind(R.id.hot_rv)
    RecyclerView mRecyclerView;

//    private static final int STATE_NORMAL=0;//正常状态
//    private static final int STATE_REFRESH=1;//刷新
//    private static final int STATE_MORE=2;//加载更多
//    private int state= STATE_NORMAL;//默认状态是正常状态

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_hot_mai,container,false);
        ButterKnife.bind(this,view);

//        mRecyclerView = (RecyclerView) view.findViewById(R.id.hot_rv);

//        initRefreshLayout();
//        getData();
        PageUtils pageUtils=PageUtils.newBuilder().setUrl(Contants.API.WARES_HOT).setLoadMore(true).setOnPageListener(new PageUtils.OnPageListener() {
            @Override
            public void load(List datas, int totalPage, int totalCount) {
                mAdapter = new HWadapter(datas,getContext());
                mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int posittion) {
                       // Toast.makeText(getActivity(),"点击了"+posittion,Toast.LENGTH_SHORT).show();
                        Wares wares=mAdapter.getItem(posittion);
                        Intent intent=new Intent(getActivity(), WareDetailActivity.class);
                        intent.putExtra(Contants.WARE,wares);
                        startActivity(intent);
                    }
                });
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
            }

            @Override
            public void refresh(List datas, int totalPage, int totalCount) {
                mAdapter.clearData();
                mAdapter.addData(datas);
                //刷新第一条
                mRecyclerView.scrollToPosition(0);
            }

            @Override
            public void loadMore(List datas, int totalPage, int totalCount) {
                mAdapter.addData(mAdapter.getItemCount(),datas);
                mRecyclerView.scrollToPosition(mAdapter.getItemCount());
            }
        }).setPageSize(20).setRefreshLayout(mRefreshLayout).build(getContext(),new TypeToken<PageRsult<Wares>>(){}.getType());
        pageUtils.request();

        return view;
    }

//    private void getData(){
//        String url= Contants.API.WARES_HOT+"?curPage="+currPage+"&pageSize="+pageSize;
//        okHttpHelper.get(url, new SpostsCallback<PageRsult<Wares>>(getContext()) {
//            @Override
//            public void onSuccess(Response response, PageRsult<Wares> waresPageRsult) {
//                datas=waresPageRsult.getList();
//                currPage=waresPageRsult.getCurrentPage();
//                totalPage=waresPageRsult.getTotalPage();
//                showData();
//            }
//
//            @Override
//            public void onErroe(Response response, int code, Exception e) {
//
//            }
//        });
//    }

//    private void showData(){
//        switch (state){
//            case STATE_NORMAL:
//
////                mRecyclerView.setAdapter(new SimpleAdapter<Wares>(datas,getContext(),R.layout.hot_list_item) {
////                    @Override
////                    protected void convert(BaseViewHoder holder, Wares item) {
////                        SimpleDraweeView draweeView= (SimpleDraweeView) holder.getView(R.id.hot_wares_img_sdv);
////                        draweeView.setImageURI(Uri.parse(item.getImgUrl()));
////                        holder.getTextView(R.id.hot_wares_title_tv).setText(item.getName());
////                        holder.getTextView(R.id.hot_wares_price_tv).setText(item.getPrice()+"");
//////                        holder.getButton(R.id.hot_wares_add_btn).setOnClickListener();
////                    }
////                });
//                break;
//            case STATE_REFRESH:
//
//                break;
//            case STATE_MORE:
//
//                break;
//        }
//
//    }



//    private void initRefreshLayout(){
//        mRefreshLayout.setLoadMore(true);
//        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
//            @Override
//            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
//                refreshData();
//            }
//
//            @Override
//            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
//                super.onRefreshLoadMore(materialRefreshLayout);
//                if(currPage<=totalPage){
//                    loadMoreData();
//                }else{
//                    Toast.makeText(getActivity(),"没有更多数据了！",Toast.LENGTH_SHORT).show();
//                    mRefreshLayout.finishRefreshLoadMore();
//                }
//            }
//        });
//    }
//
//    private void refreshData(){
//        currPage=1;
//        state=STATE_REFRESH;
//        getData();
//    }
//
//    private void loadMoreData(){
//        currPage+=1;
//        state=STATE_MORE;
//        getData();
//    }

}
