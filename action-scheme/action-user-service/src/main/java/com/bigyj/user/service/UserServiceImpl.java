package com.bigyj.user.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.bigyj.api.IUserService;
import com.bigyj.entity.Result;
import com.bigyj.pojo.TradeUser;
import com.bigyj.pojo.TradeUserMoneyLog;
import com.bigyj.user.mapper.TradeUserMapper;
import com.bigyj.user.mapper.TradeUserMoneyLogMapper;
import org.springframework.stereotype.Component;

@Component
@Service
public class UserServiceImpl implements IUserService {
    private TradeUserMapper tradeUserMapper;
    private TradeUserMoneyLogMapper tradeUserMoneyLogMapper;

    public UserServiceImpl(TradeUserMapper tradeUserMapper, TradeUserMoneyLogMapper tradeUserMoneyLogMapper) {
        this.tradeUserMapper = tradeUserMapper;
        this.tradeUserMoneyLogMapper = tradeUserMoneyLogMapper;
    }

    @Override
    public TradeUser findOne(Long userId) {
        return null;
    }

    @Override
    public Result updateMoneyPaid(TradeUserMoneyLog userMoneyLog) {
        return null;
    }
}
