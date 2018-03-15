package com.example.sell.controller;

import com.example.sell.dto.OrderDto;
import com.example.sell.enums.Result;
import com.example.sell.service.impl.OrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/seller/order")
@Slf4j
public class SellerOrderController {
    @Autowired
    private OrderServiceImpl orderService;

    @RequestMapping("/list")
    public ModelAndView list(@RequestParam(value="page",defaultValue = "1") Integer page,
                             @RequestParam(value="size",defaultValue = "10") Integer size){
        PageRequest request = new PageRequest(page-1,size);
        Page<OrderDto> orderDtos = orderService.findList(request);
        Map<String,Object> model = new HashMap<>();
        model.put("orderDtos", orderDtos);
        model.put("currentPage", page);
        model.put("size", size);
        return new ModelAndView("/order/list",model);
    }

    @RequestMapping("/cancel")
    public ModelAndView cancel(@RequestParam("orderId") String orderId){
        Map<String,Object> model = new HashMap<>();
        try {
            OrderDto orderDto =  orderService.findOne(orderId);
            orderService.cancel(orderDto);
        } catch (Exception e) {
            log.error("【卖家端取消订单】发生异常");
            model.put("msg", e.getMessage());
            model.put("url", "/sell/seller/order/list");
            return new ModelAndView("/common/error",model);
        }
        model.put("msg", Result.SUCCESS.getName());
        model.put("url", "/sell/seller/order/list");
        return new ModelAndView("/common/success",model);
    }

    @RequestMapping("/finish")
    public ModelAndView finish(@RequestParam("orderId") String orderId){
        Map<String,Object> model = new HashMap<>();
        try {
            OrderDto orderDto =  orderService.findOne(orderId);
            orderService.finish(orderDto);
        } catch (Exception e) {
            log.error("【卖家端取消订单】发生异常");
            model.put("msg", e.getMessage());
            model.put("url", "/sell/seller/order/list");
            return new ModelAndView("/common/error",model);
        }
        model.put("msg", Result.SUCCESS.getName());
        model.put("url", "/sell/seller/order/list");
        return new ModelAndView("/common/success",model);
    }

    @RequestMapping("/detail")
    public ModelAndView detail(@RequestParam("orderId") String orderId){
        OrderDto orderDto = null;
        Map<String,Object> model = new HashMap<>();
        try {
            orderDto =  orderService.findOne(orderId);
        } catch (Exception e) {
            log.error("【卖家端取消订单】发生异常");
            model.put("msg", e.getMessage());
            model.put("url", "/sell/seller/order/list");
            return new ModelAndView("/common/error",model);
        }
        model.put("orderDto", orderDto);
        model.put("url", "/sell/seller/order/list");
        return new ModelAndView("/order/detail",model);
    }

}
