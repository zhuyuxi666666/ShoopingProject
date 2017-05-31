package com.china.lhf.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by C罗 on 2016/9/22.
 */
public abstract class BaseAdapter<T,H extends BaseViewHoder> extends RecyclerView.Adapter<BaseViewHoder> {

    protected List<T> mDatas;
    protected final Context mContext;
    protected int mLayoutResId;
    private OnItemClickListener mOnItemClickListener=null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener=listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int posittion);
    }

    public BaseAdapter(List<T> datas,Context context,int layoutResId){
           this.mDatas = datas == null ? new ArrayList<T>() : datas;
           this.mContext=context;
           this.mLayoutResId=layoutResId;
    }

    @Override
    public BaseViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(mLayoutResId,parent,false);
        return new BaseViewHoder(view,mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(BaseViewHoder holder, int position) {
        T item=getItem(position);
        convert((H)holder,item);
    }

    public T getItem(int position) {
        return position>=mDatas.size() ? null : mDatas.get(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void refreshData(List<T> list){
        if(list!=null && list.size()>0){
            clearData();
            int size=list.size();
            for (int i=0;i<size;i++){
                mDatas.add(i,list.get(i));
                notifyItemInserted(i);
            }
        }
    }

    public void loadMoreData(List<T> list){
        if(list!=null && list.size()>0) {
            clearData();
            int size = list.size();
            int begin=mDatas.size();
            for (int i=0;i<size;i++){
                mDatas.add(list.get(i));
                notifyItemInserted(i+begin);
            }
        }
    }

    protected abstract void convert(H holder,T item);

    public void clearData(){
        int itemCount=mDatas.size();
        mDatas.clear();
        notifyItemRangeRemoved(0,itemCount);
    }
    public void addData(List<T> datas){
        addData(0,datas);
    }
    public void addData(int position,List<T> datas){
        if(datas!=null&&datas.size()>0) {
            mDatas.addAll(datas);
            notifyItemRangeChanged(0, mDatas.size());
        }
    }

    public List<T> getDatas(){
        return mDatas;
    }

}
