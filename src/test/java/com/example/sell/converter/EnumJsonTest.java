package com.example.sell.converter;

import com.alibaba.fastjson.JSON;
import com.example.sell.enums.OrderStatus;
import org.junit.Test;

public class EnumJsonTest {

    @Test
    public void enum2Json(){
        System.out.println(JSON.toJSONString(OrderStatus.NEW));
    }
}
