import request from '@/utils/request'

export default {

  
    //根据邮箱发验证码
  sendCode(email) {
    return request({
      url: `/edumsm/msm/sendTextMail/${email}`,
      method: 'post'
    })
  },

  //注册的方法
  registerMember(formItem) {
    return request({
      url: `/educenter/member/register`,
      method: 'post',
      data: formItem
    })
  }

}