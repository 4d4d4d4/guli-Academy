<template>
<client-only>
    <div id="aCoursesList" class="bg-fa of">
    
     <!-- /课程列表 开始 -->
    
     <section class="container">
    
       <header class="comm-title">
    
         <h2 class="fl tac">
    
           <span class="c-333">全部课程</span>
    
         </h2>
    
       </header>
    
       <section class="c-sort-box">
         <section class="c-s-dl">
           <dl>
             <dt>
               <span class="c-999 fsize14">课程类别</span>
             </dt>
             <dd class="c-s-dl-li">
               <ul class="clearfix">
                <li>
                  <a title="全部" href="#" @click.prevent="changeParentSubject(-1)">全部</a>
                </li>
                 <li v-for="(oneSubject,index) in subjectNestedList" :key="index">
                   <a :title="oneSubject.title" href="#" @click.prevent="changeParentSubject(index)">{{oneSubject.title}}</a>
                 </li>
               </ul>
             </dd>
           </dl>
           <dl>
             <dt>
               <span class="c-999 fsize14"></span>
             </dt>
             <dd class="c-s-dl-li">
               <ul class="clearfix">
    
                 <li v-for="(twoSubejct,index) in subjectList" :key="index">
                   <a :title="twoSubejct.title" href="#" @click.prevent="changeSubject(index)">{{twoSubejct.title}}</a>
                 </li>
               </ul>
             </dd>
    
           </dl>
    
           <div class="clear"></div>
    
         </section>
    
         <div class="js-wrap">
    
           <section class="fr">
    
             <span class="c-ccc">
    
               <i class="c-master f-fM">1</i>/
    
               <i class="c-666 f-fM">1</i>
    
             </span>
    
           </section>
    
           <section class="fl">
    
             <ol class="js-tap clearfix">
    
               <li>
    
                 <a title="关注度" href="#" @click.prevent="sortByBuyCount()">关注度</a>
    
               </li>
    
               <li>
    
                 <a title="最新" href="#" @click.prevent="sortByGtmCreateTime()">最新</a>
    
               </li>
    
               <li class="current bg-orange">
    
                 <a title="价格" href="#" @click.prevent="sortByPrice()">价格&nbsp;
    
                   <span>↓</span>
    
                 </a>
    
               </li>
    
             </ol>
    
           </section>
    
         </div>
    
         <div class="mt40">
    
           <!-- /无数据提示 开始-->
           <section class="no-data-wrap" v-if="data.total == 0">
             <em class="icon30 no-data-ico">&nbsp;</em>
             <span class="c-666 fsize14 ml10 vam">没有相关数据，小编正在努力整理中...</span>
           </section>
    
           <!-- /无数据提示 结束-->
    
           <article v-if="data.total > 0 " class="comm-course-list">
             <ul class="of" id="bna">
               <li v-for="course in data.list" :key="course.id">
                 <div class="cc-l-wrap">
                   <section class="course-img">
                     <img :src="course.cover" style="height: 330px; height: 190px;" class="img-responsive" :alt="course.title"  onerror="this.src='https://tse3-mm.cn.bing.net/th/id/OIP-C.TsVc5vK8h3vd_9JjMByfwwHaFq?pid=ImgDet&rs=1'">
                     <div class="cc-mask">
                       <a :href="'/course/'+course.id" :title="course.title" class="comm-btn c-btn-1" target="_blank">开始学习</a>
                     </div>
                   </section>
                   <h3 class="hLh30 txtOf mt10">
                     <a :href="'/course/'+course.id" :title="course.title" class="course-title fsize18 c-333">{{course.title}}</a>
                   </h3>
                   <section class="mt10 hLh20 of">
                     <span class="fr jgTag bg-green" v-if="course.price <= 0">
                       <i class="c-fff fsize12 f-fA">免费</i>
                     </span>
                     <span class=" jgTag bg-blue" v-if="course.price > 0">
                      <i style="font-size:20px ;color: green;">￥<font style="color:red;">{{course.price}}</font></i>
                    </span>
                     <span class="fl jgAttr c-ccc f-fA">
                       <i class="c-999 f-fA">{{course.buyCount}}人学习</i>
    
                       |
    
                       <i class="c-999 f-fA">{{course.viewCount}}人浏览过</i>
                     </span>
                   </section>
                 </div>
               </li>
                    </ul>
                    <div class="clear"></div>
                  </article>
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
                <!-- 公共分页 结束 -->
              </section>
            </section>
            <!-- /课程列表 结束 -->
          </div>
        </client-only>
        </template>
        <script>
        import courseApi from '@/api/course'

        export default {
          data(){
            return{
              page:1,
              data:{},
              subjectNestedList:[],//一级分类列表
              subjectList:[],//二级分类列表
              searchObj:{}, //查询表单对象
              oneIndex:-1,//是否选中一级分类查询
              twoIndex:-1,//是否选中二级分类查询
              buyCountSort:"",
              gmtCreateSort:"",
              priceSort:""
            }
          },
          methods:{
            //查询初始课程数据
            initCourseFirst(){
              courseApi.getCourseListByCondition(this.searchObj,1,8).then(res=>{
                this.data = res.data.data

              })
            },
            //初始化分类
            initSubject(){
              courseApi.getSubjectWithTree().then(res=>{
                this.subjectNestedList = res.data.data.list
              })
            },
            //分页切换
            gotoPage(current){
              courseApi.getCourseListByCondition(this.searchObj,current,8).then(res=>{
                this.data = res.data.data
              })
            },
            //选择父级分类
            changeParentSubject(index){
              this.subjectList = []
              this.oneIndex = index
              if(index>=0){
                this.searchObj.subjectParentId = this.subjectNestedList[index].id
              this.subjectList = this.subjectNestedList[index].children
              }else{
                this.searchObj.subjectParentId = ''
                this.searchObj.subjectId = ''
                for(let i =0;i<this.subjectNestedList.length;i++){
                  for(let j=0;j<this.subjectNestedList[i].children.length;j++){
                    this.subjectNestedList[i].children[j].title =  this.subjectNestedList[i].children[j].title+'('+this.subjectNestedList[i].title+')'
                  this.subjectList.push(this.subjectNestedList[i].children[j])
                  }
                }
              }
              this.getCourseByCondition()
            },
            //选择子集课程分类
            changeSubject(index){
              this.twoIndex = index
              this.searchObj.subjectId = this.subjectList[index].id
              this.getCourseByCondition()
            },
            //条件查询课程（带分页）
            getCourseByCondition(){
              console.log('search',this.searchObj)
              courseApi.getCourseListByCondition(this.searchObj,1,8).then(res=>{
                this.data = res.data.data
              })
            },
            //根据购买量排序
            sortByBuyCount(){
              if(this.searchObj.buyCountSort && this.searchObj.buyCountSort != ''){
                this.searchObj.buyCountSort = ''
              }else{
                this.searchObj.buyCountSort = 1
              }
              this.getCourseByCondition()
            },
            //根据发布时间排序
            sortByGtmCreateTime(){
              if(this.searchObj.gmtCreateSort && this.searchObj.gmtCreateSort != ''){
                this.searchObj.gmtCreateSort = ''
              }else{
                this.searchObj.gmtCreateSort = 1
              }
              this.getCourseByCondition()

            },
            //根据价格排序
            sortByPrice(){
              if(this.searchObj.priceSort && this.searchObj.priceSort != ''){
                this.searchObj.priceSort = ''
              }else{
                this.searchObj.priceSort = 1
              }
              this.getCourseByCondition()

            } 
          },
          created(){
            //初始化第一页课程数据
            this.initCourseFirst()
            //初始化分类
            this.initSubject()
          }
        };
        </script>