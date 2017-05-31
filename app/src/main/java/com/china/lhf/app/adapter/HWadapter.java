package com.china.lhf.app.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.china.lhf.app.R;
import com.china.lhf.app.entity.ShoppingCart;
import com.china.lhf.app.entity.Wares;
import com.china.lhf.app.utiles.CartProvider;
import com.china.lhf.app.utiles.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by C罗 on 2016/9/22.
 */
public class HWadapter extends SimpleAdapter<Wares>  {

    private CartProvider provider;

    public HWadapter(List<Wares> datas, Context context) {
        super(datas,context, R.layout.hot_list_item);
        provider=CartProvider.getInstance(context);
    }

    @Override
    protected void convert(BaseViewHoder holder, final Wares item) {
        SimpleDraweeView draweeView= (SimpleDraweeView) holder.getView(R.id.category_wares_img_sdv);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));
        holder.getTextView(R.id.hot_wares_title_tv).setText(item.getName());
        holder.getTextView(R.id.hot_wares_price_tv).setText(item.getPrice()+"");
        Button button=holder.getButton(R.id.hot_wares_add_btn);
        if(button!=null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    provider.put(item);
                    ToastUtils.show(mContext, "已添加到购物车");
                }
            });
        }
    }



    public void resetLayout(int layoutId){
        this.mLayoutResId=layoutId;
        notifyItemChanged(0,getItemCount());
    }
}
