package com.bigyj.order.servcie;

import com.alibaba.dubbo.config.annotation.Service;
import com.bigyj.api.ICouponService;
import com.bigyj.api.IGoodsService;
import com.bigyj.api.IOrderService;
import com.bigyj.api.IUserService;
import com.bigyj.entity.Result;
import com.bigyj.order.mapper.TradeOrderMapper;
import com.bigyj.pojo.TradeGoods;
import com.bigyj.pojo.TradeOrder;
import com.bigyj.pojo.TradeUser;
import com.bogyj.common.constant.ShopCode;
import com.bogyj.common.exception.CastException;
import jdk.nashorn.internal.ir.annotations.Reference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Service
public class OrderServiceImpl implements IOrderService {
    private TradeOrderMapper tradeOrderMapper;
    @Autowired
    public OrderServiceImpl(TradeOrderMapper tradeOrderMapper) {
        this.tradeOrderMapper = tradeOrderMapper;
    }

    @Reference
    private IGoodsService goodsService;

    @Reference
    private IUserService userService;

    @Reference
    private ICouponService couponService;
    @Override
    public Result confirmOrder(TradeOrder order) {
        //1.校验订单
        checkOrder(order);
        //2.生成预订单
        try {
            //3.扣减库存
            //4.扣减优惠券
            //5.使用余额

            //模拟异常抛出
            //CastException.cast(ShopCode.SHOP_FAIL);

            //6.确认订单
            //7.返回成功状态
            return new Result(ShopCode.SHOP_SUCCESS.getSuccess(),ShopCode.SHOP_SUCCESS.getMessage());
        } catch (Exception e) {
            //1.确认订单失败,发送消息

            //2.返回订单确认失败消息
        }
        return null;
    }
    /**
     * 校验订单
     *
     * @param order
     */
    private void checkOrder(TradeOrder order) {
        //1.校验订单是否存在
        if (order == null) {
            CastException.cast(ShopCode.SHOP_ORDER_INVALID);
        }
        //2.校验订单中的商品是否存在
        TradeGoods goods = goodsService.findOne(order.getGoodsId());
        if (goods == null) {
            CastException.cast(ShopCode.SHOP_GOODS_NO_EXIST);
        }
        //3.校验下单用户是否存在
        TradeUser user = userService.findOne(order.getUserId());
        if (user == null) {
            CastException.cast(ShopCode.SHOP_USER_NO_EXIST);
        }
        //4.校验商品单价是否合法
        if (order.getGoodsPrice().compareTo(goods.getGoodsPrice()) != 0) {
            CastException.cast(ShopCode.SHOP_GOODS_PRICE_INVALID);
        }
        //5.校验订单商品数量是否合法
        if (order.getGoodsNumber() >= goods.getGoodsNumber()) {
            CastException.cast(ShopCode.SHOP_GOODS_NUM_NOT_ENOUGH);
        }

        log.info("校验订单通过");

    }
}
