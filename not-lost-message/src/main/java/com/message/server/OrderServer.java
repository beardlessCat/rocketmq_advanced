package com.message.server;

import com.message.entity.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServer {
    private static Map<String,Order> orders = new HashMap<>() ;

    /**
     * 订单创建完成
     * @param order
     * @return
     */
    public Order creatOrder(Order order) {
        Order orderConfirmed = order.setOrderStatus(1);
        orders.put(orderConfirmed.getId(),orderConfirmed);
        return orderConfirmed;
    }

    /**
     * 订单初始化
     * @return
     */
    public Order initialize() {
        String orderId = UUID.randomUUID().toString().replace("-", "");
        Order order = new Order()
                .setId(orderId)
                .setUserId("00001")
                .setOrderAmount(new BigDecimal(1009))
                .setOrderStatus(0)
                .setNum(5)
                .setCouponId("1");
        return order;
    }

    /**
     * 获取订单
     * @param orderId
     * @return
     */
    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    /**
     * 订单支付成功
     * @param id
     */
    public boolean orderPayedSuccess(String id) {
        Order order = orders.get(id).setOrderStatus(2);
        if(order.getOrderStatus() == 2){
            return true ;
        }
        return false;
    }
}
