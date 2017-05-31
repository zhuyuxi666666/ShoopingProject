package com.china.lhf.app.entity;

import com.china.lhf.app.Fragment.HomeFragment;
import com.china.lhf.app.Fragment.HotFragment;

/**
 * Created by C罗 on 2016/9/6.
 */
public class Tab {

    private int title;
    private int icon;
    private Class fragement;

    public Tab(Class fragement, int icon, int title) {
        this.fragement = fragement;
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class getFragement() {
        return fragement;
    }

    public void setFragement(Class fragement) {
        this.fragement = fragement;
    }
}
