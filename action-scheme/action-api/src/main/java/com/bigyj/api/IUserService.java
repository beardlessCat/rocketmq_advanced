package com.bigyj.api;


import com.bigyj.entity.Result;
import com.bigyj.pojo.TradeUser;
import com.bigyj.pojo.TradeUserMoneyLog;

public interface IUserService {
    TradeUser findOne(Long userId);

    Result updateMoneyPaid(TradeUserMoneyLog userMoneyLog);
}
