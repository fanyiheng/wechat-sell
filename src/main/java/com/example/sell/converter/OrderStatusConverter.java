package com.example.sell.converter;

import com.example.sell.enums.BaseEnum;
import com.example.sell.enums.OrderStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class OrderStatusConverter implements AttributeConverter<OrderStatus,Integer>{
    @Override
    public Integer convertToDatabaseColumn(OrderStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public OrderStatus convertToEntityAttribute(Integer code) {
        return BaseEnum.fromCode(code,OrderStatus.class);
    }
}
