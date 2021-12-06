package com.message.server;

import com.message.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayServer {
    @Autowired
    private OrderServer orderServer ;
    public void payOrder(Order order) {
      log.info("订单{}支付成功",order.getId());
      orderServer.orderPayedSuccess(order.getId());
    }
}
