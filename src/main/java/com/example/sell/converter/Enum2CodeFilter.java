package com.example.sell.converter;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.example.sell.enums.BaseEnum;
import com.example.sell.exception.ExceptionCode;
import com.example.sell.exception.SellException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class Enum2CodeFilter implements ValueFilter {
    @Override
    public Object process(Object object, String name, Object value) {
        Field field ;
        try {
            field = object.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
//            log.error("【JSON序列化】获取对象属性异常",e);
//            throw new SellException(ExceptionCode.ENUM_CONVERT_EXP);
            return value;
        }
        if(field.isAnnotationPresent(Enum2Code.class)){
            if(value instanceof BaseEnum){
                return ((BaseEnum) value).getCode();
            }
            log.error("【JSON序列化】Enum2Code 只能作用于 com.example.sell.converter.BaseEnum 类型的属性");
            throw new SellException(ExceptionCode.ENUM_CONVERT_EXP);
        }
        return value;
    }
}
