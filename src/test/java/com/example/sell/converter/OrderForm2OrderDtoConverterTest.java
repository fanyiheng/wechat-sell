package com.example.sell.converter;

import com.alibaba.fastjson.JSON;
import com.example.sell.entity.OrderDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderForm2OrderDtoConverterTest {

    @Test
    public void convert() {
        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProductId("123456");
        orderDetail.setProductQuantity(2);
        orderDetails.add(orderDetail);

//        String items = JSON.toJSONString(orderDetails);
        String items = "[{productId:\"123456\",productQuantity:2}]";
        String items2 = "[{productId: \"1423113435324\",productQuantity: 2 }]";
        System.out.println(items2);
        List<OrderDetail> orderDetailNews =  JSON.parseObject(items2,List.class);
        System.out.println(orderDetailNews);

    }
}
