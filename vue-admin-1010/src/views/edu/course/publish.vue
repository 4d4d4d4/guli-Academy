<template>
    <div class="app-container">
        <h2 style="text-align: center;">发布新课程</h2>
        <el-steps :active="3" process-status="wait" align-center style="margin-bottom: 40px;">
            <el-step title="编辑课程基本信息"/>
            <el-step title="构建课程大纲"/>
            <el-step title="发布课程"/>
        </el-steps>

        <div class="ccInfo">
          <img :src="publishCourse.cover" style="width: 200px;height: 200px;">
          <div class="main">
            <h2>{{ publishCourse.title }}</h2>
            <p class="gray"><span>共{{ publishCourse.lessonNum }}课时</span></p>
            <p><span>所属分类：{{ publishCourse.subjectLevelOne }} — {{ publishCourse.subjectLevelTwo }}</span></p>
            <p>课程讲师：{{ publishCourse.teacherName }}</p>
            <h3 class="red" style="top: 175px;">￥{{ publishCourse.price }}</h3>
          </div>
        </div>

        <div>
            <el-button @click="previous">返回修改</el-button>
            <el-button :disabled="saveBtnDisabled" type="primary" @click="publish">发布课程</el-button>
        </div>
    </div>
    </template>
    <script>



import course from '@/api/course/course'

    export default {
    data() {
        return {
            saveBtnDisabled: false, // 保存按钮是否禁用
            courseId:'',
            publishCourse:{}   //课程发布信息
        }
     },
    created() {
        console.log('publish created')
        //获取路由课程id
        if(this.$route.params && this.$route.params.id){
            this.courseId = this.$route.params.id
            //调用接口方法根据课程id查询
            this.getCoursePublishById()
            console.log(this.publishCourse)
        }
    },
    methods: {
        //根据课程id查询
        getCoursePublishById(){
            course.getPublishCourseInfo(this.courseId).then(res=>{
                this.publishCourse = res.data.coursePublishVO
                console.log(this.publishCourse)
            })
        },

        previous() {
            console.log('previous')
            this.$router.push({ path: '/course/chapter/'+this.courseId })
     },
    publish() {
        console.log('publish')
        course.publishCourse(this.courseId).then(res=>{
            this.$message({
                type:"success",
                message:"发布成功"
            })
        })
        this.$router.push({ path: '/course/list' })
     }
     }
    }
    </script>


<style scoped>
    .ccInfo {
        background: #f5f5f5;
        padding: 20px;
        overflow: hidden;
        border: 1px dashed #DDD;
        margin-bottom: 40px;
        position: relative;
    }
    .ccInfo img {
        background: #d6d6d6;
        width: 500px;
        height: 278px;
        display: block;
        float: left;
        border: none;
    }
    .ccInfo .main {
        margin-left: 520px;
    }
    .ccInfo .main h2 {
        font-size: 28px;
        margin-bottom: 30px;
        line-height: 1;
        font-weight: normal;
    }
    .ccInfo .main p {
        margin-bottom: 10px;
        word-wrap: break-word;
        line-height: 24px;
        max-height: 48px;
        overflow: hidden;
    }
    .ccInfo .main p {
        margin-bottom: 10px;
        word-wrap: break-word;
        line-height: 24px;
        max-height: 48px;
        overflow: hidden;
    }
    .ccInfo .main h3 {
        left: 540px;
        bottom: 20px;
        line-height: 1;
        font-size: 28px;
        color: #d32f24;
        font-weight: normal;
        position: absolute;
    }
    </style>