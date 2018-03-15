package com.example.sell.controller;

import com.example.sell.config.ProjectUrlConfig;
import com.example.sell.exception.ExceptionCode;
import com.example.sell.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpService wxOpenService;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl) throws UnsupportedEncodingException {
        String url = projectUrlConfig.getWechatMpAuthorize()+"/sell/wechat/userInfo";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url,
                WxConsts.OAuth2Scope.SNSAPI_BASE, URLEncoder.encode(returnUrl, "UTF-8") );
        log.info("【微信网页授权】获取code,redirectUrl={}",redirectUrl);
        return "redirect:"+redirectUrl;
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,@RequestParam("state") String returnUrl){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken ;
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("【微信网页授权】授权异常", e);
            throw new SellException(ExceptionCode.WECHAT_MP_ERROR,e.getError().getErrorMsg());
        }
        log.info("【微信网页授权】wxMpOAuth2AccessToken={}",wxMpOAuth2AccessToken);
        String openId = wxMpOAuth2AccessToken.getOpenId();
//        return "redirect:"+returnUrl+"/#/?openid="+openId;
        return "redirect:"+returnUrl+"/?openid="+openId;
    }

    @GetMapping("/qrAuthorize")
    public String qrAuthorize(@RequestParam("returnUrl") String returnUrl) throws UnsupportedEncodingException {
        String url = projectUrlConfig.getWechatOpenAuthorize()+"/sell/wechat/userInfo";
        String redirectUrl = wxOpenService.buildQrConnectUrl(url,WxConsts.QrConnectScope.SNSAPI_LOGIN ,  URLEncoder.encode(returnUrl, "UTF-8"));
        log.info("【微信网页授权】获取code,redirectUrl={}",redirectUrl);
        return "redirect:"+redirectUrl;
    }


    @GetMapping("/qrUserInfo")
    public String qrUserInfo(@RequestParam("code") String code,@RequestParam("state") String returnUrl){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken ;
        try {
            wxMpOAuth2AccessToken = wxOpenService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("【微信网页授权】授权异常", e);
            throw new SellException(ExceptionCode.WECHAT_MP_ERROR,e.getError().getErrorMsg());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();
        log.info("【微信网页授权】wxMpOAuth2AccessToken={}",wxMpOAuth2AccessToken);
        return "redirect:"+returnUrl+"/?openid="+openId;
    }
}
