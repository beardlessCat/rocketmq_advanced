package com.bigyj.api;

import com.bigyj.entity.Result;
import com.bigyj.pojo.TradePay;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;

public interface IPayService {

    public Result createPayment(TradePay tradePay);

    public Result callbackPayment(TradePay tradePay) throws InterruptedException, RemotingException, MQClientException, MQBrokerException;

}
