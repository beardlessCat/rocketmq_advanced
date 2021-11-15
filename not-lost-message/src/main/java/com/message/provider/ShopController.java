package com.message.provider;

import com.message.entity.Order;
import com.message.mq.message.MessageServer;
import com.message.server.*;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

public class ShopController {

    private RocketMQTemplate rocketMQTemplate;
    private CouponServer couponServer ;
    private GoodsServer goodsServer ;
    private NoticeServer noticeServer ;
    private OrderServer orderServer;
    private PointServer pointServer ;
    private MessageServer messageServer ;
    private PayServer payServer ;


    /**
     * 订单确认
     */
    private Order comfirmOrder() {
        Order order = new Order();
        try {
            //1.初始化订单(预订单)
            order = orderServer.initialize();
            //2.库存扣减
            goodsServer.reduceGood();
            //3.优惠券扣减
            couponServer.useCoupom();
            //4.生成订单
            order = orderServer.creatOrder();
        } catch (Exception e) {
            //生成订单失败，发送回退消息，库存服务及优惠券服务进行回退
            messageServer.sendOrderCancelMessage(order);
        }
        //5.订单生成成功

        //6.发送延时消息
        messageServer.sendOrderDelayMessage(order);
        return order;
    }

    /**
     * 订单支付
     */
    private void payOrder(Order order) {
        try {
            payServer.payOrder();
        }catch (Exception e){
            //支付失败
        }
        messageServer.sendOrderPaySuccessMessage(order);
        //删除延时队列消息
    }
}
