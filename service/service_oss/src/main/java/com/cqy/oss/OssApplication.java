package com.cqy.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//@ComponentScan(basePackages = {"org.cqy"})
@EnableDiscoveryClient  //开启nacos注册
@EnableSwagger2   //开启Swagger 表明swagger的初始地址
public class OssApplication
{

    public static void main( String[] args )
    {
        ConfigurableApplicationContext run = SpringApplication.run(OssApplication.class, args);
        //获取阿里云oss 登陆所需数据的存放工具类的bean
//        ConstantPropertiesUtils bean = run.getBean(ConstantPropertiesUtils.class);
//        System.out.println(bean.toString());
    }
}
