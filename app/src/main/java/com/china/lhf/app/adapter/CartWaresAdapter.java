package com.china.lhf.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.china.lhf.app.R;
import com.china.lhf.app.entity.Wares;
import com.china.lhf.app.zidingyi.NumberAddSubView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by C罗 on 2016/9/27.
 */
public class CartWaresAdapter extends RecyclerView.Adapter<CartWaresAdapter.ViewHolder> {
    private List<Wares> mDatas;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public CartWaresAdapter(List<Wares> datas) {
        this.mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cart_item_wares, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(mDatas.get(position).getName());
        holder.price.setText(mDatas.get(position).getPrice() + "");
        holder.img.setImageURI(mDatas.get(position).getImgUrl());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void addData(int position, Wares wares) {
        mDatas.add(position, wares);
        notifyItemChanged(position);
    }
    public void addData(int position, List<Wares> datas) {
        if (datas !=null&&datas.size()>0){
            mDatas.addAll(datas);
            notifyItemChanged(position,mDatas.size());
        }

    }
    public void addData(List<Wares> datas) {
        addData(0,datas);
    }
    public void clearData(){
        mDatas.clear();
        notifyItemRangeRemoved(0,mDatas.size());

    }
    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private TextView price;
        private NumberAddSubView add;
        private SimpleDraweeView img;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.cart_wares_title_tv);
            price = (TextView) itemView.findViewById(R.id.cart_wares_price_tv);
            add = (NumberAddSubView) itemView.findViewById(R.id.cart_wares_num_add_sub);
            img = (SimpleDraweeView) itemView.findViewById(R.id.cart_wares_img_sdv);
            title.setOnClickListener(this);
            price.setOnClickListener(this);
            add.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    interface OnItemClickListener {
        void onClick(View v, int position);
    }


}
