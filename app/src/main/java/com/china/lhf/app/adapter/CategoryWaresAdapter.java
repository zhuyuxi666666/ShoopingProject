package com.china.lhf.app.adapter;

import android.content.Context;
import android.net.Uri;

import com.china.lhf.app.R;
import com.china.lhf.app.entity.Wares;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class CategoryWaresAdapter extends SimpleAdapter<Wares> {
    public CategoryWaresAdapter(List<Wares> datas, Context mContext) {
        super(datas, mContext, R.layout.item_category_wares);
    }

    @Override
    protected void convert(BaseViewHoder holder, Wares item) {
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.category_wares_img_sdv);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));
        holder.getTextView(R.id.hot_wares_title_tv).setText(item.getName());
        holder.getTextView(R.id.hot_wares_price_tv).setText("￥" + item.getPrice() + "");
    }
}
