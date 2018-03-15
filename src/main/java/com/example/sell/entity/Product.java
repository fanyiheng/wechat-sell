package com.example.sell.entity;

import com.example.sell.converter.ProductStatusConverter;
import com.example.sell.enums.ProductStatus;
import lombok.Data;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class Product {
    @Id
    private String productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer productStock;
    private String productDescription;
    private String productIcon;
    private Integer categoryType;
    @Convert(converter = ProductStatusConverter.class)
    private ProductStatus productStatus = ProductStatus.DOWN;
    private Date createTime;
    private Date updateTime;
}
