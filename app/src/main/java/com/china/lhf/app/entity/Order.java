package com.china.lhf.app.entity;

import java.util.List;

/**
 * 订单
 */
public class Order extends BaseBean {

    public static final int STATUS_PAY_SUCCESS = 1; //支付成功的订单
    public static final int STATUS_PAY_FAIL = -1;    //支付失败的订单
    public static final int STATUS_PAY_WAIT = 0;    //待支付的订单
    public static final int STATUS_PAY_CANCEL = -2;    //取消支付的订单

    private String orderNum;    //订单号
    private Long createdTime;       //创建时间
    private Float amount;   //金额
    private int status; //  订单状态
    private List<OrderItem> items;
    private Address address;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}
