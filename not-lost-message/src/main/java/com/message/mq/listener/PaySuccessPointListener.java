package com.message.mq.listener;

import com.alibaba.fastjson.JSON;
import com.message.entity.Order;
import com.message.server.PointServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.order.topic.success}",consumerGroup = "${mq.order.consumer.group.point}",messageModel = MessageModel.CLUSTERING )
public class PaySuccessPointListener implements RocketMQListener<MessageExt> {
    AtomicInteger atomicInteger = new AtomicInteger(0);
    @Autowired
    private PointServer pointServer ;
    @SneakyThrows
    @Override
    public void onMessage(MessageExt messageExt) {
        String msgId = messageExt.getMsgId();
        log.info("收到订单支付成功消息{}",msgId);
        String body = new String(messageExt.getBody(), "UTF-8");
        Order order = JSON.parseObject(body, Order.class);
        try {
            log.info("积分消费者消费消息!");
            int x = 1/atomicInteger.getAndIncrement()-1 ;
            pointServer.distribute(order);
        }catch (Exception e){
            //抛出异常后，MQClient会返回ConsumeConcurrentlyStatus.RECONSUME_LATER进行重试，
            throw e;
        }
    }
}
