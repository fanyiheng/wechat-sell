package com.example.sell.dto;

import com.example.sell.converter.Date2Second;
import com.example.sell.converter.Enum2Code;
import com.example.sell.entity.OrderDetail;
import com.example.sell.enums.OrderStatus;
import com.example.sell.enums.PayStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Convert;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
public class OrderDto {
    @Id
    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerOpenid;
    private BigDecimal orderAmount;
    @Enum2Code
    private OrderStatus orderStatus;
    @Enum2Code
    private PayStatus payStatus;
    @Date2Second
    private Date createTime;
    @Date2Second
    private Date updateTime;
    private List<OrderDetail> orderDetails;
}
