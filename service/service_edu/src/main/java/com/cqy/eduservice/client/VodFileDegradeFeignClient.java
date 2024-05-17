package com.cqy.eduservice.client;

import com.cqy.commonutils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

//熔断器 出错后会执行
@Component
public class VodFileDegradeFeignClient implements VodClient{

    @Override
    public Result removeAlyVideo(String id) {
        return Result.error().message("删除视频错误！");
    }

    @Override
    public Result deleteBatch(List<String> list) {

        return Result.error().message("多个视频删除异常！");
    }
}
