package com.example.sell.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinTokenCheckController {

    @GetMapping("/auth")
    public void auth(@RequestParam("code")String code){
        log.info("进入微信授权。。");
        log.info("code={}",code);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=wx36651bce924c722f&secret=d4ac0916c3bd8c88b68246598b63c72e&code="+code+"&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String respone = restTemplate.getForObject(url, String.class);
        log.info(respone);
    }

    @GetMapping("/check")
    public void check(String signature, String timestamp, String nonce, String echostr, HttpServletResponse resp) throws IOException {
        log.info("echostr>>>" + echostr);
//TODO 增加验证
        resp.getWriter().append(echostr);
    }
}
