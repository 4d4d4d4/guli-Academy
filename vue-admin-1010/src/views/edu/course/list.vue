<template>
    <div class="app-container">
        课程列表
<!--查询表单-->
<el-form :inline="true" class="demo-form-inline">
    <el-form-item>
    <el-input v-model="courseQuery.title" placeholder="课程名称"/>
    </el-form-item>
    <el-form-item>
    <el-select v-model="courseQuery.status" clearable placeholder="状态">
    <el-option :value="'Normal'" label="已发布"/>
    <el-option :value="'Draft'" label="未发布"/>
    </el-select>
    </el-form-item>
    <el-button type="primary" icon="el-icon-search" @click="getList()">查询</el-button>
    <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>

        <el-table
        v-loading="listLoading"
        :data="courseList"
        element-loading-text="数据加载中"
        border
        fit
        highlight-current-row>
        <el-table-column
        label="序号"
        width="70"
        align="center">
        <template slot-scope="scope">
         {{ (page - 1) * size + scope.$index + 1 }}
        </template>
        </el-table-column>
        <el-table-column prop="title" label="课程名称" width="80" />
        <el-table-column label="课程状态" width="80">
        <template slot-scope="scope">
         {{ scope.row.status==='Normal'?'已发布':'未发布' }}
        </template>
        </el-table-column>
        <el-table-column prop="lessonNum" label="课时数" />
        <el-table-column prop="gmtCreate" label="添加时间" width="160"/>
        <el-table-column prop="viewCount" label="浏览数量" width="60" />
        <el-table-column label="操作" width="200" align="center">

        <template slot-scope="scope">
        <router-link :to="'/course/info/'+scope.row.id">
        <el-button type="primary" size="mini" icon="el-icon-edit">修改课程基本信息</el-button>
        </router-link>
        <router-link :to="'/course/chapter/'+scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit" style="margin-top: 5px;">修改课程大纲信息</el-button>
            </router-link>
        <el-button type="danger" size="small" icon="el-icon-delete"
        @click="removeDataById(scope.row.id)" style="margin-top: 5px;width: 151px;">删除</el-button>
        </template>

        </el-table-column>
        </el-table>

        <!-- 分页 @current-change是一个方法 会将上面的参数传入方法(getList)中   -->
        <el-pagination
        :page-size="size" 
        :total="total"
        style="padding: 30px 0; text-align: center;"
        layout="total, prev, pager, next, jumper"
        @current-change="getList"
        />
        
    </div>
</template>
<script>
    // 引入调用teacher.js文件
    import course from '@/api/course/course'

    export default{
        // 写核心代码位置
        data(){ //定义变量和初始值
            return{
                page:1,//当前页
                size:5, //每页记录数
                total:0, //查询的总数量
                courseQuery:{
                    title:"",
                    status:""
                },//条件封装对象
                courseList:[] 
            }
        },
        created(){ //页面渲染之前执行，一般调用methods定义的方法
            this.getList()
        },
        methods:{ //创建具体的方法，调用teacher.js定义的
            getList(page = 1){  
                this.page = page
                course.getCourseInfoByCondition(this.courseQuery,this.page,this.size).then(res=>{
                    this.courseList = res.data.list
                    this.total = res.data.total
                })
            },
            removeDataById(id){
                this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
    }).then(() => {
         course.removeCourseById(id)
         .then((res) => {
        // this.fetchData()
        this.$message({
            type: 'success',
            message: '删除成功!'
        })
        this.getList(this.page)
        })
    })
    },
            resetData(){
                this.courseQuery = {
                    title:"",
                    status:""
                }
            },
            
}
    }
</script>