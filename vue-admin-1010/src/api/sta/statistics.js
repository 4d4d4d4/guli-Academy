import request from '@/utils/request'

export default{
    createDataByDay(day){
        return request({
            url:`/edustatistics/stda/registerCount/${day}`,
            method:'post'
        })
    },
    //2 获取统计数据
    getDataSta(searchObj) {
        return request({
            url: `/edustatistics/stda/showData/${searchObj.type}/${searchObj.begin}/${searchObj.end}`,
            method: 'get'
          })
    }
}