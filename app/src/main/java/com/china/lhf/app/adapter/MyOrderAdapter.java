package com.china.lhf.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.china.lhf.app.R;
import com.china.lhf.app.application.MyApplication;
import com.china.lhf.app.entity.Order;
import com.china.lhf.app.entity.OrderItem;
import com.china.lhf.app.utiles.DisplayUtil;
import com.squareup.picasso.Picasso;
import com.w4lle.library.NineGridAdapter;
import com.w4lle.library.NineGridlayout;

import java.util.List;

/**
 * 我的订单适配器
 */
public class MyOrderAdapter extends SimpleAdapter<Order> {

    public MyOrderAdapter(Context context, List<Order> datas) {
        super(datas,context, R.layout.item_my_order);
    }

    @Override
    protected void convert(BaseViewHoder viewHolder, final Order item) {
        viewHolder.getTextView(R.id.order_num_tv).setText("订单号：" + item.getOrderNum());
        viewHolder.getTextView(R.id.order_total_tv).setText("实付金额：￥：" + item.getAmount());
        TextView statusTV = viewHolder.getTextView(R.id.status_tv);
        switch (item.getStatus()) {
            case Order.STATUS_PAY_SUCCESS:
                statusTV.setText("成功");
                statusTV.setTextColor(Color.parseColor("#ff4CAF50"));
                break;
            case Order.STATUS_PAY_FAIL:
                statusTV.setText("失败");
                statusTV.setTextColor(Color.parseColor("#ffF44336"));
                break;
            case Order.STATUS_PAY_WAIT:
            case Order.STATUS_PAY_CANCEL:
                statusTV.setText("等待支付");
                statusTV.setTextColor(Color.parseColor("#ffFFEB3B"));
                break;

        }
        NineGridlayout nineGridlayout = (NineGridlayout) viewHolder.getView(R.id.order_wares_ngl);
        nineGridlayout.setGap(5);   //设置图片间隔
        int width = (MyApplication.getInstance().screenW - DisplayUtil.dip2px(mContext, 42)) / 3;
        //设置单张图片时的宽度
        nineGridlayout.setDefaultWidth(width);
        //设置单张图片时的高度
        nineGridlayout.setDefaultHeight(width);
        nineGridlayout.setAdapter(new OrderItemAdapter(mContext, item.getItems()));
    }

    class OrderItemAdapter extends NineGridAdapter {

        private List<OrderItem> items;

        public OrderItemAdapter(Context context, List<OrderItem> items) {
            super(context, items);
            this.items = items;
        }

        @Override
        public int getCount() {
            return (items == null) ? 0 : items.size();
        }

        @Override
        public String getUrl(int positopn) {
            OrderItem item = getItem(positopn);
            return item.getWares().getImgUrl();
        }


        @Override
        public OrderItem getItem(int position) {
            return (items == null) ? null : items.get(position);
        }

        @Override
        public long getItemId(int position) {
            OrderItem item = getItem(position);
            return item == null ? 0 : item.getId();
        }

        @Override
        public View getView(int i, View view) {
            ImageView iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
            Picasso.with(context).load(getUrl(i)).placeholder(new ColorDrawable(Color
                    .parseColor("#f5f5f5"))).into(iv);
            return iv;
        }
    }

}
