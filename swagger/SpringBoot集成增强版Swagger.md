# SpringBoot集成增强版Swagger（Knife4j）

[官网](https://doc.xiaominfo.com/knife4j/documentation/)

### Knife4j强大之处

* 提供比`bootstrap-ui`更漂亮的UI界面
* 集成方便，引入依赖少
* 生产环境禁用配置`knife4j.production=true`

* 支持`Markdown/Html/Word/OpenAPI/PDF`多种文档格式导出



##### 引入依赖

```xml
<!--knife4j start-->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-boot-starter</artifactId>
    <version>2.0.8</version>
</dependency>
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>20.0</version>
</dependency>
<!--knife4j end-->

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.16</version>
</dependency>
```

##### 配置文件yaml

```yaml
knife4j:
  # 开启增强配置，此配置可配置ignoreParameters忽略参数，接口排序等
  enable: true
  # 开启生产环境屏蔽
  production: false
  basic:
    enable: true
    # Basic认证用户名
    username: test
    # Basic认证密码
    password: 123
```



##### 配置类

* `@EnableSwagger2WebMvc`注解自动添加静态资源映射

```java
package com.jonny.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Configuration
@EnableSwagger2WebMvc
@EnableKnife4j
public class Knife4jConfig {

    public final static String splitor = ";";

    @Bean
    public Docket openApi() {
        List<Parameter> parameters = getParameters();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .globalOperationParameters(parameters)
                .select()
                .apis(basePackage("com.jonny.controller.api;com.jonny.controller"))
                .paths(PathSelectors.any())
                .build()
                .groupName("Open API")
                .securitySchemes(security());
    }

    @Bean
    public Docket manageApi() {
        List<Parameter> parameters = getParameters();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .globalOperationParameters(parameters)
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.jonny.system"))
                .paths(PathSelectors.ant("/manage/**"))
                .build()
                .groupName("后台管理")
                .securitySchemes(security());
    }

    /**
     *  全局请求头
     * @return
     */
    private List<Parameter> getParameters() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new ParameterBuilder()
                .name("Authorization")
                .description("token请求头：Bearer 44f0d54c-fc11-4743-88a8-f25ea5bd5f4a")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build());
        return parameters;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("后台管理系统")
                .version("1.0")
                .description("用户后台管理")
                .build();
    }

    private List<SecurityScheme> security() {
        return Lists.newArrayList(
                new ApiKey("Authorization", "Authorization", "header")
        );
    }

    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage)     {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(splitor)) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }
}
```

##### 实体类

```java
package com.jonny.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@ApiModel("用户")
@AllArgsConstructor
public class User {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;
}
```

##### 控制器

```java
package com.jonny.controller.system;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jonny.domain.User;
import com.jonny.domain.UserForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @ApiOperation("查询")
    @PostMapping("/get")
    public User list() {
        return new User("", "");
    }

    @ApiOperation("保存")
    @PostMapping("/save")
    @ApiOperationSupport(author = "张三", ignoreParameters = {"password1"})
    public User save(@RequestBody UserForm form) {
        System.out.println();
        return new User("", "");
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ApiOperationSupport(author = "张三", ignoreParameters = {"password1"})
    public User update(UserForm form) {
        System.out.println();
        return new User("", "");
    }
}
```

##### 启动后访问地址：http://localhost:port/doc.html

#### 注意

* `@ApiOperationSupport(author = "张三", ignoreParameters = {"password1"})`可以忽略指定属性，必须启用**增强模式**`knife4j.enable=true`

* 如果用了`knife4j`的`starter`，其他`swagger`相关的依赖最好全部去掉，只是用`starter`，不然可能会冲突，也可以值引用`knife4j`的ui依赖，具体参照官网

* 开发过程中踩到的坑，实体类的`@ApiModel("")`注解名称一定不要一样，不然可能会造成实体类属性修改了，界面上还没变的问题

* `ui`首页刷新报错

  > Illegal DefaultValue null for parameter type integer java.lang.NumberFormatException: For input string: ""

  该异常是由 `swagger` 引起的`swagger 版本 1.9.2`

  ```xml
  <!--  解决 Illegal DefaultValue null for parameter type integer    异常  -->
  <dependency>
      <groupId>io.swagger</groupId>
      <artifactId>swagger-annotations</artifactId>
      <version>1.5.21</version>
  </dependency>
  <dependency>
      <groupId>io.swagger</groupId>
      <artifactId>swagger-models</artifactId>
      <version>1.5.21</version>
  </dependency>
  ```

* 效果图

![image|690x338](https://springboot.io/uploads/default/optimized/2X/4/4d1886e8ad205ac1b64c037e91a88934924f6c28_2_1035x507.png) 

![image|690x338](https://springboot.io/uploads/default/optimized/2X/b/b82455c45dffa074614491c3fa0a05b87b53ca2c_2_1035x507.png)

![image|690x338](https://springboot.io/uploads/default/optimized/2X/b/bced615525fea9d4b575ca9b934d50a1f10e7efe_2_1035x507.png) 

## 1.context-path重复？

> knife4j `contextPath`重复，用`2.0.6`版本会有这个bug，升级到`2.0.7+`即可解决

## 2.接口排序

> 需要启用增强功能

```yaml
knife4j:
  enable: true
```

>  `@ApiSort`作用于控制器类上，越小越靠前；`@ApiOperationSupport(order = 1)`作用于控制器方法上，越小越靠前

```java
@ApiSort(1)
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController {
    
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "列表")
    @GetMapping("/list")
    public Object index() {
        return null;
    }
}
```

## 3.response无字段说明

* 返回类未声明范性
* 数据封装过程中做了转换，被转换类和目标类的属性不一致，如：通过json字符转换为实体类，原本属性为`name`, 目标类为`label`

