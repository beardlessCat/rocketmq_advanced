package com.message.mq.listener;

import com.alibaba.fastjson.JSON;
import com.message.entity.Goods;
import com.message.entity.Order;
import com.message.server.GoodsServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.order.topic.cancel}",consumerGroup = "${mq.order.consumer.group.goods}",messageModel = MessageModel.CLUSTERING )
public class OrderCancelGoodsListener implements RocketMQListener<MessageExt> {
    @Autowired
    private GoodsServer goodsServer ;
    @Override
    public void onMessage(MessageExt messageExt) {
        try {
            //1. 解析消息内容
            String msgId = messageExt.getMsgId();
            log.info("收到订单取消消息{}",msgId);
            String body = new String(messageExt.getBody(), "UTF-8");
            Order order = JSON.parseObject(body, Order.class);
            goodsServer.rollBack(order);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
