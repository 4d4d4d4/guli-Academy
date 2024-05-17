import request from '@/utils/request'

export default{
    //添加课程信息
    addCourseInfo(courseInfo){
        return request({
            url:"/eduservice/course/addCourseInfo",
            method:"post",
            data:courseInfo
        })
    },
    //获取初始化课程信息
    getCourseInfoById(courseId){
        return request({
            url:`/eduservice/course/getCourseInfo/${courseId}`,
            method:'get'
        })
    },
    //修改课程信息
    updateCourseInfo(courseInfo){
        return request({
            url:"/eduservice/course",
            method:'put',
            data:courseInfo
        })
    },
    //get课程发布信息
    getPublishCourseInfo(id){
        return request({
            url:`eduservice/course/getPublishCourseInfo/${id}`,
            method:'get'
        })
    },
    //发布课程
    publishCourse(id){
        return request({
            url:`eduservice/course/publishCourse/${id}`,
            method:"post"
        })
    },
    //获取课程列表
    getCourseInfoList(){
        return request({
            url:"/eduservice/course",
            method:"get"
        })
    },
    //获取课程列表根据条件分页查询
    getCourseInfoByCondition(courseQuery,current,size){
        return request({
            url:`/eduservice/course/getCourseInfoByCondition/${current}/${size}`,
            method:"post",
            data:courseQuery
        })
    },
    //根据id删除课程
    removeCourseById(id){
        return request({
            url:`/eduservice/course/${id}`,
            method:'delete'
        })
    
    }
}