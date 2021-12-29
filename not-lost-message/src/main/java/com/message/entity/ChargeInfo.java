package com.message.entity;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ChargeInfo {
    private String toUser ;
    private BigDecimal amount;
    private String no;
}
