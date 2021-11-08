package com.bigyj.pay.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.bigyj.api.IPayService;
import com.bigyj.entity.Result;
import com.bigyj.pay.mapper.TradeMqProducerTempMapper;
import com.bigyj.pojo.TradePay;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service
public class PayServiceImpl implements IPayService{
    private TradeMqProducerTempMapper tradeMqProducerTempMapper ;
    @Autowired
    public PayServiceImpl(TradeMqProducerTempMapper tradeMqProducerTempMapper) {
        this.tradeMqProducerTempMapper = tradeMqProducerTempMapper;
    }

    @Override
    public Result createPayment(TradePay tradePay) {
        return null;
    }

    @Override
    public Result callbackPayment(TradePay tradePay) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        return null;
    }
}
