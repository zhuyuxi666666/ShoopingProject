package com.china.lhf.app.entity;

import java.io.Serializable;

/**
 * Created by C罗 on 2016/9/14.
 */
public class BaseBean implements Serializable {
       protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
