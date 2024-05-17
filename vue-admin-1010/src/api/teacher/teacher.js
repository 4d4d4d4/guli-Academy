import request from '@/utils/request'

export default{
    //1.讲师列表 （分页查询列表)
    getTeacherListPage(current,limit,teacherQuery){
        return request({
           // <!-- url: '/eduservice/edu-teacher/pageTeacherCondition/'+current+'/'+limit, -->
            url: `/eduservice/edu-teacher/pageTeacherCondition/${current}/${limit}`,
            method: 'post',
            // teacherQuery条件对象，后端使用RequestBody获取数据
            // data表示把你的对象按照json格式发送到接口中
            data: teacherQuery
        })
    },
    deleteTeacherById(id){
        return request({
            url: `/eduservice/edu-teacher/${id}`,
            method: 'delete',
        })
    },
    //添加讲师
    addTeacher(teacher){
        return request({
            url: `/eduservice/edu-teacher/addTeacher`,  
            method: 'post',
            data: teacher
        })
    },
    selectTeacherById(id){
        return request({
            url: `/eduservice/edu-teacher/getTeacher/${id}`,  
            method: 'get'
        })
    },
    updateTeacher(teacher){
        return request({
            url: `/eduservice/edu-teacher/updateTeacher`,
            method: "post",
            data: teacher
        })
    },
    getAllTeacherList(){
        return request({
            url: '/eduservice/edu-teacher/findAll',
            method:"get"
        })
    }
}