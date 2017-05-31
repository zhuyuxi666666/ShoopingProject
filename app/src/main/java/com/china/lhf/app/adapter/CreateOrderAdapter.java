package com.china.lhf.app.adapter;

import android.content.Context;
import android.net.Uri;

import com.china.lhf.app.R;
import com.china.lhf.app.entity.ShoppingCart;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by C罗 on 2016/10/25.
 */
public class CreateOrderAdapter extends SimpleAdapter<ShoppingCart> {
    public CreateOrderAdapter(Context mContext, List<ShoppingCart> mdates) {
        super(mdates,mContext,R.layout.item_order_wares);
    }

    @Override
    protected void convert(BaseViewHoder holder, ShoppingCart item) {
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawe_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));
    }

    public float getTotalPice() {
        float sum = 0;
        if (!isNull()) {
            return sum;
        }
        for (ShoppingCart car : mDatas) {
            sum += car.getCount() * car.getPrice();
        }
        return sum;
    }

    private boolean isNull() {
        return (mDatas != null && mDatas.size() > 0);
    }



}
