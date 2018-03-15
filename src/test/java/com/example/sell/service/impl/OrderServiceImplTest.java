package com.example.sell.service.impl;

import com.example.sell.dto.OrderDto;
import com.example.sell.entity.OrderDetail;
import com.example.sell.enums.OrderStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    private final String buyerOpenid = "110110";

    @Test
    public void create() {
        OrderDto orderDto = new OrderDto();
        orderDto.setBuyerName("小明");
        orderDto.setBuyerAddress("中国");
        orderDto.setBuyerPhone("16620806546");
        orderDto.setBuyerOpenid(buyerOpenid);

        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProductId("123456");
        orderDetail.setProductQuantity(2);
        orderDetails.add(orderDetail);

        orderDetail = new OrderDetail();
        orderDetail.setProductId("123457");
        orderDetail.setProductQuantity(2);
        orderDetails.add(orderDetail);
        orderDto.setOrderDetails(orderDetails);

        orderDto = orderService.create(orderDto);
        Assert.assertNotNull(orderDto);
    }

    @Test
    public void findOne() {
        OrderDto orderDto = orderService.findOne("123456");
        Assert.assertNotNull(orderDto.getOrderId());
        Assert.assertNotNull(orderDto.getOrderDetails().get(0).getDetailId());
    }

    @Test
    public void findList() {
        Page<OrderDto> orderDtos = orderService.findList(buyerOpenid, new PageRequest(0, 3));
        System.out.println(orderDtos.getContent());
    }

    @Test
    public void cancel() {
        OrderDto orderDto = orderService.findOne("1518327129263277173");
        OrderDto cancelOrderDto = orderService.cancel(orderDto);
        Assert.assertEquals(cancelOrderDto.getOrderStatus(), OrderStatus.CANCEL);
    }

    @Test
    public void finish() {
        OrderDto orderDto = orderService.findOne("1518327129263277173");
        OrderDto finishedOrderDto = orderService.finish(orderDto);
        Assert.assertEquals(finishedOrderDto.getOrderStatus(), OrderStatus.FINISHED);
    }

    @Test
    public void paid() {
    }

    @Test
    public void findListSeller(){
        Page<OrderDto> orderDtos = orderService.findList(new PageRequest(0, 3));
        List<OrderDto> content = orderDtos.getContent();
        System.out.println(content);
        Assert.assertNotNull(content);
    }
}
