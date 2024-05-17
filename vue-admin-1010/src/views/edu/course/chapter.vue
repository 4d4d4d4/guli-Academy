<template>
    <div class="app-container">
        <h2 style="text-align: center;">发布新课程</h2>
        <el-steps :active="2" process-status="wait" align-center style="margin-bottom: 40px;">
            <el-step title="编辑课程基本信息"/>
            <el-step title="构建课程大纲"/>
            <el-step title="发布课程"/>
        </el-steps>
        <el-button type="text" @click="openChapterDialog">添加章节</el-button>
        <!-- 章节 -->
<ul class="chanpterList">
    <li
        v-for="chapter in chapterVideoList"
        :key="chapter.id">
        <p>
            {{ chapter.title }}
            <span class="acts" style="z-index: 99999;">
                <el-button type="text" style="z-index: 999;" @click="openVideo(chapter.id)">添加课时</el-button>
                <el-button type="text" @click="openEditChapter(chapter.id)">编辑</el-button>
                <el-button type="text" @click="deleteChapter(chapter.id)">删除</el-button>
            </span>
        </p>
        <!-- 视频 -->
        <ul class="chanpterList videoList">
            <li
                v-for="video in chapter.children"
                :key="video.id">
                <p>{{ video.title }}
                    <span class="acts">
                        <el-button type="text"  style="z-index: 999;" @click="openEditVideo(video.id)">编辑</el-button>
                        <el-button type="text" @click="deleteVideo(video.id)">删除</el-button>
                    </span>
                </p>
            </li>
        </ul>
    </li>
</ul>

        <el-form label-width="120px">
                <el-form-item>
                <el-button @click="previous">上一步</el-button>
                <el-button :disabled="saveBtnDisabled" type="primary" @click="next">下一步</el-button>
            </el-form-item>
        </el-form>

<!-- 弹窗 -->
        <!-- 添加和修改章节表单 -->
<el-dialog :visible.sync="dialogChapterFormVisible" title="添加章节">
    <el-form :model="chapterData" label-width="120px">
        <el-form-item label="章节标题">
            <el-input v-model="chapterData.title"/>
        </el-form-item>
        <el-form-item label="章节排序">
            <el-input-number v-model="chapterData.sort" :min="0" controls-position="right"/>
        </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
        <el-button @click="dialogChapterFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate">确 定</el-button>
    </div>
</el-dialog>

<!-- 添加和修改课时表单 -->
<el-dialog :visible.sync="dialogVideoFormVisible" title="添加课时">
  <el-form :model="video" label-width="120px">
    <el-form-item label="课时标题">
      <el-input v-model="video.title"/>
    </el-form-item>
    <el-form-item label="课时排序">
      <el-input-number v-model="video.sort" :min="0" controls-position="right"/>
    </el-form-item>
    <el-form-item label="是否免费">
      <el-radio-group v-model="video.free">
        <el-radio :label="true">免费</el-radio>
        <el-radio :label="false">默认</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="上传视频">
            <el-upload
                   :on-success="handleVodUploadSuccess"
                   :on-remove="handleVodRemove"
                   :before-remove="beforeVodRemove"
                   :on-exceed="handleUploadExceed"
                   :file-list="fileList"
                   :action="BASE_API+'/eduvod/video/upload'"
                   :limit="1"
                   class="upload-demo">
            <el-button size="small" type="primary">上传视频</el-button>
            <el-tooltip placement="right-end">
                <div slot="content">最大支持1G，<br>
                    支持3GP、ASF、AVI、DAT、DV、FLV、F4V、<br>
                    GIF、M2T、M4V、MJ2、MJPEG、MKV、MOV、MP4、<br>
                    MPE、MPG、MPEG、MTS、OGG、QT、RM、RMVB、<br>
                    SWF、TS、VOB、WMV、WEBM 等视频格式上传</div>
                <i class="el-icon-question"/>
            </el-tooltip>
            </el-upload>
        </el-form-item>
    </el-form-item>
  </el-form>
  <div slot="footer" class="dialog-footer">
    <el-button @click="dialogVideoFormVisible = false">取 消</el-button>
    <el-button :disabled="saveVideoBtnDisabled" type="primary" @click="saveOrUpdateVideo">确 定</el-button>
  </div>
</el-dialog>
    </div>
</template>

    <script>
import chapter from '@/api/chapter/chapter'
import course from '@/api/course/course'
import video from '@/api/video/video'
    
export default {
    data() {
        return {
            chapterData:{
                title:"" ,
                sort:0,
                courseId:this.$route.params.id
            }, //封装章节对象
            video:{
                title:'',
                sort:'',
                free:'',
                courseId:''
            },
            saveBtnDisabled: false, // 保存按钮是否禁用
            chapterVideoList:[], //封装章节内容 内有属性children封装的对应的小节内容
            courseId:'', //课程id
            dialogChapterFormVisible:false, //章节弹窗是否显示
            dialogVideoFormVisible:false, //小节弹窗是否显示

            fileList: [],//上传文件列表
            BASE_API: process.env.BASE_API // 接口API地址
        }
     },
    created() {
        console.log(this.$route.params.id)
        this.init()
     },

    methods: {  
        //点击×调用方法
        beforeVodRemove(file , fileList){
            return this.$confirm(`确定移除${file.name}?`)
        },

        //点击确定方法
        handleVodRemove(){
            video.deleteVideoById(this.video.videoSourceId).then(res=>{
                this.$message({
                    type:"success",
                    message:'删除成功'
                })
                //清空文件列表
                this.fileList = []
                //清空视频id
                this.video.videoOriginalName = ""
                this.video.videoSourceId = ""
            })
        },


        //成功回调
        handleVodUploadSuccess(response, file, fileList) {  
            //获取视频id
            this.video.videoSourceId = response.data.videoId
            //获取视频原始名称
            this.video.videoOriginalName = file.name

            console.log('successhandle',this.video)
        },
    //视图上传多于一个视频
        handleUploadExceed(files, fileList) {
            this.$message.warning('想要重新上传视频，请先删除已上传的视频')
        },


    //**********************************小节方法*********************************
    openVideo(chapterId){
        this.video = {}
        this.fileList = []
        //弹窗
        this.dialogVideoFormVisible = true
        this.video.chapterId = chapterId
        this.video.courseId = this.chapterData.courseId
        console.log(this.video)
    },
    //编辑小节窗口
    openEditVideo(id){
        console.log(id)
        this.video = {} 
        //弹窗
        this.dialogVideoFormVisible = true
        video.getVideoInfo(id).then(res=>{
            this.video = res.data.video
        })
        console.log(this.video)
    },
    //保存小节
    saveVideo(){
        console.log("save",this.video)
        video.addVideo(this.video).then(res=>
        {
            console.log("saveAfter",this.video)
            this.$message({
                type:'success',
                message:'添加小节成功'
            })
            this.getChapterVideo()
            this.dialogVideoFormVisible = false
        })
    },

    //编辑小节
    editVideo(){
        video.updateVideo(this.video).then(res=>{
            this.$message({
                type:'success',
                message:'修改小节成功'
            })
            this.getChapterVideo()
            this.dialogVideoFormVisible = false
        })
    },

    //删除小节
    deleteVideo(id){
        this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
         video.deleteVideo(id)   
         .then((res) => {
        // this.fetchData()
        this.$message({
            type: 'success',
            message: '删除成功!'
        })
        this.getChapterVideo()
    })
    })
    },


    saveOrUpdateVideo(){
        if(this.video.id){
            this.editVideo()
        }else{
            this.saveVideo()
        }
    },




    //==================================章节操作=================================
        openEditChapter(chapterId){
            this.dialogChapterFormVisible = true

            chapter.getChapter(chapterId).then(res=>{
                this.chapterData = res.data.chapter
            })
        },
        aaa(){
            console.log('sssssss')
        },
        openChapterDialog(){
            this.dialogChapterFormVisible = true
            this.chapterData.title = ""
            this.chapterData.sort = 0   
        },         
        //添加章节
        saveChapter(){
            chapter.addChapter(this.chapterData).then(res=>{
                this.$message({
                    type:"success",
                    message:"添加成功"
                })
                this.getChapterVideo()
                this.dialogChapterFormVisible = false
            }).catch(error=>{
                this.$message({
                    type:"error",
                    message:error.data.message
                })
            })
        },
        
        //删除章节
        deleteChapter(chapterId){
            this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
         chapter.deleteChapter(chapterId)   
         .then((res) => {
        // this.fetchData()
        this.$message({
            type: 'success',
            message: '删除成功!'
        })
        this.getChapterVideo()
    })
    })
        },

        //修改章节
        updateChapter(){
            chapter.updateChapter(this.chapterData).then(res=>{
                this.$message({
                    type:"success",
                    message:"修改成功"
                })
                this.getChapterVideo()
                this.dialogChapterFormVisible = false
            }).catch(error=>{
                this.$message({
                    type:"error",
                    message:error.data.message
                })
            })
        },

        //添加或者修改章节
        saveOrUpdate(){
            if(!this.chapterData.id){
                this.saveChapter()
            }else{
                this.updateChapter()
            }
        },

        //根据课程id查询章节和小节
        getChapterVideo(){
            chapter.getAllChapterVideo(this.courseId).then(res=>{
                console.log(res.data)
                this.chapterVideoList = res.data.list
                // console.log(this.chapterVideoList)
            })
        },
        previous() {
        // console.log('previous')
         course.getCourseInfoById(this.courseId).then(res=>{
            this.$router.push({ path:"/course/info/"+this.courseId})
         })
     },
    next() {
        // console.log('next')
        this.$router.push({ path: '/course/publish/'+this.courseId })
        },
        //初始化
        init(){
            this.courseId = this.$route.params.id
            this.getChapterVideo()
            // console.log(this.$route)
        }

     },
     //监听
     watch:{
          $route(to,form){ //路由变化的方式，当路由发生变化 方法会执行
            this.init()
          }
    }

    }
    </script>

<style scoped>
    .chanpterList{
        position: relative;
        list-style: none;
        margin: 0;
        padding: 0;
    }
    .chanpterList li{
      position: relative;
    }
    .chanpterList p{
      /* float: left; */
      font-size: 20px;
      margin: 10px 0;
      padding: 10px;
      height: 70px;
      line-height: 50px;
      width: 100%;
      /* border: 1px solid #DDD; */
    }
    .chanpterList .acts {
        float: right;
        font-size: 14px;
    }
    .videoList{
      padding-left: 50px;
    }
    .videoList p{
      /* float: left; */
      font-size: 14px;
      margin: 10px 0;
      padding: 10px;
      height: 50px;
      line-height: 30px;
      width: 100%;
      border: 1px dotted #DDD;
    }
    </style>
