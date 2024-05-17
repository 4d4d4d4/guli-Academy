<template>
  <client-only>
  <div class="main">
    <div class="title">
      <a class="active" href="/login">登录</a>
      <span>·</span>
      <a href="/register">注册</a>
    </div>

    <div class="sign-up-container">
      <el-form ref="userForm" :model="user">

        <el-form-item class="input-prepend restyle" prop="email" :rules="[{ required: true, message: '请输入邮箱', trigger: 'blur' },{validator: checkEmail, trigger: 'blur'}]">
          <div>
            <el-input type="text" placeholder="邮箱号" v-model="user.email"/>
          </div>
        </el-form-item>

        <el-form-item class="input-prepend" prop="password" :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]">
          <div>
            <el-input type="password" placeholder="密码" v-model="user.password"/>
          </div>
        </el-form-item>

        <div class="btn">
          <input type="button" class="sign-in-button" value="登录" @click="submitLogin()">
        </div>
      </el-form>
      <!-- 更多登录方式 -->
    </div>

  </div>
</client-only>
</template>

<script>
  import '~/assets/css/sign.css'
  import '~/assets/css/iconfont.css'
  import loginApi from '@/api/login'
  import cookie from 'js-cookie'

  export default {
    layout: 'sign',

    data () {
      return {
        //封装登录邮箱和密码
        user:{
          email:'',
          password:''
        },
        //获取通过解析token获取到的用户信息
        loginInfo:{}
      }
    },

    methods: {

      checkEmail (rule, value, callback) {
        //debugger
        if (!(/^\d{5,12}@qq\.com$/.test(value))) {
              return callback(new Error('QQ邮箱格式不正确'));
            }
        return callback()
      },
       //登录方法
      submitLogin(){
        //实现token拦截器  第一步 调用接口进行登录 ，返回token
        loginApi.submitLogin(this.user).then(res=>{
          if(res.data.code !== 20000){
            // console.log(res.data)
            this.$message({
              type:"error",
              message:res.data.message
            })
            return 
          }
          //获取token   参数一：名称  参数二：token的值  参数三：作用范围
          //实现token拦截器 第二步 将获取到的token值放到cookie中
          cookie.set('cqy_token',res.data.data.token,{domain: 'localhost'})

          //实现token拦截器 第四步 调用接口根据token获取用户信息 
          loginApi.getLoginUserInfo().then(res=>{
            if(res.data.code !== 20000){
              // console.log(res.data)
            this.$message({
              type:"error",
              message:res.data.message
            })
            return 
          }
            //获取用户信息
            this.loginInfo = res.data.data.member
            //将用户信息存放到cookie中去
            //注意 cookie只能保存字符串形式的数据 所以要将对象转化为字符串
            cookie.set('cqy_ucenter', JSON.stringify(this.loginInfo),{domain:"localhost"})
            
            //跳转页面
            window.location.href = "/";
          })
        })
      }
    }
  }
</script>
<style>
   .el-form-item__error{
    z-index: 9999999;
  }
</style>