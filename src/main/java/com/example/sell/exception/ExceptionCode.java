package com.example.sell.exception;

public enum ExceptionCode {
    PRODUCT_NOT_EXIST(100001, "商品不存在"),
    PRODUCT_INSUFFICIENT(100002, "库存不足"),
    SYS_EXP(100999, "系统异常"),
    ENUM_CONVERT_EXP(100998, "枚举转换异常"),
    ORDER_STATUS_ERROR(100003, "订单状态不正确"),
    ORDER_UPDATE_FAIL(100004, "订单更新失败"),
    ORDER_DETAIL_EMPTY(100005, "订单中无商品详情"),
    ORDER_PAY_STATUS_ERROR(100006, "订单支付状态不正确"),
    PARAM_ERROR(100000, "参数不正确"),
    CAR_EMPTY(100007, "购物车不能为空"),
    ORDER_NOT_EXIST(100008,"订单不存在" ),
    ORDER_OWER_ERROR(100009, "订单不属于当前用户"),
    WECHAT_MP_ERROR(100010, "微信公众号处理异常"),
    WXPAY_NOTIFY_MONEY_VERIFY_ERROR(100011, "微信支付金额验证不通过"),
    PRODUCT_STATUS_ERROR(100012, "商品状态不正确"),
    LOGIN_FAIL(100013,"登录失败"), LOCK_FAILED(100014,"获取锁失败" );
    public final Integer code;
    public final String name;

    private ExceptionCode(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
