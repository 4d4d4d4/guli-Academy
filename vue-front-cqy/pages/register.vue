<template>
  <client-only>
  <div class="main">
    <div class="title">
      <a href="/login">登录</a> 
      <span>·</span>
      <a class="active" href="/register">注册</a>
    </div>

    <div class="sign-up-container">
      <el-form ref="userForm" :model="params">

        <el-form-item class="input-prepend restyle" prop="nickname" :rules="[{ required: true, message: '请输入你的昵称', trigger: 'blur' }]">
          <div>
            <el-input type="text" placeholder="你的昵称" v-model="params.nickname"/>
          </div>
        </el-form-item>

        <el-form-item class="input-prepend restyle no-radius" prop="email" :rules="[{ required: true, message: '请输入邮箱', trigger: 'blur' },{validator: checkEmail, trigger: 'blur'}]">
          <div>
            <el-input type="text" placeholder="邮箱" v-model="params.email"/>
          </div>
        </el-form-item>

        <el-form-item class="input-prepend restyle no-radius" prop="code" :rules="[{ required: true, message: '请输入验证码', trigger: 'blur' }]">
          <div style="width: 100%;display: block;float: left;position: relative">
            <el-input type="text" placeholder="验证码" v-model="params.code"/>
          </div>
          <div class="btn" style="position:absolute;right: 0;top: 6px;width: 40%;">
            <a href="javascript:" type="button" @click="getCodeFun()" :value="codeTest"  style="border: none;background-color: none;" :style="{'pointer-events' : codeFlag?'auto':'none'}">{{codeTest}}</a>
          </div>
        </el-form-item>

        <el-form-item class="input-prepend" prop="password" :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]">
          <div>
            <el-input type="password" placeholder="设置密码" v-model="params.password"/>
          </div>
        </el-form-item>

        <div class="btn">
          <input type="button" class="sign-up-button" value="注册" @click="submitRegister()">
        </div>
        <p class="sign-up-msg">
          点击 “注册” 即表示您同意并愿意遵守简书
          <br>
          <a target="_blank" href="http://www.jianshu.com/p/c44d171298ce">用户协议</a>
          和
          <a target="_blank" href="http://www.jianshu.com/p/2ov8x3">隐私政策</a> 。
        </p>
      </el-form>
    </div>
  </div>
</client-only>
</template>

<script>
  import '~/assets/css/sign.css'
  import '~/assets/css/iconfont.css'

  import registerApi from '@/api/register'

  export default {
    layout: 'sign',
    data() {
      return {
        params: { //封装注册输入数据
          email: '',
          code: '',  //验证码
          nickname: '',
          password: ''
        },
        sending: true,      //是否发送验证码
        second: 60,        //倒计时间
        codeTest: '获取验证码',
        codeFlag:true  //验证码是否可以点击
       }
    },
    methods: {
     
       //注册提交的方法
       submitRegister() {
        console.log('params',this.params)
         registerApi.registerMember(this.params)
          .then(response => {
            let _code = response.data.code
            if(_code === 20000){
            //提示注册成功
              this.$message({
                type: 'success',
                message: "注册成功"
              })
            //跳转登录页面
            this.$router.push({path:'/login'})
            }else{
              this.$message({
                type: 'error',
                message: response.data.message
              })
            }
          })
       },
       timeDown() {
        let result = setInterval(() => {
          --this.second;
          this.codeTest = this.second
          if (this.second < 1) {
            clearInterval(result);
            this.sending = true;
            //this.disabled = false;
            this.second = 60;
            this.codeTest = "获取验证码"
            this.codeFlag = true
          }
        }, 1000);

      },
       //通过输入邮箱发送验证码
       getCodeFun() {
        console.log('params',this.params)
        this.timeDown()
        this.codeFlag = false
         registerApi.sendCode(this.params.email)
          .then(response => {
              this.sending = false
              //调用倒计时的方法
          })
       },

      checkEmail(rule, value, callback) {
        //debugger
        if (!(/^\d{5,12}@qq\.com$/.test(value))) {
              return callback(new Error('QQ邮箱格式不正确'));
            }
        return callback()
      }
    }
  }
</script>
