package com.cqy.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqy.commonutils.Result;
import com.cqy.eduservice.entity.EduCourse;
import com.cqy.eduservice.entity.frontVo.CourseFrontVo;
import com.cqy.eduservice.entity.vo.CourseInfoVo;
import com.cqy.eduservice.service.EduCourseService;
import com.cqy.eduservice.service.EduTeacherService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/eduservice/coursefront")
@CrossOrigin
public class courseFrontController {
    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    //条件查询带分页的课程列表
    @PostMapping("/getFrontCourseList/{current}/{limit}")
    public Result getFrontCoursePageListWithCondition(@RequestBody(required = false) CourseFrontVo course, @PathVariable Integer current, @PathVariable Integer limit){
        Page<EduCourse> page = new Page<>(current,limit);
        Map<String,Object> map = courseService.getCourseFrontList(page,course);
        return  Result.ok().data(map);
    }



}
