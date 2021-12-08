package com.message.mq.listener;

import com.alibaba.fastjson.JSON;
import com.message.entity.Order;
import com.message.server.GoodsServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RocketMQMessageListener(topic = "${mq.order.topic.delay}",consumerGroup = "${mq.order.consumer.group.delay.goods}",messageModel = MessageModel.CLUSTERING )
public class OrderTimeoutGoodsListener implements RocketMQListener<MessageExt> {
    @Autowired
    private GoodsServer goodsServer;
    @SneakyThrows
    @Override
    public void onMessage(MessageExt messageExt) {
        String msgId = messageExt.getMsgId();
        log.info("收到订单延时取消消息{}，取消订单",msgId);
        String body = new String(messageExt.getBody(), "UTF-8");
        Order order = JSON.parseObject(body, Order.class);
        goodsServer.orderDelay(order);
    }
}
