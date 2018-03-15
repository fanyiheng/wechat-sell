package com.example.sell.entity;

import com.example.sell.converter.OrderStatusConverter;
import com.example.sell.converter.PayStatusConverter;
import com.example.sell.enums.OrderStatus;
import com.example.sell.enums.PayStatus;
import lombok.Data;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class OrderMaster {
    @Id
    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerOpenid;
    private BigDecimal orderAmount;
    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus orderStatus = OrderStatus.NEW;
    @Convert(converter = PayStatusConverter.class)
    private PayStatus payStatus = PayStatus.WAIT;
    private Date createTime;
    private Date updateTime;
}
