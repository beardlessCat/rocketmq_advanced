package com.bigyj.goods.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.bigyj.api.IGoodsService;
import com.bigyj.entity.Result;
import com.bigyj.goods.mapper.TradeGoodsMapper;
import com.bigyj.goods.mapper.TradeGoodsNumberLogMapper;
import com.bigyj.goods.mapper.TradeMqConsumerLogMapper;
import com.bigyj.pojo.TradeGoods;
import com.bigyj.pojo.TradeGoodsNumberLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service
public class GoodsServiceImpl implements IGoodsService {
    private TradeGoodsMapper tradeGoodsMapper;
    private TradeGoodsNumberLogMapper tradeGoodsNumberLogMapper;
    private TradeMqConsumerLogMapper tradeMqConsumerLogMapper;
    @Autowired
    public GoodsServiceImpl(TradeGoodsMapper tradeGoodsMapper, TradeGoodsNumberLogMapper tradeGoodsNumberLogMapper, TradeMqConsumerLogMapper tradeMqConsumerLogMapper) {
        this.tradeGoodsMapper = tradeGoodsMapper;
        this.tradeGoodsNumberLogMapper = tradeGoodsNumberLogMapper;
        this.tradeMqConsumerLogMapper = tradeMqConsumerLogMapper;
    }

    @Override
    public TradeGoods findOne(Long goodsId) {
        return null;
    }

    @Override
    public Result reduceGoodsNum(TradeGoodsNumberLog goodsNumberLog) {
        return null;
    }
}
