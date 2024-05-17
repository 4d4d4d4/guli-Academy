<template>
<div class="app-container">
    <h2 style="text-align: center;">发布新课程</h2>
    <el-steps :active="1" process-status="wait" align-center style="margin-bottom: 45px;">
    <el-step title="编辑课程基本信息"></el-step>
    <el-step title="构建课程大纲"></el-step>
    <el-step title="发布课程"></el-step>
  </el-steps>
    <el-form label-width="140px">
        <el-form-item label="课程标题">
            <el-input v-model="courseInfo.title" placeholder=" 示例：机器学习项目课：从基础到搭建项目视频课程。专业名称注意大小写"/>
            </el-form-item>
            <!-- 所属分类 TODO -->
            <!-- 课程分类(一级) -->
            <el-form-item label="课程分类">
                <el-select
                    v-model="courseInfo.subjectParentId"
                    placeholder="请选择一级分类"
                    @change="changeOneSubejct">
                    <el-option
                        v-for="subjectOne in subjectOneList"
                        :key="subjectOne.id"
                        :label="subjectOne.title"
                        :value="subjectOne.id"/>
                </el-select>
                            <!-- 课程分类(二级) -->
                <el-select
                v-model="courseInfo.subjectId"
                placeholder="请选择二级分类">
                <el-option
                    v-for="subjectTwo in subjectTwoList"
                    :key="subjectTwo.id"
                    :label="subjectTwo.title"
                    :value="subjectTwo.id"/>
            </el-select>
            </el-form-item>         

            <!-- 课程讲师 TODO -->

            <!-- 课程讲师 -->
            <el-form-item label="课程讲师">
                <el-select
                    v-model="courseInfo.teacherId"
                    placeholder="请选择">
                    <el-option
                        v-for="teacher in teacherList"
                        :key="teacher.id"
                        :label="teacher.name"
                        :value="teacher.id"/>
                </el-select>
            </el-form-item>

        <el-form-item label="总课时">
            <el-input-number :min="0" v-model="courseInfo.lessonNum" controls-position="right" placeholder="请填写课程的总课时数"/>
        </el-form-item>
        <!-- 课程简介 富文本编辑 -->
        <el-form-item label="课程简介">
          <tinymce :height="250" v-model="courseInfo.description"/>
         </el-form-item>


        <!-- 课程封面 TODO -->
        <el-form-item label="课程封面">
            <el-upload
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
              :action="'http://localhost:9001'+'/eduoss/fileoss'"
              class="avatar-uploader">
              <img :src="courseInfo.cover" style="width: 300px;height: 200px;">
            </el-upload>
          </el-form-item>

        </el-form-item>



        <el-form-item label="课程价格">
        <el-input-number :min="0" v-model="courseInfo.price" controls-position="right" placeholder="免费课程请设置为0元"/> 元
    </el-form-item>
    <el-form-item>
    <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdateCourse">保存并下一步</el-button>
    </el-form-item>
    </el-form>
</div>
</template>

<style scoped>
  .tinymce-container{
    line-height: 20px;
  }
</style>

  <script>
import course from '@/api/course/course'
import teacher from '@/api/teacher/teacher'
import subject from '@/api/subject/subject'
import Tinymce from '@/components/Tinymce' //声明富文本编辑器组件
    export default {
      components:{Tinymce}, //引入组件
      data() {
        return {
          active: 0,
          saveBtnDisabled:false,//保存并下一步按钮是否禁用
          courseInfo:{
                title:'',
                subjectId:'',
                teacherId:'',
                lessonNum:0,
                description:'',
                subjectParentId:'',//一级分类id
                subjectId:'',//二级分类的id
                cover:'https://cqy521.oss-cn-beijing.aliyuncs.com/2aa2549c07e94a8f9b2046ed971cd1f5.jpg',
                price:0
          },  //封装课程信息
          teacherList:[], //封装所有讲师列表
          subjectOneList:[],//一级分类的数组
          subjectTwoList:[],//二级分类
          flag:false,//标记是修改还是添加 默认添加
        };
      },
  
      methods: {
        //上传封面成功调用的方法
        handleAvatarSuccess(res, file){
          console.log(1234)
          this.courseInfo.cover = res.data.url
          console.log(res.data.url)
        },
        //上传之前调用的方法
        beforeAvatarUpload(file){
          const isJPGorPNG = (file.type === 'image/jpeg') || (file.type === 'image/png')
          const isLt2M = file.size /1024 /1024 < 2
          console.log(isJPGorPNG)
          if(!isJPGorPNG){
            this.$message.error('头像只能上传png或者jpeg格式')
          }
          if(!isLt2M){
            this.$message.error('上传头像图片大小不要超过2MB')
          }
          return isJPGorPNG && isLt2M
        },
        saveOrUpdateCourse() {
          if(this.flag){
           course.updateCourseInfo(this.courseInfo).then(res=>{
            //提示成功
            this.$message({
                type:"success",
                message:"编辑课程信息成功"
            })
            let courseId = this.$route.params.id
            //跳转到第二步
           this.$router.push({path:"/course/chapter/"+courseId})
           }) 
          }else{
           course.addCourseInfo(this.courseInfo).then(res=>{
            //提示成功
            this.$message({
                type:"success",
                message:"编辑课程信息成功"
            })
            let courseId = res.data.courseId
            //跳转到第二步
           this.$router.push({path:"/course/chapter/"+courseId})
           })
          }
        },
        //获得所有讲师的列表
        getTeacherList(){
            teacher.getAllTeacherList().then(res=>{
                this.teacherList = res.data.items
            })
        },
        //查询所有一级分类的列表
        getOneSubjectList(){
            subject.getAllSubjectWithTree().then(res=>{
                this.subjectOneList = res.data.list
            })
        },
        //选择一级分类后 根据一级分类(id)确定二级分类
        changeOneSubejct(value){ //参数为一级分类的id值
          // console.log(value)
          this.courseInfo.subjectId = ''
          for(let i = 0;i<this.subjectOneList.length;i++){
            if(this.subjectOneList[i].id == value){
              this.subjectTwoList = this.subjectOneList[i].children
            }
          }
        }
      },

      created(){
        console.log(this.$route)
        if(this.$route.params.id){

          course.getCourseInfoById(this.$route.params.id).then(res=>{
            console.log('ssss',res.data)
            this.courseInfo = res.data.courseInfo
          })
          this.flag = true
        }
        //初始化一级分类
        this.getOneSubjectList(),
        //初始化讲师
        this.getTeacherList()

      },
      //监听
     watch:{
          $route(to,form){ //路由变化的方式，当路由发生变化 方法会执行
            console.log("我发生了变化")
          }
    }
    }
  </script>