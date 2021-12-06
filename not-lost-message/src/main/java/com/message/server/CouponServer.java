package com.message.server;

import com.message.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class CouponServer {
    private static List<String> userCoupon= new ArrayList<>();
    private ReentrantLock reentrantLock = new ReentrantLock();
    static {
        userCoupon.add("1");
        userCoupon.add("2");
        userCoupon.add("3");
    }
    @Autowired
    private OrderServer orderServer ;
    /**
     * 优惠券扣减
     * @param order
     */
    public void useCoupon(Order order) {
        reentrantLock.lock();
        try {
            String couponId = order.getCouponId();
            if(!StringUtils.isEmpty(couponId)){
                userCoupon.remove(couponId);
            }
        }finally {
            reentrantLock.unlock();
        }
    }

    /**
     * 优惠券回退
     * @param order
     */
    public void rollBack(Order order) {
        String couponId = order.getCouponId();
        userCoupon.add(couponId);
        log.info("优惠券回退成功！");
    }

    /**
     * 统计当前优惠券
     */
    public void couponCount() {
        log.info("{}当前剩余优惠券数量为：{}，优惠券列表：{}",Thread.currentThread().getName(),userCoupon.size(),userCoupon.toString());
    }

    public void delayOrder(Order order) {
        Order orderInfo = orderServer.getOrder(order.getId());
        if(orderInfo.getOrderStatus()!=2){
            this.rollBack(order);
        }else {
            log.info("订单{}已成功完成支付",orderInfo.getId());
        }
    }
}
