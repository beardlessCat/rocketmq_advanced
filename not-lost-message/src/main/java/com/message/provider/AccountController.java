package com.message.provider;

import com.message.entity.ChargeInfo;
import com.message.server.ChargeServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AccountController {

    private ChargeServer chargeServer ;
    @Autowired
    public AccountController(ChargeServer chargeServer) {
        this.chargeServer = chargeServer;
    }

    /**
     * 重置
     */
    @PostMapping("/charge")
    private void charge() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        ChargeInfo chargeInfo = new ChargeInfo();
        chargeServer.charge(chargeInfo);
    }
    //查询充值结果
    @GetMapping("/getChargeResult/{no}")
    private boolean getChargeResult(@PathVariable String no) {
        return chargeServer.getChargeResult(no);
    }
}

