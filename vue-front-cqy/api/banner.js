import request from '@/utils/request'

export default{
    getListBanner(){
        return request({
            url:"/cmsservice/bannerfront/getAllBanner",
            method:"get"
        })
    }
}
