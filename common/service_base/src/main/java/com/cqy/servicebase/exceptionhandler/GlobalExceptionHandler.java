package com.cqy.servicebase.exceptionhandler;

import com.cqy.commonutils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})  //指定出现什么异常会执行
    @ResponseBody  //返回json格式
    public Result error(Exception e){
        e.printStackTrace();
        return Result.error().message("执行了全局异常").data("error",e);
    }

    //自定义异常
    @ExceptionHandler
    @ResponseBody
    public Result error(GuliException e){
        log.info(e.getMessage());
        e.printStackTrace();
        return Result.error().message(e.getMsg()).code(e.getCode());  //像前端传输自定义的状态码和消息
    }
}
