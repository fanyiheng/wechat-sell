package com.example.sell.service;

import com.example.sell.dto.OrderDto;

public interface BuyerService {
    OrderDto findOrderOne(String openid,String orderId);
    OrderDto cancelOrder(String openid,String orderId);
}
