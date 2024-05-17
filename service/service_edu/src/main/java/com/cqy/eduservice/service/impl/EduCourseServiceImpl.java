package com.cqy.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqy.eduservice.entity.EduCourse;
import com.cqy.eduservice.entity.EduCourseDescription;
import com.cqy.eduservice.entity.frontVo.CourseFrontVo;
import com.cqy.eduservice.entity.vo.CourseInfoVo;
import com.cqy.eduservice.entity.vo.CoursePublishVo;
import com.cqy.eduservice.mapper.EduCourseMapper;
import com.cqy.eduservice.service.EduChapterService;
import com.cqy.eduservice.service.EduCourseDescriptionService;
import com.cqy.eduservice.service.EduCourseService;
import com.cqy.eduservice.service.EduVideoService;
import com.cqy.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-03-02
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //课程描述注入
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    //注入小节和章节service
    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private EduChapterService chapterService;

    //添加课程基本信息的方法
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1 向课程表添加课程基本信息
        //CourseInfoVo对象转换eduCourse对象
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if(insert == 0) {
            //添加失败
            throw new GuliException(20001,"添加课程信息失败");
        }

        //获取添加之后课程id
        String cid = eduCourse.getId();

        //2 向课程简介表添加课程简介
        //edu_course_description
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());
        //设置描述id就是课程id
        courseDescription.setId(cid);
        courseDescriptionService.save(courseDescription);

        return cid;
    }

    //根据课程id查询课程基本信息
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        //1 查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);


        //2 查询描述表
        EduCourseDescription courseDescription = courseDescriptionService.getById(courseId);
        if(courseDescription!=null) {
            courseInfoVo.setDescription(courseDescription.getDescription());
        }
        return courseInfoVo;
    }

    //修改课程信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //1 修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if(update == 0) {
            throw new GuliException(20001,"修改课程信息失败");
        }

        //2 修改描述表
        EduCourseDescription description = new EduCourseDescription();
        description.setId(courseInfoVo.getId());
        description.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.updateById(description);
    }

    //根据课程id查询课程确认信息
    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        //调用mapper
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(id);
        return publishCourseInfo;
    }

    //删除课程
    @Override
    public void removeCourse(String courseId) {
        //1 根据课程id删除小节
        eduVideoService.removeVideoByCourseId(courseId);

        //2 根据课程id删除章节
        chapterService.removeChapterByCourseId(courseId);

        //3 根据课程id删除描述
        courseDescriptionService.removeById(courseId);

        //4 根据课程id删除课程本身
        int result = baseMapper.deleteById(courseId);
        if(result == 0) { //失败返回
            throw new GuliException(20001,"删除失败");
        }
    }

    //条件查询带分页的课程列表
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> page, CourseFrontVo course) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();

        //查询条件
        wrapper.eq(!StringUtils.isEmpty(course.getSubjectParentId()),"subject_parent_id",course.getSubjectParentId());
        wrapper.eq(!StringUtils.isEmpty(course.getSubjectId()),"subject_id",course.getSubjectId());
        wrapper.orderByDesc(!StringUtils.isEmpty(course.getPriceSort()),"price");
        wrapper.orderByDesc(!StringUtils.isEmpty(course.getBuyCountSort()),"buy_count");
        wrapper.orderByDesc(!StringUtils.isEmpty(course.getGmtCreateSort()),"gmt_create");

        baseMapper.selectPage(page, wrapper);


        List<EduCourse> list = page.getRecords();
        long size = page.getSize();
        long current = page.getCurrent();
        long total = page.getTotal();
        long pages = page.getPages();
        boolean hasNext = page.hasNext();
        boolean hasPrevious = page.hasPrevious();

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("current",current);
        map.put("size",size);
        map.put("total",total);
        map.put("pages",pages);
        map.put("hasNext",hasNext);
        map.put("hasPrevious",hasPrevious);
        return map;
    }

}
