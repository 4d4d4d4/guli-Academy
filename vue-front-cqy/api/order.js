import request from '@/utils/request'
export default{
    //生成订单
    createOrders(courseId){
        return request({
            url:`/eduorder/torder/createOrder/${courseId}`,
            method:'post'
        })
    },
    //根据订单号查询订单
    getOrdersInfo(orderId){
        return request({
            url:`/eduorder/torder/getOrderInfo/${orderId}`,
            method:'get'
        })
    },
    //根据用户id和课程id 查询订单信息
    getOrdersInfoByMemberIdAndCourseId(memberId,courseId){
        return request({
            url:`/eduorder/torder/getOrderInfoBy/${memberId}/${courseId}`,
            method:'get'
        })
    },


    //生成二维码的方法
    createNatvie(orderNo){
        return request({
        url:`/eduorder/tpaylog/createNative/${orderNo}`,
        method:'get'
        })
    },
    //  根据订单号 查询订单支付状态
    queryPayStatus(orderNo){
        return request ({
            url:`/eduorder/tpaylog/queryPayStatus/${orderNo}`,
            method:'get'
        })
    }
}