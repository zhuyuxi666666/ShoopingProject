package com.china.lhf.app.entity;

/**
 * Created by C罗 on 2016/9/26.
 */
public class ShoppingCart extends Wares {
    private int count;
    private boolean isChecked=true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
