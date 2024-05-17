package com.cqy.eduservice.controller;


import com.cqy.commonutils.Result;
import com.cqy.eduservice.client.VodClient;
import com.cqy.eduservice.entity.EduVideo;
import com.cqy.eduservice.service.EduVideoService;
import com.cqy.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-03-02
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    @Autowired
    private VodClient vodClient;

        @PostMapping({"/addVideo"})
    public Result addVideo(@RequestBody EduVideo video) {
            System.out.println("***********************************************************");
        System.out.println(video);
        this.videoService.save(video);
        return Result.ok();
    }
    //删除小节
    @DeleteMapping({"/deleteVideo/{id}"})
    public Result deleteVideoById(@PathVariable String id) {
        //根据小节id获取视频id
        EduVideo videoById = videoService.getById(id);
        if(videoById.getVideoSourceId()!=null && !videoById.getVideoSourceId().equals("")){
            //根据视频id删除阿里云存储的视频
            Result result = vodClient.removeAlyVideo(videoById.getVideoSourceId());
            System.out.println(result);
            if(result.getCode() == 20001){
                throw new GuliException(20001,"删除视频失败了!!!");
            }
            //清除数据库存储的视频id
            videoById.setVideoSourceId("");
            videoService.updateById(videoById);
        }
        //删除小节
        this.videoService.removeById(id);
        return Result.ok();
    }
    //修改小节
    @PutMapping({"/updateVideo"})
    public Result updateVideo(@RequestBody EduVideo video) {
        this.videoService.updateById(video);
        return Result.ok();
    }

    @GetMapping({"{id}"})
    public Result getVideo(@PathVariable String id) {
        EduVideo video = (EduVideo)this.videoService.getById(id);
        return Result.ok().data("video", video);
    }

}

