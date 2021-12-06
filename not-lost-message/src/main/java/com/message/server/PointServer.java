package com.message.server;

import com.message.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PointServer {
    public void distribute(Order order) {
        log.info("{}订单获得积分",order.getId());
    }
}
