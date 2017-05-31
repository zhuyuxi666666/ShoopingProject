package com.china.lhf.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.china.lhf.app.AddressListActivity;
import com.china.lhf.app.LoginActivity;
import com.china.lhf.app.MyOrderActivity;
import com.china.lhf.app.R;
import com.china.lhf.app.adapter.MyFavoriteAdapter;
import com.china.lhf.app.application.MyApplication;
import com.china.lhf.app.entity.User;
import com.china.lhf.app.http.Contants;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 热卖的fragment
 */
public class MineFragment extends BaseFragment {

    @Bind(R.id.mine_logo_iv)
    CircleImageView mcircleImageView;
    @Bind(R.id.mine_login)
    TextView mlogin;
    @Bind(R.id.exit_login)
    Button mexit;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mine,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void init() {
        showUser();
    }

    @OnClick({R.id.mine_login,R.id.exit_login,R.id.order_mine,R.id.like_mine,R.id.address_mine,R.id.message_mine})
    public void login(View v){
        switch (v.getId()) {
            case R.id.mine_login:
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivityForResult(intent, Contants.REQUEST_CODE);
                break;
            case R.id.exit_login:
                MyApplication.getInstance().clearUser();
                showUser();
                break;
            case R.id.order_mine:     //我的订单
                Intent orderIntent = new Intent(getContext(), MyOrderActivity.class);
                startActivity(orderIntent, true);
                break;
            case R.id.like_mine:     //我的收藏
                Intent favoriteIntent = new Intent(getContext(), MyFavoriteAdapter.class);
                startActivity(favoriteIntent, true);
                break;
            case R.id.address_mine:     //收货地址
                Intent addrIntent = new Intent(getContext(), AddressListActivity.class);
                startActivity(addrIntent, true);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        showUser();
        super.onActivityResult(requestCode,resultCode,data);
    }

    private void showUser(){
        User user= MyApplication.getInstance().getUser();
        if(user==null){
            mexit.setVisibility(View.GONE);
            mcircleImageView.setImageResource(R.mipmap.default_head);
            mlogin.setText(R.string.to_login);
        }else{
            mexit.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(user.getLogo_url())) {
                Picasso.with(getActivity()).load(Contants.API.BASE_URL + user.getLogo_url()).into(mcircleImageView);
            }
            mlogin.setText(user.getUsername());
        }
    }
}
