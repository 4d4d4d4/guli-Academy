package com.cqy.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqy.commonutils.Result;
import com.cqy.eduservice.entity.EduCourse;
import com.cqy.eduservice.entity.EduTeacher;
import com.cqy.eduservice.service.EduCourseService;
import com.cqy.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/front")
@CrossOrigin
public class frontController {
    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    // 查询签八条热门课程记录 查询前四条名师
    @Cacheable(value = "index",key = "'HotCourseTeacher'")
    @GetMapping("/index")
    public Result index(){
        //查询8条热门课程
        QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();
        courseWrapper.orderByDesc("id");
        courseWrapper.last(" limit 8");
        List<EduCourse> courseList = courseService.list(courseWrapper);

        //查询四位名师
        QueryWrapper<EduTeacher> teacherWrapper = new QueryWrapper<>();
        teacherWrapper.orderByDesc("id");
        teacherWrapper.last(" limit 4");
        List<EduTeacher> teacherList = teacherService.list(teacherWrapper);

        return Result.ok().data("courseList",courseList).data("teacherList",teacherList);
    }
}
