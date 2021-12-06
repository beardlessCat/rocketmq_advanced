package com.message.server;

import com.message.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class GoodsServer {
    private static Integer total = 100 ;
    private ReentrantLock reentrantLock = new ReentrantLock();
    @Autowired
    private OrderServer orderServer ;
    /**
     * 库存扣减
     * @param order
     */
    public void reduceGood(Order order) {
        reentrantLock.lock();
        try {
            if(total>=order.getNum()){
                total = total-order.getNum();
            }
        }finally {
            reentrantLock.unlock();
        }
    }

    /**
     * 订单取消库存回退
     * @param order
     */
    public void rollBack(Order order) {
        total = total+order.getNum();
    }

    /**
     * 统计库存信息
     */
    public void goodsCount() {
        log.info("{}当前库存为：{}",Thread.currentThread().getName(),total);
    }

    /**
     *
     * @param order
     */
    public void orderDelay(Order order) {
        Order orderInfo = orderServer.getOrder(order.getId());
        if(orderInfo.getOrderStatus()!=2){
            this.rollBack(order);
        }else {
            log.info("订单{}已成功完成支付",orderInfo.getId());
        }
    }
}
