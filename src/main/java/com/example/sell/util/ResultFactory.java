package com.example.sell.util;

import com.example.sell.vo.Result;

public class ResultFactory {
    public static <T> Result<T> success(T data){
        Result<T> result = new Result<>();
        result.setCode(0);
        result.setData(data);
        result.setMsg("成功");
        return result;
    }
    public static Result success(){
        return success(null);
    }
    public static Result fail(String msg){
        return fail(1, msg);
    }
    public static Result fail(Integer code,String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
