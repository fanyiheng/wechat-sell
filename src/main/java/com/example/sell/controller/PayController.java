package com.example.sell.controller;

import com.example.sell.dto.OrderDto;
import com.example.sell.service.OrderService;
import com.example.sell.service.impl.PayServiceImpl;
import com.lly835.bestpay.model.PayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayServiceImpl payService;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl){
//        OrderDto orderDto = orderService.findOne(orderId);
        PayResponse response = new PayResponse();//payService.create(orderDto);
        Map<String,Object> model = new HashMap<>();
        model.put("payResponse", response);
        model.put("returnUrl",returnUrl);
        return new ModelAndView("pay/create",model);
    }

    @PostMapping("/notify")
    public ModelAndView notify(String notifyData){
        payService.notify(notifyData);
        return new ModelAndView("pay/success");
    }

}
