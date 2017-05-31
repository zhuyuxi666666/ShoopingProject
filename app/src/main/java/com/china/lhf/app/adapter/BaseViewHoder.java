package com.china.lhf.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by C罗 on 2016/9/22.
 */
public class BaseViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private SparseArray<View> views;
    protected BaseAdapter.OnItemClickListener mOnItemClickListener;

    public BaseViewHoder(View itemView,BaseAdapter.OnItemClickListener onItemClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.views=new SparseArray<View>();
        this.mOnItemClickListener=onItemClickListener;
    }

    public View getView(int viewId){
        return retrieveView(viewId);
    }

    public TextView getTextView(int viewId){
        return retrieveView(viewId);
    }

    public ImageView getImageView(int viewId){
        return retrieveView(viewId);
    }

    public Button getButton(int viewId){
        return retrieveView(viewId);
    }

    public CheckBox getCheckBox(int viewId){
        return retrieveView(viewId);
    }

    private <V extends View> V retrieveView(int viewId){
            View view=views.get(viewId);
        if(view==null){
            view =itemView.findViewById(viewId);
            views.put(viewId,view);
        }
        return (V) view;
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener!=null){
            mOnItemClickListener.onItemClick(v,getLayoutPosition());
        }
    }
}
