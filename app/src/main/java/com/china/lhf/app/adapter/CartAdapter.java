package com.china.lhf.app.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.china.lhf.app.R;
import com.china.lhf.app.entity.ShoppingCart;
import com.china.lhf.app.entity.Wares;
import com.china.lhf.app.utiles.CartProvider;
import com.china.lhf.app.zidingyi.NumberAddSubView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Iterator;
import java.util.List;

/**
 * Created by C罗 on 2016/9/27.
 */
public class CartAdapter extends SimpleAdapter<ShoppingCart> implements BaseAdapter.OnItemClickListener{

    private CheckBox checkBox;
    private TextView textView;
    //private NumberAddSubView numberAddSubView;
    private CartProvider cartProvider;

    public CartAdapter(List<ShoppingCart> datas, Context context, final CheckBox checkBox, TextView textView) {
        super(datas, context, R.layout.cart_item_wares);
        this.checkBox=checkBox;
        this.textView=textView;

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAllOrNone(checkBox.isChecked());
                showTotalPrice();
            }
        });
        cartProvider=CartProvider.getInstance(context);
        setOnItemClickListener(this);
        showTotalPrice();
    }

    public void checkAllOrNone(boolean ischecked) {
        if(!isNull()){
            return;
        }
        int i=0;
        for (ShoppingCart cart:mDatas){
            cart.setChecked(ischecked);
            notifyItemChanged(i);
            i++;
        }
    }

    @Override
    protected void convert(BaseViewHoder holder, final ShoppingCart item) {
        holder.getTextView(R.id.cart_wares_title_tv).setText(item.getName());
        holder.getTextView(R.id.cart_wares_price_tv).setText("￥"+item.getPrice());
        SimpleDraweeView draweeView= (SimpleDraweeView) holder.getView(R.id.cart_wares_img_sdv);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));

        CheckBox checkBox= (CheckBox) holder.getView(R.id.checkBox);
        checkBox.setChecked(item.isChecked());

        NumberAddSubView numberAddSubView= (NumberAddSubView) holder.getView(R.id.cart_wares_num_add_sub);
        numberAddSubView.setValue(item.getCount());
        numberAddSubView.setMaxValue(item.getStock());
        numberAddSubView.setmOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                   addOrSubUpdate(value,item);
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                   addOrSubUpdate(value,item);
            }
        });
    }

    private void addOrSubUpdate(int value,ShoppingCart item){
        item.setCount(value);
        cartProvider.updata(item);
        showTotalPrice();
    }

    private float getTotalPrice(){
        float sum=0;
        if(!isNull()){
            return sum;
        }
        for (ShoppingCart cart:mDatas){
            if(cart.isChecked()){
                sum+=cart.getCount()*cart.getPrice();
            }
        }
        return sum;
    }

    private boolean isNull(){
        return (mDatas!=null && mDatas.size()>0);
    }

    public void showTotalPrice(){
        float total=getTotalPrice();
//        textView.setText(Html.fromHtml("合计 ￥<font color='color:#eb4f38'>"+total+"</font>"),TextView.BufferType.SPANNABLE);
        SpannableString sp=new SpannableString("合计 ￥"+total);
//        mContext.getResources().getColor(R.color.colorPrimary);
        sp.setSpan(new ForegroundColorSpan(0xFFeb4f38),3,sp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(sp);
    }

    @Override
    public void onItemClick(View view, int posittion) {
        ShoppingCart cart=getItem(posittion);
        cart.setChecked(!cart.isChecked());
        notifyItemChanged(posittion);
        checkListener();
        showTotalPrice();
    }

    private void checkListener(){
        int count=0;
        int checkNum=0;
        if(mDatas!=null){
            count=mDatas.size();
            for (ShoppingCart cart:mDatas){
                if(!cart.isChecked()){
                    checkBox.setChecked(false);
                    break;
                }else {
                    checkNum+=1;
                }
            }
            if(count == checkNum){
                checkBox.setChecked(true);
            }
        }
    }

    public void delCart(){
        if(!isNull()){
            return;
        }
        for (Iterator iterator=mDatas.iterator();iterator.hasNext();){
            ShoppingCart cart= (ShoppingCart) iterator.next();
            if(cart.isChecked()){
                int position=mDatas.indexOf(cart);
                cartProvider.delete(cart);
                iterator.remove();
                notifyItemChanged(position);
            }
        }
    }

}
