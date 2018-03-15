package com.example.sell.converter;

import com.alibaba.fastjson.JSON;
import com.example.sell.dto.OrderDto;
import com.example.sell.entity.OrderDetail;
import com.example.sell.exception.ExceptionCode;
import com.example.sell.exception.SellException;
import com.example.sell.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class OrderForm2OrderDtoConverter {
    public static OrderDto convert(OrderForm orderForm){
        OrderDto orderDto = new OrderDto();
        orderDto.setBuyerName(orderForm.getName());
        orderDto.setBuyerPhone(orderForm.getPhone());
        orderDto.setBuyerAddress(orderForm.getAddress());
        orderDto.setBuyerOpenid(orderForm.getOpenid());
        List<OrderDetail> orderDetails ;
        String items = orderForm.getItems();
        try {
            orderDetails =  JSON.parseArray(items,OrderDetail.class);
        }catch (Exception e){
            log.error("【对象转换】错误，string={}",orderForm.getItems());
            throw new SellException(ExceptionCode.PARAM_ERROR);
        }
        orderDto.setOrderDetails(orderDetails);
        return orderDto;
    }
}
