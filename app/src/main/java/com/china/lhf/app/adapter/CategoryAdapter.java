package com.china.lhf.app.adapter;

import android.content.Context;

import com.china.lhf.app.R;
import com.china.lhf.app.entity.Category;

import java.util.List;

/**
 * Created by C罗 on 2016/9/23.
 */
public class CategoryAdapter extends SimpleAdapter<Category> {

    public CategoryAdapter(List<Category> datas, Context context) {
        super(datas, context, R.layout.category_left_item);
    }



    @Override
    protected void convert(BaseViewHoder holder, Category item) {
            holder.getTextView(R.id.category_left_tv).setText(item.getName());
    }
}
