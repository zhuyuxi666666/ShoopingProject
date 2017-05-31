package com.china.lhf.app.entity;

/**
 * Created by C罗 on 2016/9/18.
 */
public class Banner extends BaseBean {
       private String name;
       private String imgUrl;
       private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
