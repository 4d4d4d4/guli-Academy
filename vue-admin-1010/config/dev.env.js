'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  OSS_PATH: '"https://cqy521.oss-cn-beijing.aliyuncs.com/"',
 // BASE_API: '"https://easy-mock.com/mock/5950a2419adc231f356a6636/vue-admin"',
  //gateway网关的监听端口
  BASE_API: '"http://localhost:8222"',
  // 设置的是nginx监听的端口 可以进行请求转发
  // BASE_API: '"http://localhost:9001"',
})
