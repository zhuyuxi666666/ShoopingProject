package com.china.lhf.app.msg;

/**
 * Created by Administrator on 2016/10/25 0025.
 */
public class CreateOrderRespMag  extends BaseRespMsg{
    private OrderResMsg data;

    public OrderResMsg getData() {
        return data;
    }

    public void setData(OrderResMsg data) {
        this.data = data;
    }
}
