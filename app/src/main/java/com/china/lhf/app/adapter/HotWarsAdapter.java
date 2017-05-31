package com.china.lhf.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.china.lhf.app.R;
import com.china.lhf.app.entity.Wares;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by C罗 on 2016/9/20.
 */
public class HotWarsAdapter extends RecyclerView.Adapter<HotWarsAdapter.ViewHoder> {

    private static final String TAG ="HotWarsAdapter" ;
    private Context mContext;
    private List<Wares> mDatas;
    private LayoutInflater inflater;
    private OnItemClickListener listener;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public HotWarsAdapter(List<Wares> datas) {
        this.mDatas = datas;
    }


    @Override
    public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.hot_list_item, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(ViewHoder holder, int position) {
        holder.title.setText(mDatas.get(position).getName());
        holder.price.setText(mDatas.get(position).getPrice() + "");
        holder.img.setImageURI(mDatas.get(position).getImgUrl());
       // Picasso.with(mContext).load(mDatas.get(position).getImgUrl()).into(holder.img);
//        Log.e(TAG, "图片信息："+mDatas.get(position).getImgUrl());
    }

    public void addData(int position, Wares wares) {
        mDatas.add(position, wares);
        notifyItemChanged(position);
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public void clearData(){
        mDatas.clear();
        notifyItemRangeRemoved(0,mDatas.size());
    }
    public void addData(List<Wares> datas){
           addData(0,datas);
    }
    public void addData(int position,List<Wares> datas){
        if(datas!=null&&datas.size()>0) {
            mDatas.addAll(datas);
            notifyItemRangeChanged(0, mDatas.size());
        }
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title;
        private TextView price;
        private Button add;
        private SimpleDraweeView img;

        public ViewHoder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.hot_wares_title_tv);
            price = (TextView) itemView.findViewById(R.id.hot_wares_price_tv);
            add = (Button) itemView.findViewById(R.id.hot_wares_add_btn);
            img = (SimpleDraweeView) itemView.findViewById(R.id.category_wares_img_sdv);
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
