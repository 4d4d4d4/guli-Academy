<template>
      <div class="app-container">
        <el-form label-width="120px">
          <el-form-item label="讲师名称">
            <el-input v-model="teacher.name"/>
          </el-form-item>
          <el-form-item label="讲师排序">
            <el-input-number v-model="teacher.sort" controls-position="right" min="0"/>
          </el-form-item>
          <el-form-item label="讲师头衔">
            <el-select v-model="teacher.level" clearable placeholder="请选择">
              <!--
                数据类型一定要和取出的json中的一致，否则没法回填
                因此，这里value使用动态绑定的值，保证其数据类型是number
              -->
              <el-option :value="1" label="高级讲师"/>
              <el-option :value="2" label="首席讲师"/>
            </el-select>
          </el-form-item>
          <el-form-item label="讲师资历">
            <el-input v-model="teacher.career"/>
          </el-form-item>
          <el-form-item label="讲师简介">
            <el-input v-model="teacher.intro" :rows="10" type="textarea"/>
          </el-form-item>
          <!-- 讲师头像：TODO -->

          <!-- 讲师头像 -->
    <el-form-item label="讲师头像">
    <!-- 头衔缩略图 -->
    <pan-thumb :image="teacher.avatar"/>
    <!-- 文件上传按钮 -->
    <el-button type="primary" icon="el-icon-upload" @click="imagecropperShow=true">更换头像
    </el-button>
    <!--
v-show：是否显示上传组件
:key：类似于id，如果一个页面多个图片上传控件，可以做区分
:url：后台上传的url地址
@close：关闭上传组件
@crop-upload-success：上传成功后的回调 -->
    <image-cropper
                   v-show="imagecropperShow"
                   :width="300"
                   :height="300"
                   :key="imagecropperKey"
                   :url="BASE_API+'/eduoss/fileoss'"
                   field="file"
                   @close="close"
                   @crop-upload-success="cropSuccess"/>
        </el-form-item>


          <el-form-item>
            <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存</el-button>
          </el-form-item>
        </el-form>
      </div>
    </template>

<script>

import teacherApi from '@/api/teacher/teacher'
import ImageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'

    export default{
      components:{ImageCropper,PanThumb},
        data(){ 
            return{
            teacher:{
                name:"",
                sort:0,
                level:1,
                career:"",
                intro:"",
                avatar:'',
            }, //封装教师提交表单
            saveBtnDisabled : false, //保存按钮是否禁用
            //头像上传弹窗是否显示
            imagecropperShow : false,
            BASE_API: process.env.BASE_API, //获取dev.env.js的API地址
            imagecropperKey : 0, //上传组件的唯一标识
            }
        },
        methods: {
          //页面初始化
          init(){
                        //获取路由中的id参数值
            if (this.$route.params && this.$route.params.id){
                const id = this.$route.params.id
                this.getInfo(id)
                // console.log("id是：",id)
             }else{
              this.teacher = {}
             }

          },
          close(){ //关闭上传弹窗的方法
            this.imagecropperShow = false
          },
          // 头像上传成功调用的方法
          cropSuccess(resData){
            //上传成功之后返回上传成功的接口地址
            this.teacher.avatar = resData.url
            this.imagecropperShow = false
            this.imagecropperKey = this.imagecropperKey+1
          },
            //数据回显
            getInfo(id){
                teacherApi.selectTeacherById(id).then(res=>{
                    this.teacher = res.data.teacher
                })
            },

            saveOrUpdate(){
              if (this.$route.params && this.$route.params.id){
                //修改
                this.updateTeacher()
             }else{
                //添加
                this.saveTeacher()
             }

            },
            //添加老师方法
            saveTeacher(){
                teacherApi.addTeacher(this.teacher).then(res=>{
                    //提示信息
                    this.$message({
                        type: "success",
                        message: "添加成功"
                    })
                    //返回列表页面 路由跳转
                    this.$router.push({path:'/teacher/table'})
                })
            },
            updateTeacher(){
              teacherApi.updateTeacher(this.teacher).then(res=>{
                this.$message({
                  type:"success",
                  message:"修改成功"
                })
                this.$router.push({path:'/teacher/table'})
              }).catch(error=>{
                this.$message({
                  type:"error",
                  message:"修改失败"
                })
              })
            }
            
        },
        created() {
          console.log(this.$router)
            //获取路由中的id参数值
            this.init()
        },
        //监听
        watch:{
          $route(to,form){ //路由变化的方式，当路由发生变化 方法会执行
            console.log('watch $route')
            this.init()
          }
        }
    }
</script>