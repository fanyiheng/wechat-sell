package com.example.sell.util;

import com.example.sell.exception.ExceptionCode;
import com.example.sell.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ObjConverter {

    public static <T,R> R convert(T source,Class<R> targetClass){
        try{
            R target = targetClass.newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        }catch (Exception e){
            log.error("【对象转换异常】，source={}，targetClass={},{}", source,targetClass,e);
            throw new SellException(ExceptionCode.SYS_EXP);
        }
    }

    public static <T,R> List<R> convert(List<T> sources, Class<R> targetClass){
        try{
            List<R> targetList = new ArrayList<>();
            for(T source : sources){
                R target = targetClass.newInstance();
                BeanUtils.copyProperties(source, target);
                targetList.add(target);
            }
            return targetList;
        }catch (Exception e){
            log.error("【对象转换异常】，source={}，targetClass={}，{}", sources,targetClass,e);
            throw new SellException(ExceptionCode.SYS_EXP);
        }
    }

    public static <T,R> Page<R> convert(Page<T> sources, Pageable pageable,Class<R> targetClass){
        try{
            List<T> sourceContent = sources.getContent();
            List<R> targetContent = convert(sourceContent, targetClass);
            Page<R> targets = new PageImpl<>(targetContent,pageable,sources.getTotalElements());
            return targets;
        }catch (Exception e){
            log.error("【对象转换异常】，source={}，targetClass={},{}", sources,targetClass,e);
            throw new SellException(ExceptionCode.SYS_EXP);
        }
    }
}
