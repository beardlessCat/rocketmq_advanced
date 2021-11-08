package com.bigyj.coupon.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.bigyj.api.ICouponService;
import com.bigyj.coupon.mapper.TradeCouponMapper;
import com.bigyj.entity.Result;
import com.bigyj.pojo.TradeCoupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service
public class CouponServiceImpl implements ICouponService {

    private TradeCouponMapper tradeCouponMapper;
    @Autowired
    public CouponServiceImpl(TradeCouponMapper tradeCouponMapper) {
        this.tradeCouponMapper = tradeCouponMapper;
    }

    @Override
    public TradeCoupon findOne(Long coupouId) {
        return null;
    }

    @Override
    public Result updateCouponStatus(TradeCoupon coupon) {
        return null;
    }
}
