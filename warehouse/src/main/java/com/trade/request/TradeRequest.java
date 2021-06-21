package com.trade.request;

import com.trade.domain.Type;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TradeRequest {
    private String companyName;
    private String time;
    private String productName;
    private Integer amount;
    private BigDecimal price;
    private Type type;
}
