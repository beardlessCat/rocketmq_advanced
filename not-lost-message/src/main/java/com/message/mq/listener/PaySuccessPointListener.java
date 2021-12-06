package com.message.mq.listener;

import com.alibaba.fastjson.JSON;
import com.message.entity.Order;
import com.message.server.PointServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.order.topic.success}",consumerGroup = "${mq.order.consumer.group.point}",messageModel = MessageModel.CLUSTERING )
public class PaySuccessPointListener implements RocketMQListener<MessageExt> {
    @Autowired
    private PointServer pointServer ;
    @SneakyThrows
    @Override
    public void onMessage(MessageExt messageExt) {
        String msgId = messageExt.getMsgId();
        log.info("收到订单支付成功消息{}",msgId);
        String body = new String(messageExt.getBody(), "UTF-8");
        Order order = JSON.parseObject(body, Order.class);
        pointServer.distribute(order);
    }
}
