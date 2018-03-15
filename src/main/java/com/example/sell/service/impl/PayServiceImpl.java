package com.example.sell.service.impl;

import com.example.sell.dto.OrderDto;
import com.example.sell.exception.ExceptionCode;
import com.example.sell.exception.SellException;
import com.example.sell.service.OrderService;
import com.example.sell.service.PayService;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayServiceImpl implements PayService {
    private static final String ORDER_NAME="微信点餐支付";
    @Autowired
    private BestPayServiceImpl bestPayService;
    @Autowired
    private OrderServiceImpl orderService;

    @Override
    public PayResponse create(OrderDto orderDto) {
        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(orderDto.getOrderId());
        payRequest.setOrderId(orderDto.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setOrderAmount(orderDto.getOrderAmount().doubleValue());
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("【微信支付】 发起支付，request={}",payRequest);
        PayResponse response = bestPayService.pay(payRequest);
        log.info("【微信支付】 发起支付，response={}",response);
        return response;
    }

    @Override
    public PayResponse notify(String notifyData) {
        //验证签名
        //支付状态
        //支付金额
        //支付人
        PayResponse response = bestPayService.asyncNotify(notifyData);
        log.info("【微信支付】异步通知，payResponse={}",response);
        OrderDto orderDto = orderService.findOne(response.getOrderId());
        if(orderDto == null){
            log.error("【微信支付】异步通知，订单不存在，orderId={}",response.getOrderAmount() );
            throw new SellException(ExceptionCode.ORDER_NOT_EXIST);
        }
        if(!orderDto.getOrderAmount().equals(response.getOrderAmount())){
            log.error("【微信支付】异步通知，订单金额不一致，orderId={},微信通知金额={},系统金额={}"
            ,response.getOrderId(),response.getOrderAmount(),orderDto.getOrderAmount());
            throw new SellException(ExceptionCode.WXPAY_NOTIFY_MONEY_VERIFY_ERROR);
        }
        orderService.paid(orderDto);
        return response;
    }

    @Override
    public RefundResponse refund(OrderDto orderDto) {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderId(orderDto.getOrderId());
        refundRequest.setOrderAmount(orderDto.getOrderAmount().doubleValue());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("【微信退款】 request={}", refundRequest);

        RefundResponse refundResponse = bestPayService.refund(refundRequest);
        log.info("【微信退款】 response={}", refundResponse);
        return refundResponse;
    }
}
