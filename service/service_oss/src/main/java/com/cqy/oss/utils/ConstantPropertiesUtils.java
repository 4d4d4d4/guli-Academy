package com.cqy.oss.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun.oss.file")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConstantPropertiesUtils implements InitializingBean {//继承这个接口可以实现 在启动项目时候会加载数据并执行这个接口中的实现方法

    //读取配置文件内容
    private String endpoint; //地域节点
    private String keyId; //阿里云账号id
    private String keySecret; //阿里云密钥
    private String bucketName; //bucket名称

    //定义公开静态常量
    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;
    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT = endpoint;
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
    }
}
