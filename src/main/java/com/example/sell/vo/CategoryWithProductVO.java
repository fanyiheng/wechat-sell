package com.example.sell.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CategoryWithProductVO {
    @JSONField(name="name")
    private String categoryName;
    @JSONField(name="type")
    private Integer categoryType;
    @JSONField(name="foods")
    private List<ProductVO> productVOs;
}
