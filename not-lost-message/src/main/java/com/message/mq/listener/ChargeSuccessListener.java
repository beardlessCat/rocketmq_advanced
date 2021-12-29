package com.message.mq.listener;

import com.alibaba.fastjson.JSON;
import com.message.entity.ChargeInfo;
import com.message.entity.Order;
import com.message.server.AccountServer;
import com.message.server.PointServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.order.topic.charge}",consumerGroup = "${mq.order.consumer.group.charge}",messageModel = MessageModel.CLUSTERING )
public class ChargeSuccessListener implements RocketMQListener<MessageExt> {
    AtomicInteger atomicInteger = new AtomicInteger(0);
    @Autowired
    private AccountServer accountServer ;
    @SneakyThrows
    @Override
    public void onMessage(MessageExt messageExt) {
        String msgId = messageExt.getMsgId();
        log.info("收到充值成功消息{}",msgId);
        String body = new String(messageExt.getBody(), "UTF-8");
        ChargeInfo chargeInfo = JSON.parseObject(body, ChargeInfo.class);
        accountServer.updateAccount(chargeInfo);

    }
}
