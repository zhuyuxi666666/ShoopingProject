package com.china.lhf.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.china.lhf.app.MainActivity;
import com.china.lhf.app.NewOrderActivity;
import com.china.lhf.app.R;
import com.china.lhf.app.adapter.CartAdapter;
import com.china.lhf.app.adapter.decoration.DividerItemDecoration;
import com.china.lhf.app.entity.ShoppingCart;
import com.china.lhf.app.entity.User;
import com.china.lhf.app.http.Contants;
import com.china.lhf.app.http.OkHttpHelper;
import com.china.lhf.app.http.SpostsCallback;
import com.china.lhf.app.utiles.CartProvider;
import com.china.lhf.app.widget.HomeToolbar;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * 热卖的fragment
 *
 */
public class CarFragment extends BaseFragment implements View.OnClickListener{

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.checkbox_all)
    CheckBox mCheckBox;
    @Bind(R.id.tv_total)
    TextView mTextTotal;
    @Bind(R.id.btn_order)
    Button mBtnOrder;
    @Bind(R.id.btn_del)
    Button mBtnDel;

    private CartAdapter mAdapter;
    private CartProvider cartProvider;
    private HomeToolbar homeToolbar;

    public static final int ACTION_EDIT=1;
    public static final int ACTION_CAMPLATE=2;

    private OkHttpHelper okHttpHelper=OkHttpHelper.getInstance();

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.fragment_car,container,false);
       ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void init() {
        cartProvider=CartProvider.getInstance(getContext());
        showData();
    }

    private void showData(){
        List<ShoppingCart> carts=cartProvider.getAll();
        Log.e("购物车", "showData: "+carts);
        mAdapter=new CartAdapter(carts,getContext(),mCheckBox,mTextTotal);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
    }

    public void refData(){
        mAdapter.clearData();
        List<ShoppingCart> carts=cartProvider.getAll();
        mAdapter.addData(carts);
        mAdapter.showTotalPrice();
        mCheckBox.setChecked(true);
        hideDelControl();
    }

    //在activity关联fragment时调用
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
            MainActivity activity= (MainActivity) context;
            homeToolbar= (HomeToolbar) activity.findViewById(R.id.toobar);
            changeToobar();
        }
    }

    public void changeToobar() {
        homeToolbar.hideSearchView();
        homeToolbar.showTitleView();
        homeToolbar.setTitle(R.string.cart);
        homeToolbar.setRightButtonText("编辑");
        homeToolbar.setmRightButtonClickListener(this);
        homeToolbar.getmRightButton().setTag(ACTION_EDIT);
    }

    @Override
    public void onClick(View v) {
        int action= (int) v.getTag();
        if(ACTION_EDIT == action){
            showDeleteControl();
        }else if(ACTION_CAMPLATE == action){
            hideDelControl();
        }
    }

    private void showDeleteControl(){
        homeToolbar.setRightButtonText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        homeToolbar.getmRightButton().setTag(ACTION_CAMPLATE);
        mAdapter.checkAllOrNone(false);
        mCheckBox.setChecked(false);
    }

    private void hideDelControl(){
        homeToolbar.setRightButtonText("编辑");
        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);
        mBtnDel.setVisibility(View.GONE);
        homeToolbar.getmRightButton().setTag(ACTION_EDIT);
        mAdapter.checkAllOrNone(true);
        mCheckBox.setChecked(true);
        mAdapter.showTotalPrice();
    }

    @OnClick({R.id.btn_del,R.id.btn_order})
    public void cartBtnClick(View view){
        switch (view.getId()) {
            case R.id.btn_del :
            mAdapter.delCart();
                break;
            case R.id.btn_order:
                Intent intent=new Intent(getActivity(), NewOrderActivity.class);
                startActivity(intent,true);
                break;
        }
    }

}
