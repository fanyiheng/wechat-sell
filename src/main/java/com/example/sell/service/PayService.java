package com.example.sell.service;

import com.example.sell.dto.OrderDto;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;

public interface PayService {
    PayResponse create(OrderDto orderDto);
    PayResponse notify(String notifyData);
    RefundResponse refund(OrderDto orderDto);
}
