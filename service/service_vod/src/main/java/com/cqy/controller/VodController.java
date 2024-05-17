package com.cqy.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.*;
import com.baomidou.mybatisplus.extension.api.R;
import com.cqy.commonutils.Result;
import com.cqy.service.VodService;
import com.cqy.servicebase.exceptionhandler.GuliException;
import com.cqy.utils.ConstantVodUtils;
import com.cqy.utils.InitObject;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
public class VodController {
    @Autowired
    private VodService service;

    //上传视频阿里云
    @PostMapping("/upload")
    public Result uploadAliVideo(MultipartFile file){
        //返回上传视频id
        String id = service.uploadAliVideo(file);
        return Result.ok().data("videoId",id);
    }

    //根据视频id删除阿里云的视频
    @DeleteMapping("removeAlyVideo/{id}")
    public Result removeAlyVideo(@PathVariable String id){
        try {
            //初始化对象
            DefaultAcsClient acsClient = InitObject.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建删除视频的request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            System.out.print(id);
            request.setVideoIds(id);
            //向request设置视频id
            acsClient.getAcsResponse(request);

            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"删除失败了");
        }
    }

    //删除多个视频
    //传递多个视频id
    @DeleteMapping("delete-batch")
    public Result deleteBatch(@RequestParam("list") List<String> list){
        service.removeAlyVideo(list);
        return Result.ok();
    }

    //根据视频id获取视频凭证
    @GetMapping("getPlayAuth/{id}")
    public Result getPlayAuth(@PathVariable String id){
        try {
            //创建初始化对象
            DefaultAcsClient acsClient = InitObject.initVodClient(ConstantVodUtils.ACCESS_KEY_ID,ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建获取凭证的request和response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();

            //向request中设置视频id
            request.setVideoId(id);

            //调用方法得到凭证
            GetVideoPlayAuthResponse response = acsClient.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return Result.ok().data("playAuth",playAuth);
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"凭证获取失败");
        }
    }

}
