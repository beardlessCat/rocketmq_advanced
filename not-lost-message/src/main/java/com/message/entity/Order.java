package com.message.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class Order {
    private String id ;
    private String userId ;
    private BigDecimal orderAmount;
    private Integer orderStatus;
    private Integer num ;
    private String couponId ;
}
