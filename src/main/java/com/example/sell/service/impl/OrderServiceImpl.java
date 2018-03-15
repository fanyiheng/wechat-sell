package com.example.sell.service.impl;

import com.example.sell.dto.CarDto;
import com.example.sell.dto.OrderDto;
import com.example.sell.entity.OrderDetail;
import com.example.sell.entity.OrderMaster;
import com.example.sell.entity.Product;
import com.example.sell.exception.ExceptionCode;
import com.example.sell.enums.OrderStatus;
import com.example.sell.enums.PayStatus;
import com.example.sell.exception.SellException;
import com.example.sell.repository.OrderDetailRepository;
import com.example.sell.repository.OrderMasterRepository;
import com.example.sell.service.OrderService;
import com.example.sell.util.KeyUtil;
import com.example.sell.util.ObjConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private PayServiceImpl payService;

    @Autowired
    private PushMessageServiceImpl pushMessageService;

    @Autowired
    private WebSocket webSocket;

    @Override
    @Transactional
    public OrderDto create(OrderDto orderDto) {
        BigDecimal orderAmount = BigDecimal.ZERO;
        String orderId = KeyUtil.genUniqueKey();
        //1.获取商品价格和数量,2.写入订单详情
        for (OrderDetail orderDetail : orderDto.getOrderDetails()) {
            Product product = productService.findOne(orderDetail.getProductId());
            if (product == null) {
                log.error("【创建订单】商品不存在，orderDto={}",orderDto);
                throw new SellException(ExceptionCode.PRODUCT_NOT_EXIST);
            }
            orderAmount = product.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAmount);

            BeanUtils.copyProperties(product, orderDetail);
            orderDetail.setOrderId(orderId);
            orderDetail.setDetailId(KeyUtil.genUniqueKey());

            orderDetailRepository.save(orderDetail);
        }
        //3.写入订单主信息
        OrderMaster orderMaster = new OrderMaster();
        orderDto.setOrderId(orderId);
        BeanUtils.copyProperties(orderDto, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatus.NEW);
        orderMaster.setPayStatus(PayStatus.WAIT);
        orderMasterRepository.save(orderMaster);

        List<CarDto> carDtos =
                orderDto.getOrderDetails().stream().map(e -> new CarDto(e.getProductId(), e.getProductQuantity())).collect(Collectors.toList()
                );
        //4.减库存
        productService.decreaseStock(carDtos);
        //websocket 广播消息到买家端
        webSocket.sendMessage("新订单通知");
        return orderDto;
    }

    @Override
    public OrderDto findOne(String orderId) {
        OrderDto orderDto = new OrderDto();
        OrderMaster orderMaster = orderMasterRepository.findOne(orderId);
        if(orderMaster == null){
            log.error("【订单查询】订单不存在，orderId={}", orderId);
            throw new SellException(ExceptionCode.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        BeanUtils.copyProperties(orderMaster, orderDto);
        orderDto.setOrderDetails(orderDetails);
        return orderDto;
    }

    @Override
    public Page<OrderDto> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasters = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
        Page<OrderDto> orderDtos = ObjConverter.convert(orderMasters, pageable, OrderDto.class);
        return orderDtos;
    }

    @Override
    @Transactional
    public OrderDto cancel(OrderDto orderDto) {
        //判断订单状态
        if (!orderDto.getOrderStatus().equals(OrderStatus.NEW)) {
            log.error("【取消订单】订单状态不正确，orderId={},orderStatus={}", orderDto.getOrderId(), orderDto.getOrderStatus());
            throw new SellException(ExceptionCode.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderDto.setOrderStatus(OrderStatus.CANCEL);
        OrderMaster orderMaster = ObjConverter.convert(orderDto, OrderMaster.class);
        OrderMaster updatedOrderMaster = orderMasterRepository.save(orderMaster);
        if(updatedOrderMaster == null){
            log.error("【取消订单】订单更新失败，orderMaster={}", orderMaster);
            throw new SellException(ExceptionCode.ORDER_STATUS_ERROR);
        }
        if(CollectionUtils.isEmpty(orderDto.getOrderDetails())){
            log.error("【取消订单】订单中无商品详情，orderId={}", orderDto.getOrderId());
            throw new SellException(ExceptionCode.ORDER_DETAIL_EMPTY);
        }
        //返回库存
        List<CarDto> carDtos = orderDto.getOrderDetails().stream()
                .map(e -> new CarDto(e.getProductId(), e.getProductQuantity())).collect(Collectors.toList());
        productService.increaseStock(carDtos);
        //如果已付款，需要退款
        if(orderDto.getPayStatus() == PayStatus.SUCCESS){
            payService.refund(orderDto);
        }
        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto finish(OrderDto orderDto) {
        if (!orderDto.getOrderStatus().equals(OrderStatus.NEW)) {
            log.error("【完结订单】订单状态不正确，orderId={},orderStatus={}", orderDto.getOrderId(), orderDto.getOrderStatus());
            throw new SellException(ExceptionCode.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderDto.setOrderStatus(OrderStatus.FINISHED);
        OrderMaster orderMaster = ObjConverter.convert(orderDto, OrderMaster.class);
        OrderMaster updatedOrderMaster = orderMasterRepository.save(orderMaster);
        if(updatedOrderMaster == null){
            log.error("【完结订单】订单更新失败，orderMaster={}", orderMaster);
            throw new SellException(ExceptionCode.ORDER_UPDATE_FAIL);
        }
        //推送微信模板消息
        pushMessageService.onOrderStatusChanged(orderDto);
        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto paid(OrderDto orderDto) {
        if(!orderDto.getOrderStatus().equals(OrderStatus.NEW)) {
            log.error("【订单支付完结】订单状态不正确，orderId={},orderStatus={}", orderDto.getOrderId(), orderDto.getOrderStatus());
            throw new SellException(ExceptionCode.ORDER_STATUS_ERROR);
        }
        if(!orderDto.getPayStatus().equals(PayStatus.WAIT)) {
            log.error("【订单支付完结】订单支付态不正确，orderId={},payStatus={}", orderDto.getOrderId(), orderDto.getPayStatus());
            throw new SellException(ExceptionCode.ORDER_PAY_STATUS_ERROR);
        }
        //修改订单状态
        orderDto.setPayStatus(PayStatus.SUCCESS);
        OrderMaster orderMaster = ObjConverter.convert(orderDto, OrderMaster.class);
        OrderMaster updatedOrderMaster = orderMasterRepository.save(orderMaster);
        if(updatedOrderMaster == null){
            log.error("【订单支付完结】订单更新失败，orderMaster={}", orderMaster);
            throw new SellException(ExceptionCode.ORDER_UPDATE_FAIL);
        }
        return orderDto;
    }

    @Override
    public Page<OrderDto> findList(Pageable pageable) {
        Page<OrderMaster> orderMasters = orderMasterRepository.findAll(pageable);
        Page<OrderDto> orderDtos = ObjConverter.convert(orderMasters, pageable, OrderDto.class);
        return orderDtos;
    }
}
