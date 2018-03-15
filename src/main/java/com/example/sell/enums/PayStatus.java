package com.example.sell.enums;

public enum PayStatus implements BaseEnum<Integer>{
    WAIT(0,"等待支付"),
    SUCCESS(1,"支付成功")
    ;
    private final Integer code;
    private final String name;
    private PayStatus(Integer code, String name){
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
