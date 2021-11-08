package com.bigyj.api;


import com.bigyj.entity.Result;
import com.bigyj.pojo.TradeOrder;

public interface IOrderService {

    /**
     * 下单接口
     * @param order
     * @return
     */
    public Result confirmOrder(TradeOrder order);

}
