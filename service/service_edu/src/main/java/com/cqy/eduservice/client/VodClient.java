package com.cqy.eduservice.client;

import com.cqy.commonutils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 远程调用vod模块
 */
@Component
@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class) //调用服务的名称  出错调用的类 该注解会自动创建一个代理类 用于远程调用
public interface VodClient {

    //定义调用的方法路径
    //根据视频id删除阿里云的视频
    @DeleteMapping("/eduvod/video/removeAlyVideo/{id}")
     Result removeAlyVideo(@PathVariable("id") String id);
    //删除多个视频
    //传递多个视频id
    @DeleteMapping("/eduvod/video/delete-batch")
     Result deleteBatch(@RequestParam("list") List<String> list);
}
