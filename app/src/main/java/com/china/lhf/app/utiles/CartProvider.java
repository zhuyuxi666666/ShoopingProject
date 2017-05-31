package com.china.lhf.app.utiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.SparseArray;

import com.china.lhf.app.entity.ShoppingCart;
import com.china.lhf.app.entity.Wares;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by C罗 on 2016/9/26.
 */
public class CartProvider {

    private SparseArray<ShoppingCart> datas=null;
    private Context mcontext;
    private static final String CART_JSON="cart_json";

    private static CartProvider instance = null;
    public static CartProvider getInstance(Context context){
        if(instance == null){
            synchronized (CartProvider.class){
                if(instance == null){
                    instance=new CartProvider(context);
                }
            }
        }
        return instance;
    }

    private CartProvider(Context context) {
        this.datas = new SparseArray<ShoppingCart>(10);
        this.mcontext=context;
        listToSpares();
    }

    public void put(Wares wares){
        ShoppingCart cart=convertData(wares);
        put(cart);
    }

    public void put(ShoppingCart cart){
        ShoppingCart temp = datas.get(cart.getId().intValue());
        if(temp!=null){
            temp.setCount(temp.getCount() + 1);
        }else{
            temp=cart;
            temp.setCount(1);
        }
        datas.put(cart.getId().intValue(),temp);
        commit();
    }

    public void updata(ShoppingCart cart){
        datas.put(cart.getId().intValue(),cart);
        commit();
    }

    public void delete(ShoppingCart cart) {
        datas.delete(cart.getId().intValue());
        commit();
    }

    public void commit(){
        List<ShoppingCart> carts=sparesToList();
        PreferencesUtiles.putString(mcontext,CART_JSON,GsonUtils.toJson(carts));
    }

    private void listToSpares(){
        List<ShoppingCart> carts=getDataFromLocal();
        if(carts!=null&&carts.size()>0){
            for (ShoppingCart cart:carts) {
                datas.put(cart.getId().intValue(),cart);
            }
        }
    }

    private List<ShoppingCart> sparesToList(){
        int size=datas.size();
        List<ShoppingCart> list=new ArrayList<ShoppingCart>();
        for (int i=0;i<size;i++){
            list.add(datas.valueAt(i));
        }
        return list;
    }

    public List<ShoppingCart> getAll(){

        return getDataFromLocal();
    }

    public List<ShoppingCart> getDataFromLocal(){
           String json=PreferencesUtiles.getString(mcontext,CART_JSON);
        List<ShoppingCart> carts=null;
        if(json!=null){
              carts=GsonUtils.fromJson(json,new TypeToken<List<ShoppingCart>>(){}.getType());
        }
        return carts;
    }

    private ShoppingCart convertData(Wares item){
        ShoppingCart cart=new ShoppingCart();
        cart.setId(item.getId());
        cart.setName(item.getName());
        cart.setImgUrl(item.getImgUrl());
        cart.setPrice(item.getPrice());
        cart.setStock(item.getStock());
        return cart;
    }

    public void clear(){
        datas.clear();
        commit();
    }
}
