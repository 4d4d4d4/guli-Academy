package com.cqy.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqy.commonutils.Result;
import com.cqy.eduservice.entity.EduCourse;
import com.cqy.eduservice.entity.EduTeacher;
import com.cqy.eduservice.service.EduCourseService;
import com.cqy.eduservice.service.EduTeacherService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eduservice/teacherfront")
@CrossOrigin
public class teacherFrontController {
    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;


    //分页查询讲师列表
    @PostMapping("/getTeacherList/{current}/{limit}")
    public Result getTeacherFrontList(@PathVariable Integer current,@PathVariable Integer limit){
        Page<EduTeacher> page = new Page<EduTeacher>(current,limit);
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        IPage<EduTeacher> teacherPage = teacherService.page(page, wrapper);
        List<EduTeacher> eduTeacherList = teacherPage.getRecords();
        long total = teacherPage.getTotal();
        long pages = teacherPage.getPages();
        long size = teacherPage.getSize();
        boolean next = current < pages;
        boolean previous = current > 1;
        System.out.println(pages);

        return Result.ok().data("list",eduTeacherList)
                .data("total",total)
                .data("pages",pages)
                .data("size",size)
                .data("next",next)
                .data("previous",previous)
                .data("current",current);
        
    }

    // 查询讲师详情
    @GetMapping("/getTeacherInfo/{teacherId}")
    public Result getTeacherInfo(@PathVariable("teacherId") Long id){
        EduTeacher eduTeacher = teacherService.getById(id);
        QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();
        courseWrapper.eq("teacher_id",id);
        List<EduCourse> courseList = courseService.list(courseWrapper);
        return Result.ok().data("teacher",eduTeacher).data("courseList",courseList);
    }
}
