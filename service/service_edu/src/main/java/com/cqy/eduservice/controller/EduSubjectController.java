package com.cqy.eduservice.controller;


import com.cqy.commonutils.Result;
import com.cqy.eduservice.entity.subject.OneSubject;
import com.cqy.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-02-29
 */
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService subjectService;

    //添加课程分类
    //获取上传过来文件，把文件内容读取出来
    @PostMapping("addSubject")
    public Result addSubject(MultipartFile file) {
        //上传过来excel文件
        subjectService.saveSubject(file,subjectService);
        return Result.ok();
    }

    //课程分类列表（树形）
    @GetMapping("getAllSubject")
    public Result getAllSubject() {
        //list集合泛型是一级分类
        List<OneSubject> list = subjectService.getAllOneTwoSubject();
        return Result.ok().data("list",list);
    }

}

