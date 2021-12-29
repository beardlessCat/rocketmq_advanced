package com.message.server;

import com.message.entity.ChargeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class AccountServer {
    private CopyOnWriteArrayList<String> txs = new CopyOnWriteArrayList<>();
    private static BigDecimal totalAmount ;
    public void updateAccount(ChargeInfo chargeInfo) {
        String no = chargeInfo.getNo();
        //幂等校验
        if(txs.contains(no)){
            log.warn("事务已经执行！");
        }
        //更新账户金额
        totalAmount = chargeInfo.getAmount();
        //添加事务记录
        txs.add(no);
    }
}
