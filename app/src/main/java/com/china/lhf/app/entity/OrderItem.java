package com.china.lhf.app.entity;

/**
 * 订单列表
 */
public class OrderItem extends BaseBean {

    private Float amount;
    private Wares wares;

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Wares getWares() {
        return wares;
    }

    public void setWares(Wares wares) {
        this.wares = wares;
    }
}
