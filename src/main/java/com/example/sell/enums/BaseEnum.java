package com.example.sell.enums;

import com.example.sell.exception.ExceptionCode;
import com.example.sell.exception.SellException;

public interface BaseEnum<E> {

    static <T extends BaseEnum,R> T fromCode(R code,Class<T> clazz){
        T[] enumConstants = clazz.getEnumConstants();
        if(enumConstants != null && enumConstants.length > 0) {
            for (T enumConstant : enumConstants) {
                if (enumConstant.getCode().equals(code)) {
                    return enumConstant;
                }
            }
        }
        throw new SellException(ExceptionCode.ENUM_CONVERT_EXP,
                String.format("【枚举转换】转换异常，clazz=%s，code=%s",clazz,code));
    };

    E getCode();
    String getName();
}
