package com.message.mq.message;

import com.message.entity.Order;

public class MessageServer {
    /**
     * 发送订单取消消息
     * @param order
     */
    public void sendOrderCancelMessage(Order order) {
    }

    /**
     * 发送订单创建成功延时消息
     * @param order
     */
    public void sendOrderDelayMessage(Order order) {
    }

    /**
     * 发送订单支付成功消息
     * @param order
     */
    public void sendOrderPaySuccessMessage(Order order) {
    }
}
