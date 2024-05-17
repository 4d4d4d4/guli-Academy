import request from '@/utils/request'
export default{
    // 添加小节
    addVideo(video){
        return request({
            url:'/eduservice/video/addVideo',
            method:'post',
            data:video
        })
    },
    //删除小节
    deleteVideo(id){
        return request({
            url:`/eduservice/video/deleteVideo/${id}`,
            method:'delete'
        })
    },
    //修改小节
    updateVideo(video){
        return request({
            url:'/eduservice/video/updateVideo',
            method:'put',
            data:video
        })
    },
    //获取小节信息
    getVideoInfo(id){
        return request({
            url:`/eduservice/video/${id}`,
            method:'get'
        })
    },
    //删除阿里云中的视频
    deleteVideoById(id){
        return request({
            url:`/eduvod/video/removeAlyVideo/${id}`,
            method:'delete'
        })

    }
}