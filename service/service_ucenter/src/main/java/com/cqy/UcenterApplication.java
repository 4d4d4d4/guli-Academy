package com.cqy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableSwagger2
@MapperScan("com.cqy.educenter.mapper")
public class UcenterApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(UcenterApplication.class,args);
    }
}