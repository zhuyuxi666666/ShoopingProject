package com.china.lhf.app.entity;

/**
 * Created by C罗 on 2016/9/19.
 */
public class Campaign extends BaseBean {
         private String title;
         private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
