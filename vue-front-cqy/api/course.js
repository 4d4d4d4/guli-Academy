import request from '@/utils/request'
export default{
    //查询条件分页课程列表
    getCourseListByCondition(course,current,limit){
        return request({
            url:`eduservice/coursefront/getFrontCourseList/${current}/${limit}`,
            method:'post',
            data:course
        })
    },
    //查询一级分类和二级分类
    getSubjectWithTree(){
        return request({
            url:'/eduservice/subject/getAllSubject',
            method:'get'
        })
    },
    //根据id获取课程详情
    getCourseInfoByCourseId(courseId){
        return request({
            url:`/eduservice/course/getCourseInfo/${courseId}`,
            method:'get'
        })
    },
    //课程大纲列表 
    getChapteAndVideoList(courseId){
        return request({
            url:`/eduservice/chapter/getChapterVideo/${courseId}`,
            method:'get'
        })

    }
}