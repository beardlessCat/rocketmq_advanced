package com.message.provider;

import com.message.entity.Order;
import com.message.mq.message.MessageServer;
import com.message.server.CouponServer;
import com.message.server.GoodsServer;
import com.message.server.OrderServer;
import com.message.server.PayServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ShopController {

    private CouponServer couponServer ;
    private GoodsServer goodsServer ;
    private OrderServer orderServer;
    private MessageServer messageServer ;
    private PayServer payServer ;
    @Autowired
    public ShopController(CouponServer couponServer, GoodsServer goodsServer, OrderServer orderServer, MessageServer messageServer, PayServer payServer) {
        this.couponServer = couponServer;
        this.goodsServer = goodsServer;
        this.orderServer = orderServer;
        this.messageServer = messageServer;
        this.payServer = payServer;
    }

    /**
     * 订单确认
     */
    @PostMapping("/confirmOrder")
    private Order confirmOrder() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Order order = orderServer.initialize();
        try {
            //2.库存扣减
            goodsServer.reduceGood(order);
            //3.优惠券扣减
            couponServer.useCoupon(order);
            //int x = 1/0 ;
            //4.生成订单
            order = orderServer.creatOrder(order);
            //5.订单生成成功

            //6.发送延时消息
            messageServer.sendOrderDelayMessage(order);
            log.info("{}订单确认成功",order.getId());
        } catch (Exception e) {
            log.error("订单：{}生成失败，发送取消消息",order.getId());
            //生成订单失败，发送回退消息，库存服务及优惠券服务进行回退
            messageServer.sendOrderCancelMessage(order);
        }

        return order;
    }

    /**
     * 订单支付
     */
    @PostMapping("/payOrder")
    private void payOrder(String orderId) {
        Order order = orderServer.getOrder(orderId);
        try {
            if(order!=null){
                //1.支付订单
                payServer.payOrder(order);
                //2.订单成功发送消息
                messageServer.sendOrderPaySuccessMessage(order);
            }
        }catch (Exception e){
            //支付失败
        }

    }
}
