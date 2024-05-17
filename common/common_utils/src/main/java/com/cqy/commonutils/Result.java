package com.cqy.commonutils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {
    @ApiModelProperty("是否成功")
    private Boolean success;

    @ApiModelProperty("状态码")
    private Integer code;

    @ApiModelProperty("返回消息")
    private String message;

    @ApiModelProperty("返回数据")
    private Map<String,Object> data = new HashMap<String,Object>();

    // 构造方法私有化
    private Result(){}

    // 成功
    public static Result ok(){
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS);
        result.setSuccess(true);
        result.setMessage("成功");
        return result;
    }

    //失败
    public static Result error(){
        Result result = new Result();
        result.setCode(ResultCode.ERROR);
        result.setSuccess(false);
        result.setMessage("失败");
        return result;
    }

    //添加数据
    public Result data(Map<String,Object> map){
        this.setData(map);
        return this;
    }

    //添加状态
    public Result success(Boolean flag){
        this.setSuccess(flag);
        return this;
    }

    //添加状态码
    public Result code(Integer code){
        this.setCode(code);
        return this;
    }
    public Result data(String key, Object value){
        this.data.put(key,value);
        return this;
    }

    //添加返回信息
    public Result message(String message){
        this.setMessage(message);
        return this;
    }

}
