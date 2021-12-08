package com.message.mq.listener;

import com.alibaba.fastjson.JSON;
import com.message.entity.Order;
import com.message.server.OrderServer;
import com.message.server.PayServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQTransactionListener
public class OrderTXMsgListener implements RocketMQLocalTransactionListener {
    private PayServer payServer ;
    private OrderServer orderServer ;
    @Autowired
    public OrderTXMsgListener(PayServer payServer, OrderServer orderServer) {
        this.payServer = payServer;
        this.orderServer = orderServer;
    }

    // 执行本地事务
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object arg) {
        log.info(" TX message listener execute local transaction, message={},args={} ",message,arg);
        // 执行本地事务
        RocketMQLocalTransactionState result = RocketMQLocalTransactionState.COMMIT;
        try {
            String jsonString = new String((byte[]) message.getPayload());
            Order order = JSON.parseObject(jsonString, Order.class);
            //执行交费代码
            boolean paySuccess = payServer.payOrder(order);
            if(!paySuccess){
                result = RocketMQLocalTransactionState.ROLLBACK;
            }
        } catch (Exception e) {
            log.error(" exception message={} ",e.getMessage());
            result = RocketMQLocalTransactionState.UNKNOWN;
        }
        return result;
    }
    // 检查本地事务
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        log.info(" TX message listener check local transaction, message={} ",message.getPayload());
        RocketMQLocalTransactionState result = RocketMQLocalTransactionState.COMMIT;
        try {
          //检查本地事务
            String jsonString = new String((byte[]) message.getPayload());
            Order order = JSON.parseObject(jsonString, Order.class);
            Order orderInfo = orderServer.getOrder(order.getId());
            if(orderInfo == null ||orderInfo.getOrderStatus() != 2){
                result = RocketMQLocalTransactionState.ROLLBACK;
            }
        } catch (Exception e) {
            // 异常就回滚
            log.error(" exception message={} ",e.getMessage());
            result = RocketMQLocalTransactionState.ROLLBACK;
        }
        return result;
    }
}
