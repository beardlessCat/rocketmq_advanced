package com.message.mq.message;

import com.alibaba.fastjson.JSON;
import com.message.entity.Order;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class MessageServer {
    @Value("${mq.order.topic.cancel}")
    private String cancelTopic;
    @Value("${mq.order.topic.success}")
    private String successTopic;
    @Value("${mq.order.topic.delay}")
    private String delayTopic;

    private RocketMQTemplate rocketMQTemplate ;
    @Autowired
    public MessageServer(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    /**
     * 发送订单取消消息
     * @param order
     */
    public void sendOrderCancelMessage(Order order) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Message message = new Message(cancelTopic,"",order.getId(),JSON.toJSONString(order).getBytes(StandardCharsets.UTF_8));
        rocketMQTemplate.getProducer().send(message);
    }

    /**
     * 发送订单创建成功延时消息
     * @param order
     */
    public void sendOrderDelayMessage(Order order) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Message message = new Message(delayTopic,"",order.getId(),JSON.toJSONString(order).getBytes(StandardCharsets.UTF_8));
        message.setDelayTimeLevel(3);
        rocketMQTemplate.getProducer().send(message);
    }

    /**
     * 发送订单支付成功消息
     * @param order
     */
    public void sendOrderPaySuccessMessage(Order order) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Message message = new Message(successTopic,"",order.getId(),JSON.toJSONString(order).getBytes(StandardCharsets.UTF_8));
        rocketMQTemplate.getProducer().send(message);
    }
}
