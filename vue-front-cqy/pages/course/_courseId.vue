
<template>
  <client-only>
    <div id="aCoursesList" class="bg-fa of">
   <!-- /课程详情 开始 -->
   <section class="container">
     <section class="path-wrap txtOf hLh30">
       <a href="#" title class="c-999 fsize14" @click="goto('/')">首页</a>
       \
       <a href="#" title class="c-999 fsize14" @click="goto('/course')">课程列表</a>
       \
       <span class="c-333 fsize14">{{courseInfo.title}}</span>
     </section>
     <div>
       <article class="c-v-pic-wrap" style="height: 357px;">
         <section class="p-h-video-box" id="videoPlay">
           <img :src="courseInfo.cover" :alt="courseInfo.title" class="dis c-v-pic" onerror="this.src='https://tse3-mm.cn.bing.net/th/id/OIP-C.TsVc5vK8h3vd_9JjMByfwwHaFq?pid=ImgDet&rs=1'"> 
         </section>
       </article>
       <aside class="c-attr-wrap">
         <section class="ml20 mr15">
           <h2 class="hLh30 txtOf mt15">
             <span class="c-fff fsize24">{{courseInfo.title}}</span>
           </h2>
           <section class="c-attr-jg">
             <span class="c-fff">价格：</span>
             <b class="c-yellow" style="font-size:24px;">￥{{courseInfo.price}}</b>
           </section>
           <section class="c-attr-mt c-attr-undis">
             <span class="c-fff fsize14">主讲： {{mainTeacherInfo.name}}&nbsp;&nbsp;&nbsp;</span>
           </section>
           <section class="c-attr-mt of">
             <span class="ml10 vam">
               <em class="icon18 scIcon"></em>
               <a class="c-fff vam" title="收藏" href="#" >收藏</a>
             </span>
           </section>
           <section class="c-attr-mt">
            <a :href="'/player/'+defalutVideoId" title="立即观看" target="_blank"  class="comm-btn c-btn-3" v-if="courseInfo.price<=0 || payFlag">立即观看</a> 
             <a @click="createOrders()" href="#" title="立即购买"  class="comm-btn c-btn-3" v-if="courseInfo.price>0 && !payFlag">立即购买</a>
           </section>
         </section>
       </aside>
       <aside class="thr-attr-box">
         <ol class="thr-attr-ol clearfix">
           <li>
             <p>&nbsp;</p>
             <aside>
               <span class="c-fff f-fM">购买数</span>
               <br>
               <h6 class="c-fff f-fM mt10">{{courseInfo.buyCount}}</h6>
             </aside>
           </li>
           <li>
             <p>&nbsp;</p>
             <aside>  
               <span class="c-fff f-fM">课时数</span> 
               <br>
                 <h6 class="c-fff f-fM mt10">{{courseInfo.lessonNum}}</h6>  
             </aside>
           </li>
           <li>
             <p>&nbsp;</p>
             <aside>
               <span class="c-fff f-fM">浏览数</span>
               <br>
               <h6 class="c-fff f-fM mt10">{{courseInfo.viewCount}}</h6>
             </aside>
           </li>
         </ol>
       </aside>
       <div class="clear"></div>
     </div>
     <!-- /课程封面介绍 -->
     <div class="mt20 c-infor-box">
       <article class="fl col-7">
         <section class="mr30">
           <div class="i-box">
             <div>
               <section id="c-i-tabTitle" class="c-infor-tabTitle c-tab-title">
                 <a name="c-i" class="current" title="课程详情">课程详情</a>
               </section>
             </div>
             <article class="ml10 mr10 pt20">
               <div>
                 <h6 class="c-i-content c-infor-title">
                   <span>课程介绍</span>
                 </h6>
                 <div class="course-txt-body-wrap">
                   <section class="course-txt-body">
                     <p v-html="courseInfo.description">
                       {{courseInfo.description}}
                     </p>
                   </section>
                 </div>
               </div>
               <!-- /课程介绍 -->
               <div class="mt50">
                 <h6 class="c-g-content c-infor-title">
                   <span>课程大纲</span>
                 </h6>
                 <section class="mt20">
                   <div class="lh-menu-wrap">
                     <menu id="lh-menu" class="lh-menu mt10 mr10">
                       <ul>
                         <!-- 文件目录 -->
                         <li class="lh-menu-stair" v-for="(syllabus,index) in syllabusList" :key="index">
                           <a href="javascript: void(0)" :title="syllabus.title" class="current-1">
                             <em class="lh-menu-i-1 icon18 mr10"></em>{{syllabus.title}}
                           </a>
                           <ol class="lh-menu-ol" style="display: block;">
                             <li class="lh-menu-second ml30" v-for="(video,index) in syllabus.children" :key="index">
                               <a :href="'/player/'+video.videoSourceId" target="_blank" title v-bind:class="{'current-2': (index>=2)  }">
                                
                                 <span class="fr" v-if="index<2">
                                   <i class="free-icon vam mr10">免费试听</i>
                                 </span>
                                 <em class="lh-menu-i-2 icon16 mr5">&nbsp;</em>{{video.title}}
                               </a>
                             </li>
                           </ol>
                         </li>
                       </ul>
                     </menu>
                   </div>
                 </section>
               </div>
               <!-- /课程大纲 -->
             </article>
           </div>
         </section>
       </article>
       <aside class="fl col-3">
         <div class="i-box">
           <div>
             <section class="c-infor-tabTitle c-tab-title">
               <a title href="javascript:void(0)">主讲讲师</a>
             </section>
             <section class="stud-act-list">
               <ul style="height: auto;">
                 <li>
                   <div class="u-face">
                     <a href="#">
                       <img :src="mainTeacherInfo.avatar" width="50" height="50" alt>
                     </a>
                   </div>
                   <section class="hLh30 txtOf">
                     <a class="c-333 fsize16 fl" href="#">{{mainTeacherInfo.name}}</a>
                   </section>
                   <section class="hLh20 txtOf">
                     <span class="c-999">{{mainTeacherInfo.intro}}</span>
                   </section>
                 </li>
               </ul>
             </section>
           </div>
         </div> 
       </aside>  
       <div class="clear"></div>  
     </div>  
      </section>
      <!-- /课程详情 结束 -->
      <div class="comments">

    <!-- 评论列表 -->
    <!-- TODO 完善回复功能 -->
        <div class="comment-wrap" v-for="(comment,index) in data.list" :key="index">
            <div class="photo">
                <img :src="comment.avatar" style="width: 50px;height: 50px;"  onerror="this.src='https://img.zcool.cn/community/01258f5b56bd8ba80121ade00bb711.png@1280w_1l_2o_100sh.png'">
                <a>{{comment.nickname}}</a>
            </div>
            <div class="comment-block">
                <p class="comment-text">{{comment.content}}</p>
                <div class="bottom-comment">
                    <div class="comment-date">{{comment.gmtCreate}}</div>
                    <ul class="comment-actions">
                        <li class="complain">举报</li>
                        <li class="reply">回复</li>
                    </ul>
                </div>
            </div>
        </div>
        <!-- 发送评论 -->
        <div class="comment-wrap">
          <div class="photo">
              <div class="avatar" style="background-image: url('/assets/coolboy.jpg')"></div>
          </div>
          <div class="comment-block">
              <form action="">
                  <textarea v-model="comment.content" placeholder="Say somthing..."></textarea>
                  <el-button style="margin-left: 800px;" @click="appendComment()">发表</el-button>
              </form>
          </div>
      </div>
    </div>
                <!-- 公共分页 开始 -->
                <div>
                  <div class="paging">
                    <!-- undisable这个class是否存在，取决于数据属性hasPrevious -->
                    <a
                      :class="{undisable: !data.hasPrevious}"
                      href="#"
                      title="首页"
                      @click.prevent="gotoPage(1)">首页</a>
            
                    <a
                      :class="{undisable: !data.hasPrevious}"
                      href="#"
                      title="前一页"
                      @click.prevent="gotoPage(data.current-1)">&lt;</a>
            
                    <a
                      v-for="page in data.pages"
                      :key="page"
                      :class="{current: data.current == page, undisable: data.current == page}"
                      :title="'第'+page+'页'"
                      href="#"
                      @click.prevent="gotoPage(page)">{{ page }}</a>
            
                    <a
                      :class="{undisable: !data.hasNext}"
                      href="#"
                      title="后一页"
                      @click.prevent="gotoPage(data.current+1)">&gt;</a>
            
                    <a
                      :class="{undisable: !data.hasNext}"
                      href="#"
                      title="末页"
                      @click.prevent="gotoPage(data.pages)">末页</a>
            
                    <div class="clear"/>
                  </div>
                </div>
    </div>


</client-only>
  </template>
  <style>
    @import "@/assets/css/comment.css";
    /* 或者直接在组件内部使用内联样式 */
    /* 例如： */
    /* .comment {
      color: red;
    } */
  </style>
  <script>
    import courseApi from '@/api/course'
    import teacherApi from '@/api/teacher'
    import commentApi from '@/api/comment'
    import orderApi from '@/api/order'
    import cookie from 'js-cookie'

  export default {
    data(){
      return{
        courseId:'', //课程id
        courseInfo:{}, //课程详情
        mainTeacherInfo:{},//主讲讲师详情
        syllabusList:[], //课程大纲列表
        current:1,
        limit:5,
        commentList:[],//评论列表
        comment:{}, //评论封装对象
        hasPrevious:'', //是否有上一页
        hasNext:'',//是否有下一页
        data:{},
        defalutVideoId:'',//默认的视频id
        payFlag:false,  //商品是否支付 默认false
      }
    },
    methods:{
      goto(path){
        this.$router.push(path)
      },
      //分页切换
      gotoPage(current){
        commentApi.getCommentByPage(current,5).then(res=>{
                this.data = res.data.data
              })
            },
      initPage(){
        this.courseId = this.$route.params.courseId
        courseApi.getCourseInfoByCourseId(this.courseId).then(res=>{
          this.courseInfo = res.data.data.courseInfo
          console.log(this.courseInfo)
          teacherApi.getTeacherInfo(this.courseInfo.teacherId).then(res=>{
            this.mainTeacherInfo = res.data.data.teacher
          })
          courseApi.getChapteAndVideoList(this.courseId).then(res=>{
            this.syllabusList = res.data.data.list
            this.defalutVideoId = res.data.data.list[0].children[0].videoSourceId
            console.log('大纲列表',this.syllabusList)
          })
        })
        orderApi.getOrdersInfoByMemberIdAndCourseId(JSON.parse(cookie.get('cqy_ucenter')).id,this.$route.params.courseId).then(res=>{
          if(res.data.data.orderInfo.status === 1){
            this.payFlag = true
          }
        })
      },
      //初始化评论区
      initCommpentList(current=1){
        current = this.current
        commentApi.getCommentByPage(current,this.limit).then(res=>{
          this.commentList = res.data.data.list
          this.data = res.data.data
        })
      },
      //发送评论
      appendComment(){
        if(!this.comment.content || this.comment.content === ''){
          this.$message({
            type:'warn',
            message:'评论内容不能为空'
          })
        
        }else{
        if(!cookie.get('cqy_ucenter')){
          this.$message({
            type:'warn',
            message:'用户未登录'
          })
        }else{
          this.comment.courseId = this.courseId
          this.comment.teacherId = this.mainTeacherInfo.id
          this.comment.memberId = JSON.parse(cookie.get('cqy_ucenter')).id
         commentApi.appendComment(this.comment).then(res=>{
          this.comment.content = ''
          this.$message({
            type:'success',
            message:"评论已发送",
          })
          this.initCommpentList()
        }).catch(err=>{
          this.$message({
          type:'error',
          message:'发送失败'
        })
        })
      }
      }
      this.initCommpentList()
    },
    //生成订单
    createOrders(){
      orderApi.createOrders(this.courseId).then(res=>{
        //获取订单号
        res.data.data.orderId
        //跳转到订单购买页面
        this.$router.push({path:'/orders/'+res.data.data.orderId})
      })
    }
    },
    created(){
      this.initPage()
      this.initCommpentList()
    }
  };
  </script>


  