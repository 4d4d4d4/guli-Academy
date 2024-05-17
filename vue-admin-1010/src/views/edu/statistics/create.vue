<template>
      <div class="app-container">
        <!--表单-->
        <el-form :inline="true" class="demo-form-inline">
          <el-form-item label="日期">
            <el-date-picker
              v-model="day"
              type="date"
              placeholder="选择要统计的日期"
              value-format="yyyy-MM-dd" />
          </el-form-item>
          <el-button
            :disabled="btnDisabled"
            type="primary"
            @click="create()">生成</el-button>
        </el-form>
        {{day}}
      </div>
</template>

<script>
    import staApi from '@/api/statistics/statistics'
    export default{
        data(){
            return{
                day: new Date().getFullYear()+'-'+(new Date().getMonth()+1)+'-'+new Date().getDay(), //要统计数据的时间
                btnDisabled:false
            }
        },
        methods:{
            init(){

            },
            create(){
            staApi.createDataByDay(this.day).then(res=>{
                this.$message({
                    type:'success',
                    message:'数据生成成功'
                })
                this.$router.push({path:"statistics/show"})
            })
        }
        },
    }
</script>