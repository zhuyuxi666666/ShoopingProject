package com.china.lhf.app.entity;

/**
 *
 */
public class Favorites extends BaseBean {

    private Long createTime;
    private Wares wares;

    public Favorites() {
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Wares getWares() {
        return wares;
    }

    public void setWares(Wares wares) {
        this.wares = wares;
    }
}
