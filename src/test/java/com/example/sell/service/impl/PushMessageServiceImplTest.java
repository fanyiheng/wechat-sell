package com.example.sell.service.impl;

import com.example.sell.dto.OrderDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PushMessageServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private PushMessageServiceImpl pushMessageService;
    @Test
    public void onOrderStatusChanged() {
        OrderDto orderDto = orderService.findOne("1518327129263277173");
        pushMessageService.onOrderStatusChanged(orderDto);
    }
}
