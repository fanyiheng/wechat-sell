package com.example.sell.exception;

import lombok.Getter;

@Getter
public class SellException extends RuntimeException {
    private Integer code;

    public SellException(ExceptionCode exceptionCode){
        super(exceptionCode.name);
        this.code = exceptionCode.code;
    }

    public SellException(ExceptionCode exceptionCode,String message) {
        super(message);
        this.code = exceptionCode.code;
    }
}
