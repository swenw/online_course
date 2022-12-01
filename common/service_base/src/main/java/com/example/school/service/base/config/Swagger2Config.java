package com.example.school.service.base.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.function.Predicate;

@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select() //进行路径展示，主要展示路径开头为/api的接口
                .paths(Predicates.and(PathSelectors.regex("/api/.*")))
                .build();
    }

    @Bean
    public Docket adminApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("adminApi")
                .apiInfo(adminApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .build();
    }

    // 在Swagger文档顶头进行展示的
    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("网站的API文档")
                .description("本文档描述了在线教育平台的api接口定义")
                .version("1.0")
                .contact(new Contact("SWenW", "https://swenw.github.io/", "swenwa@163.com"))
                .build();
    }

    private ApiInfo adminApiInfo() {
        return new ApiInfoBuilder()
                .title("网站的API文档")
                .description("本文档描述了在线教育平台的后台api接口定义")
                .version("1.0")
                .contact(new Contact("SWenW", "https://swenw.github.io/", "swenwa@163.com"))
                .build();
    }
}
