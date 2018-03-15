package com.example.sell.controller;

import com.example.sell.converter.OrderForm2OrderDtoConverter;
import com.example.sell.dto.OrderDto;
import com.example.sell.exception.ExceptionCode;
import com.example.sell.exception.SellException;
import com.example.sell.form.OrderForm;
import com.example.sell.service.impl.BuyerServiceImpl;
import com.example.sell.service.impl.OrderServiceImpl;
import com.example.sell.util.ResultFactory;
import com.example.sell.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {
    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private BuyerServiceImpl buyerService;

    //创建订单
    @PostMapping("/create")
    public Result<Map<String, String>> create(@Valid OrderForm orderForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】参数不正确，orderForm={}", orderForm);
            throw new SellException(ExceptionCode.PARAM_ERROR, bindingResult.getFieldError().getDefaultMessage());
        }
        OrderDto orderDto = OrderForm2OrderDtoConverter.convert(orderForm);
        if (CollectionUtils.isEmpty(orderDto.getOrderDetails())) {
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ExceptionCode.CAR_EMPTY);
        }
        OrderDto createdOrderDto = orderService.create(orderDto);

        Map<String, String> createdResult = Collections.singletonMap("orderId", createdOrderDto.getOrderId());
        return ResultFactory.success(createdResult);
    }


    //查询订单列表
    @GetMapping("list")
    public Result<List<OrderDto>> list(@RequestParam("openid") String openid,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (StringUtils.isEmpty(openid)) {
            log.error("【查询订单列表】openid 为空");
            throw new SellException(ExceptionCode.PARAM_ERROR);
        }
        PageRequest pageRequest = new PageRequest(page, size);
        Page<OrderDto> orderDtos = orderService.findList(openid, pageRequest);
        return ResultFactory.success(orderDtos.getContent());
    }

    //查询订单详情
    @GetMapping("/detail")
    public Result<OrderDto> detail(@RequestParam("openid") String openid,@RequestParam("orderId") String orderId){
        OrderDto orderDto = buyerService.findOrderOne(openid, orderId);
        return ResultFactory.success(orderDto);
    }

    //取消订单
    @PostMapping("/cancel")
    public Result cancel(@RequestParam("openid") String openid,@RequestParam("orderId") String orderId){
        buyerService.cancelOrder(openid, orderId);
        return ResultFactory.success();
    }
}
