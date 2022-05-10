# Knife4j

[参考地址](https://gitbook.cn/books/5f313ec4564341656e4bf28a/index.html)

> 不分组

## pom.xml

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-boot-starter</artifactId>
    <version>2.0.9</version>
</dependency>
```

## yaml配置

```yaml
knife4j:
  # 开启增强配置，此配置可配置ignoreParameters忽略参数，接口排序等
  enable: true
```



## 配置类

```java
package com.example.demo.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
@EnableKnife4j
public class SwaggerConfig {
//public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public Docket defaultApi1() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //.groupName("2.X版本")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo.controller"))
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("demo系统")
                .description("xxxxxxxxx")
//                        .termsOfServiceUrl("http://www.xx.com/")
                .contact(new Contact("", "", ""))
                .version("1.0")
                .build();
    }

    //@Override
    //public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //    registry.addResourceHandler("/doc.html")
    //            .addResourceLocations("classpath:/META-INF/resources/");
    //    registry.addResourceHandler("/webjars/**")
    //            .addResourceLocations("classpath:/META-INF/resources/webjars/");
    //}
}
```

## 访问

> 访问doc.html

* http://localhost:port/doc.html



# 问题


* Knife4j文档请求异常，控制台app.js报错`Cannot read property 'id' of null`

> 可从以下方面着手
>
> 1.权限问题，如被拦截器或者项目权限框架（如：shiro/spring security）拦截了
>
> 2.静态资源路径不对，需要打开上面注释部分代码
>
> 3.项目配置文件种是否启用了swagger功能
>
> 4.可通过文章开头的参考地址，用`1.9.6`版本启动，访问后，再次切换回高版本（此文测试就是这样就能访问）
