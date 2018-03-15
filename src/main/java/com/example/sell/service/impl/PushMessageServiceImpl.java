package com.example.sell.service.impl;

import com.example.sell.config.WechatAccountConfig;
import com.example.sell.dto.OrderDto;
import com.example.sell.service.PushMessageService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class PushMessageServiceImpl implements PushMessageService{
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WechatAccountConfig wechatAccountConfig;

    @Override
    public void onOrderStatusChanged(OrderDto orderDto) {
        WxMpTemplateMessage templateMessage =
                WxMpTemplateMessage.builder()
                        .templateId(wechatAccountConfig.getTemplateIds().get("orderStatus"))
                        .toUser(orderDto.getBuyerOpenid())
                        .data(Arrays.asList(
                                new WxMpTemplateData("first", "亲，请记得收货。"),
                                new WxMpTemplateData("keyword1", "微信点餐"),
                                new WxMpTemplateData("keyword2", "18868812345"),
                                new WxMpTemplateData("keyword3", orderDto.getOrderId()),
                                new WxMpTemplateData("keyword4", orderDto.getOrderStatus().getName()),
                                new WxMpTemplateData("keyword5", "￥" + orderDto.getOrderAmount()),
                                new WxMpTemplateData("remark", "欢迎再次光临！")
                        ))
                .build();
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            log.error("【微信模版消息】发送失败, {}", e);
        }
    }
}
