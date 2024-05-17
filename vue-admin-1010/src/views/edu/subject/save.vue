<template>
      <div class="app-container">
        <el-form label-width="120px">
          <el-form-item label="信息描述">
            <el-tag type="info">excel模版说明</el-tag>
            <el-tag>
              <i class="el-icon-download"/>
              <a :href="OSS_PATH + '/excel/template.xlsx'">点击下载模版</a>
            </el-tag>
          </el-form-item>
          <el-form-item label="选择Excel">
            <el-upload
              ref="upload"
              :auto-upload="false"
              :on-success="fileUploadSuccess"
              :on-error="fileUploadError"
              :disabled="importBtnDisabled"
              :limit="1"
              :action="BASE_API+'/eduservice/subject/addSubject'"
              name="file"
              accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet">
              <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              <el-button
                :loading="loading"
                style="margin-left: 10px;"
                size="small"
                type="success"
                @click="submitUpload">{{ fileUploadBtnText }}</el-button>
            </el-upload>
          </el-form-item>
        </el-form>
      </div>
    </template>
    
    <script>
        export default{
            data(){
                return{
                    BASE_API: process.env.BASE_API, // 接口API地址
                    OSS_PATH: process.env.OSS_PATH, // 阿里云OSS地址
                    fileUploadBtnText: '上传到服务器', // 按钮文字
                    importBtnDisabled: false, // 选取文件按钮是否禁用,
                    loading: false
                }
            },
            methods:{
              //点击按钮上传文件到接口里面
              submitUpload(){
                if(this.$refs.upload.submit.length==0){
                  this.$message({
                  type:'error',
                  message:'不可以传输空文件'
                })
                }else{
                this.importBtnDisabled = true //
                this.loading  = true
                console.log('sssss:',this.$refs.upload)
                this.$refs.upload.submit()
                }
              },
              //文件上传成功方法
              fileUploadSuccess(){
                this.loading = false
                this.$message({
                  type:'success',
                  message:'已成功添加课程分类'
                })
                //跳转课程分类页
                //路由跳转
                this.$router.push({path:'/subject/list'}) 
              },
              //文件上传失败
              fileUploadError(){
                this.loading = false
                this.$message({
                  type:'error',
                  message:'会不会玩？不会玩让高手来'
                })
              
            }
            },
            created(){
                console.log(process.env)
            }
        
        }
    </script>