package com.example.sell.converter;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.example.sell.exception.ExceptionCode;
import com.example.sell.exception.SellException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Date;

@Slf4j
public class Date2SecondFilter implements ValueFilter {
    @Override
    public Object process(Object object, String name, Object value) {
        Field field ;
        try {
            field = object.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
//            log.error("【JSON序列化】获取对象属性异常",e);
//            throw new SellException(ExceptionCode.SYS_EXP);
            return value;
        }
        if(field.isAnnotationPresent(Date2Second.class)){
            if(value instanceof java.util.Date){
                return ((Date) value).getTime()/1000;
            }
            log.error("【JSON序列化】Date2Second 只能作用于 java.util.Date 类型的属性");
            throw new SellException(ExceptionCode.SYS_EXP);
        }
        return value;
    }
}
