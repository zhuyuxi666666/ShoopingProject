package com.china.lhf.app.msg;

/**
 * Created by Administrator on 2016/10/25 0025.
 */
public class OrderResMsg{
    private String orderNum;
    private String charge;

    public OrderResMsg(String orderNum, String charge) {
        this.orderNum = orderNum;
        this.charge = charge;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }
}
