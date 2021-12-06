package com.message.runner;

import com.message.entity.Coupon;
import com.message.factory.ThreadFactoryImpl;
import com.message.server.CouponServer;
import com.message.server.GoodsServer;
import com.message.server.OrderServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Autowired
    private CouponServer couponServer ;
    @Autowired
    private GoodsServer goodsServer ;
    private final ScheduledExecutorService scheduledExecutorService
            = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl("OrderScheduledThread"));
    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                couponServer.couponCount();
                goodsServer.goodsCount();
            }
        }, 20, 20, TimeUnit.SECONDS);
    }
}
