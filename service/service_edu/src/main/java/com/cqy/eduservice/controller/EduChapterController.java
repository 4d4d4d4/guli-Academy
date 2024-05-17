package com.cqy.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqy.commonutils.Result;
import com.cqy.eduservice.entity.EduChapter;
import com.cqy.eduservice.entity.EduVideo;
import com.cqy.eduservice.entity.chapter.ChapterVo;
import com.cqy.eduservice.service.EduChapterService;
import com.cqy.eduservice.service.EduVideoService;
import com.cqy.servicebase.exceptionhandler.GuliException;
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
@RequestMapping("/eduservice/chapter")
@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private EduVideoService videoService;

    //课程大纲列表,根据课程id进行查询
    @GetMapping("getChapterVideo/{courseId}")
    public Result getChapterVideo(@PathVariable String courseId) {
        List<ChapterVo> list = chapterService.getChapterVideoByCourseId(courseId);
        return Result.ok().data("list",list);
    }

    //添加章节
    @PostMapping
    public Result addChapter(@RequestBody EduChapter eduChapter) {
        chapterService.save(eduChapter);
        return Result.ok();
    }

    //根据章节id查询
    @GetMapping("getChapterInfo/{chapterId}")
    public Result getChapterInfo(@PathVariable String chapterId) {
        EduChapter eduChapter = chapterService.getById(chapterId);
        return Result.ok().data("chapter",eduChapter);
    }

    //修改章节
    @PostMapping("updateChapter")
    public Result updateChapter(@RequestBody EduChapter eduChapter) {
        chapterService.updateById(eduChapter);
        return Result.ok();
    }

    //删除的方法
    @DeleteMapping("deleteChapter/{chapterId}")
    public Result deleteChapter(@PathVariable String chapterId) {
        EduChapter chapter = (EduChapter)this.chapterService.getById(chapterId);
        String courseId = chapter.getCourseId();
        QueryWrapper<EduVideo> wrapper = new QueryWrapper();
        wrapper.eq("course_id", courseId);
        wrapper.eq("chapter_id", chapterId);
        List<EduVideo> list = this.videoService.list(wrapper);
        if (list.size() > 0) {
            throw new GuliException(20001, "该章节仍有发布的小节课程，请勿删除！");
        } else {
            boolean b = this.chapterService.removeById(chapterId);
            return b ? Result.ok() : Result.error().message("删除失败，未找到该章节。");
        }
    }
}

