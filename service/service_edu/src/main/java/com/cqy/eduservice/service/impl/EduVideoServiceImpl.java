package com.cqy.eduservice.service.impl;

import com.cqy.eduservice.client.VodClient;
import com.cqy.eduservice.entity.EduVideo;
import com.cqy.eduservice.mapper.EduVideoMapper;
import com.cqy.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-03-02
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient client;


    //1 根据课程id删除小节

    @Override
    public void removeVideoByCourseId(String courseId) {
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id",courseId);
        videoWrapper.select("video_source_id");
        List<EduVideo> videos = baseMapper.selectList(videoWrapper);
        System.out.println(videos);
        List<String> list = new ArrayList<>();
        for(EduVideo video : videos){
            if (video!=null && video.getVideoSourceId()!=null && !video.getVideoSourceId().equals("")) {
                list.add(video.getVideoSourceId());
            }
        }
        if(list.size()>0) {
            System.out.println("****************");
            System.out.println(list);
            //根据视频id删除多个视频
            client.deleteBatch(list);
        }
        //删除小节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
