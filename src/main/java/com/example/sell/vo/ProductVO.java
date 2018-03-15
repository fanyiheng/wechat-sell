package com.example.sell.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVO {
    @JSONField(name="id")
    private String productId;
    @JSONField(name="name")
    private String productName;
    @JSONField(name="price")
    private BigDecimal productPrice;
    @JSONField(name="description")
    private String productDescription;
    @JSONField(name="icon")
    private String productIcon;
}
