import request from '@/utils/request'
export default{
    //查询热门课程和名师
    getIndexDatar(){
        return request({
            url:"/eduservice/front/index",
            method:"get"
        })
    }
}