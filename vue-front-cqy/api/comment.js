import request from '@/utils/request'
export default{
    getCommentByPage(current,limit){
        return request({
            url:`/eduservice/comment/getComment/${current}/${limit}`,
            method:'get'
        })
    },
    appendComment(comment){
        return request({
            url:`/eduservice/comment/addComment`,
            method:'post',
            data:comment
        })
    }
}