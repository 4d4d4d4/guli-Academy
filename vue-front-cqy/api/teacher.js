import request from '@/utils/request'

export default{
    //分页讲师查询的方法
    getTeacherList(current,limit){
        return request({
            url:`/eduservice/teacherfront/getTeacherList/${current}/${limit}`,
            method:"post"
        })
    },
    //查询讲师详情
    getTeacherInfo(id){
        return request({
            url:`/eduservice/teacherfront/getTeacherInfo/${id}`,
            method:'get'
        })

    },
    //
}