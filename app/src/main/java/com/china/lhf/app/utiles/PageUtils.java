package com.china.lhf.app.utiles;
import android.content.Context;
import android.widget.Toast;
import com.china.lhf.app.entity.PageRsult;
import com.china.lhf.app.http.OkHttpHelper;
import com.china.lhf.app.http.SpostsCallback;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Response;

/**
 * Created by lenovo on 2016/10/9.
 */
public class PageUtils {
    private static Builder builder;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    private static final int STATE_NORMAL = 0;//正常状态
    private static final int STATE_REFRESH = 1;//刷新
    private static final int STATE_MORE = 2;//加载更多
    private int state = STATE_NORMAL;//默认状态是正常状态

    private PageUtils() {
        okHttpHelper = OkHttpHelper.getInstance();
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        //可以加载更多
        builder.refreshLayout.setLoadMore(builder.canLoadMore);
        builder.refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            //刷新时调用
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (builder.pageIndex <= builder.totalPage) {
                    loadMoreData();
                } else {
                    Toast.makeText(builder.context, "已经没有更多数据了!", Toast.LENGTH_SHORT).show();
                    //关闭刷新  ]]]
                    builder.refreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    //刷新数据
    private void refreshData() {
        builder.pageIndex = 1;
        state = STATE_REFRESH;
        reuestData();
    }

    //加载更多
    private void loadMoreData() {
        builder.pageIndex += 1;
        state = STATE_MORE;
        reuestData();

    }
    private void reuestData() {
        okHttpHelper.get(buildUrl(), new RequestCallBack(builder.context));
    }
    private String buildUrl(){
        return builder.url+"?"+buildUrlParams();
    }
    private  String buildUrlParams(){
        HashMap<String ,Object> map=builder.params;
        map.put("curPage",builder.pageIndex);
        map.put("pageSize",builder.pageSize);
        StringBuffer sb=new StringBuffer();
        for (Map.Entry<String,Object> entity:map.entrySet()){
            sb.append(entity.getKey()+"="+entity.getValue());
            sb.append("&");

        }
        String s=sb.toString();
        if (s.endsWith("&")){
            s=s.substring(0,s.length()-1);
        }
        return s;
    }
    private <T> void showData(List<T> datas, int totalPage, int totalCount) {
        //正确状态下
        switch (state) {
            case STATE_NORMAL:
                if (builder.onPageListener != null) {
                    builder.onPageListener.load(datas, totalPage, totalCount);
                }
                break;
            case STATE_REFRESH:
                if (builder.onPageListener != null) {
                    builder.onPageListener.load(datas, totalPage, totalCount);
                }
                builder.refreshLayout.finishRefresh();
                break;
            case STATE_MORE:
                if (builder.onPageListener != null) {
                    builder.onPageListener.load(datas, totalPage, totalCount);
                }
                builder.refreshLayout.finishRefreshLoadMore();
                break;
        }

    }

    public void request(){
        reuestData();
    }

    public void putParams(String ket,Object value){
        builder.putParam(ket,value);
    }

    public interface OnPageListener<T> {
        void load(List<T> datas, int totalPage, int totalCount);

        void refresh(List<T> datas, int totalPage, int totalCount);

        void loadMore(List<T> datas, int totalPage, int totalCount);
    }

    public static Builder newBuilder() {
        builder = new Builder();
        return builder;
    }

    public static class Builder {
        private String url;
        private MaterialRefreshLayout refreshLayout;
        private boolean canLoadMore;
        private int totalPage = 1;
        private int pageIndex = 1;
        private int pageSize = 10;
        private HashMap<String, Object> params = new HashMap<>(5);
        private OnPageListener onPageListener;
        private Context context;
        private Type type;

        public Builder setOnPageListener(OnPageListener onPageListener) {
            this.onPageListener = onPageListener;
            return builder;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return builder;
        }

        public Builder setRefreshLayout(MaterialRefreshLayout refreshLayout) {
            this.refreshLayout = refreshLayout;
            return builder;
        }

        public Builder setLoadMore(boolean loadMore) {
            this.canLoadMore = loadMore;
            return builder;
        }

        public Builder setPageSize(int pageSize) {
            this.pageSize = pageSize;
            return builder;
        }

        public Builder putParam(String key, Object value) {
            params.put(key, value);
            return builder;
        }

        public PageUtils build(Context context, Type type) {
            this.context=context;
            this.type=type;
            validate();
            return new PageUtils();
        }



        private void validate(){
            if (context==null){
                throw new RuntimeException("Context can't be null");
            }
            if (this.url==null||"".equals(this.url)){
                throw new RuntimeException("URL can't be null");
            }
            if (this.refreshLayout==null||"".equals(this.url)){
                throw new RuntimeException("MaterialRefreshLayout can't be null");
            }
        }
    }
    class RequestCallBack<T> extends SpostsCallback<PageRsult<T>> {

        public RequestCallBack(Context mContext) {
            super(mContext);
            super.mType=builder.type;
        }

        @Override
        public void onSuccess(Response response, PageRsult<T> pageResult) {
            builder.pageIndex = pageResult.getCurrentPage();
            builder.totalPage = pageResult.getTotalPage();
            showData(pageResult.getList(),builder.totalPage,pageResult.getTotalCount());
        }

        @Override
        public void onErroe(Response response, int code, Exception e) {
            super.onErroe(response, code, e);
        }
    }
}

