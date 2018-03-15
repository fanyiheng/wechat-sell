package com.example.sell.dto;

import lombok.Data;

@Data
public class CarDto {
    private String productId;
    private Integer productQuantity;

    public CarDto(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
