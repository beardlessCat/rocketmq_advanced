package com.message.server;

import com.message.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NoticeServer {
    public void sendNotice(Order order) {
        log.info("{}订单支付成功，发送消息！",order.getId());
    }
}
