package com.example.sell.enums;

public enum Result implements BaseEnum<Integer> {
    SUCCESS(0,"成功"), LOGOUT_SUCCESS(1, "登出成功");
    private final Integer code;
    private final String name;

    private Result(Integer code, String name){
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
