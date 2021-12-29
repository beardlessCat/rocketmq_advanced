package com.message.server;

import com.message.entity.ChargeInfo;
import com.message.mq.message.MessageServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ChargeServer {
    private static Map<String,ChargeInfo> chargeLogs= new ConcurrentHashMap();
    @Autowired
    private MessageServer messageServer ;
    public void charge(ChargeInfo chargeInfo) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        //记录操作日志
        chargeLogs.put(chargeInfo.getNo(),chargeInfo);
        //发送消息
        messageServer.sendCharMessage(chargeInfo);
    }

    /**
     * 查询充值结果
     * @param no
     * @return
     */
    public boolean getChargeResult(String no) {
        ChargeInfo chargeInfo = chargeLogs.get(no);
        if(chargeInfo!=null){
            BigDecimal amount = chargeInfo.getAmount();
            if(amount!=null){
                return true;
            }
        }
        return false;
    }
}
