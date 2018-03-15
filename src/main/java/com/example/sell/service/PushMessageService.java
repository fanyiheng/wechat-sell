package com.example.sell.service;

import com.example.sell.dto.OrderDto;

public interface PushMessageService {
    void onOrderStatusChanged(OrderDto orderDto);
}
