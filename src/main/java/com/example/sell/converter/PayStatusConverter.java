package com.example.sell.converter;

import com.example.sell.enums.BaseEnum;
import com.example.sell.enums.PayStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PayStatusConverter implements AttributeConverter<PayStatus,Integer> {
    @Override
    public Integer convertToDatabaseColumn(PayStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public PayStatus convertToEntityAttribute(Integer code) {
        return BaseEnum.fromCode(code,PayStatus.class);
    }
}
