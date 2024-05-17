package com.cqy.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqy.commonutils.Result;
import com.cqy.eduservice.entity.EduCourse;
import com.cqy.eduservice.entity.EduCourseDescription;
import com.cqy.eduservice.entity.vo.CourseInfoVo;
import com.cqy.eduservice.entity.vo.CoursePublishVo;
import com.cqy.eduservice.service.EduCourseDescriptionService;
import com.cqy.eduservice.service.EduCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-03-02
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduCourseDescriptionService descriptionService;

    //课程列表 基本实现
    @GetMapping
    public Result getCourseInfoList() {
        List<EduCourse> list = this.courseService.list(null);
        return Result.ok().data("list", list);
    }
    @PostMapping({"/getCourseInfoByCondition/{current}/{size}"})
    public Result getCourseInfoByCondition(@RequestBody EduCourse course, @PathVariable int current, @PathVariable int size) {
        System.out.print(course);
        QueryWrapper<EduCourse> wrapper = new QueryWrapper();
        wrapper.like(course.getTitle() != null && course.getTitle() != "", "title", course.getTitle());
        wrapper.eq(course.getStatus() != null && course.getStatus() != "", "status", course.getStatus());
        Page<EduCourse> page = new Page((long)current, (long)size);
        IPage<EduCourse> iPage = this.courseService.page(page, wrapper);
        List<EduCourse> list = iPage.getRecords();
        long total = iPage.getTotal();
        return Result.ok().data("list", list).data("total", total);
    }

    //添加课程基本信息的方法
    @PostMapping("addCourseInfo")
    public Result addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        //返回添加之后课程id，为了后面添加大纲使用
        String id = courseService.saveCourseInfo(courseInfoVo);
        return Result.ok().data("courseId",id);
    }

    //根据课程id查询课程基本信息
    @GetMapping("getCourseInfo/{courseId}")
    public Result getCourseInfo(@PathVariable String courseId) {
        CourseInfoVo courseInfo = courseService.getCourseInfo(courseId);
        return Result.ok().data("courseInfo",courseInfo);
    }

    //修改课程信息
    @PutMapping
    public Result EditCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        this.courseService.updateById(eduCourse);
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo, eduCourseDescription);
        this.descriptionService.updateById(eduCourseDescription);
        return Result.ok();
    }

    //根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public Result getPublishCourseInfo(@PathVariable String id) {
        CoursePublishVo coursePublishVo = courseService.publishCourseInfo(id);
        return Result.ok().data("coursePublishVO",coursePublishVo);
    }

    //课程最终发布
    //修改课程状态
    @PostMapping("publishCourse/{id}")
    public Result publishCourse(@PathVariable String id) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");//设置课程发布状态
        courseService.updateById(eduCourse);
        return Result.ok();
    }

    //删除课程
    @DeleteMapping("{courseId}")
    public Result deleteCourse(@PathVariable String courseId) {
        //
        courseService.removeCourse(courseId);
        return Result.ok();
    }
}

