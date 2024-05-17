<template>
    <div class="app-container">
      <el-input v-model="filterText" placeholder="Filter keyword" style="margin-bottom:30px;" />
  
      <el-tree
        ref="tree2"
        :data="data2"
        :props="defaultProps"
        :filter-node-method="filterNode"
        class="filter-tree"
        default-expand-all
      />
  
    </div>
  </template>

  <script>
import subject from '@/api/subject/subject'

  export default {
  
    data() {
      return {
        filterText: '',
        data2: [],
        defaultProps: {
          children: 'children',
          label: 'title'
        }
      }
    },
    watch: {
      filterText(val) {
        this.$refs.tree2.filter(val)
      }
    },
  
    methods: {
      filterNode(value, data) {
        if (!value) return true
        return data.title.toLowerCase().indexOf(value) !== -1
      },

      //查询课程分类列表 （tree）
      getAllSubjectWithTree(){
        subject.getAllSubjectWithTree().then(res=>{
            console.log('初始的data::',this.data2)
            
            this.data2 = res.data.list
            console.log('现在的dataa:',this.data2)
        })
      }
    },
    created(){
        this.getAllSubjectWithTree()

    }
  }
  </script>
  
  