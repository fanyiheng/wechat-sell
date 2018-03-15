package com.example.sell.converter;

import com.example.sell.enums.BaseEnum;
import com.example.sell.enums.ProductStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ProductStatusConverter implements AttributeConverter<ProductStatus,Integer> {
    @Override
    public Integer convertToDatabaseColumn(ProductStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public ProductStatus convertToEntityAttribute(Integer code) {
        return BaseEnum.fromCode(code,ProductStatus.class);
    }
}
