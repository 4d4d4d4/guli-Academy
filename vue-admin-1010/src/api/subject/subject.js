import request from '@/utils/request'

export default{

    //查询课程分类列表 （tree）
    getAllSubjectWithTree(){
        return request({
            url: "/eduservice/subject/getAllSubject",
            method:"get"
        })
    },

    //添加课程分类 （根据上传过来的xslx文件）
    addSubejctClass(file){
        return request({
            url:"/eduservice/subject/addSubject",
            method:"post",
            data: file
        })
    }

}