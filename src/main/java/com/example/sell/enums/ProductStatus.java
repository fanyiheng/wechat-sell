package com.example.sell.enums;

public enum ProductStatus implements BaseEnum<Integer> {
    UP(0, "在架"),
    DOWN(1, "下架");
    private final Integer code;
    private final String name;

    private ProductStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }
}
