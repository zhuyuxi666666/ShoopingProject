package com.china.lhf.app.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by C罗 on 2016/9/22.
 */
public abstract class SimpleAdapter<T> extends BaseAdapter<T,BaseViewHoder> {

    public SimpleAdapter(List<T> datas, Context context, int layoutResId) {
        super(datas, context, layoutResId);
    }
}
