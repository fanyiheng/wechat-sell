package com.example.sell.service.impl;

import com.example.sell.dto.OrderDto;
import com.example.sell.exception.ExceptionCode;
import com.example.sell.exception.SellException;
import com.example.sell.service.BuyerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {
    @Autowired
    private OrderServiceImpl orderService;
    @Override
    public OrderDto findOrderOne(String openid, String orderId) {
        OrderDto orderDto = checkOrderOwner(openid, orderId);
        return orderDto;
    }

    @Override
    public OrderDto cancelOrder(String openid, String orderId) {
        OrderDto orderDto = checkOrderOwner(openid, orderId);
        if(!orderDto.getBuyerOpenid().equals(openid)){
            log.error("【订单查询】订单不存在,orderId={}",orderId);
            throw new SellException(ExceptionCode.ORDER_NOT_EXIST);
        }
        orderService.cancel(orderDto);
        return null;
    }

    private OrderDto checkOrderOwner(String openid,String orderId){
        OrderDto orderDto = orderService.findOne(orderId);
        if(orderDto == null){
           return null;
        }
        if(!orderDto.getBuyerOpenid().equals(openid)){
            log.error("【订单查询】订单的openid不一致,openid={},orderDto={}",openid,orderDto);
            throw new SellException(ExceptionCode.ORDER_OWER_ERROR);
        }
        return orderDto;
    }

}
