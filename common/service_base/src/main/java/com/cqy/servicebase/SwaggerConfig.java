package com.cqy.servicebase;


import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration //配置类
@EnableSwagger2 //定义为Swagger
@Slf4j
public class SwaggerConfig {

    //Docket Bean主要用来配置Swagger文档的生成规则
    @Bean
    public Docket webApiConfig(){
        log.info("正在加载Swagger文档.....");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi") //设置分组名为webApi，用于区分不同的API文档。
                .apiInfo(webApiInfo())//设置API文档的基本信息，包括标题、描述、版本号和联系人信息。这里调用了webApiInfo()方法来获取ApiInfo对象。
                .select()  //选择要生成API文档的接口。
                .paths(Predicates.not(PathSelectors.regex("/admin/.*"))) //设置路径过滤规则，排除以/admin/开头的路径。
                .paths(Predicates.not(PathSelectors.regex("/error/.*")))
                .build();
    }

    private ApiInfo webApiInfo(){
        return new ApiInfoBuilder()
                .title("网站-课程中心api文档")
                .description("本文档描述了课程中心微服务的接口定义")
                .version("1.0")
                .contact(new Contact("Helen", "http://atguigu.com", "55317332@qq.com"))
                .build();
    }

    /*
     * localhost:port/swagger-ui.html 访问swagger文档
     */

}