package com.example.sell.enums;

public enum OrderStatus implements BaseEnum<Integer>{
    NEW(0,"新下单"),
    OLD(1,"历史订单"),
    CANCEL(2,"已取消"),
    FINISHED(3, "已完结 ");
    private final Integer code;
    private final String name;

    private OrderStatus(Integer code, String name){
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
