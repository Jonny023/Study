# Springboot JSR303(validation)数据校验

## 引入依赖

> `springboot从2.3`版本开始已经移除了`spring-boot-starter-web`中的`hibernate-validator`依赖， [参考](https://blog.csdn.net/not_say/article/details/107366789)

* 引用依赖也最好用`spring-boot-starter-validation`这种`starter`方式，减少依赖冲突

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

## 分组

* 创建两个`interface`用来区分`insert`和`update`操作，不需要实现

> 分组的作用：当新增和修改用同一个类的时候，若只想要某个字段在修改时才必传，新增时可不传，可以用分组实现

```java
package com.jonny.springbootjsr303.validation;

/**
 *  插入组
 */
public interface Insert {
}



/**
 *  更新组
 */
public interface Update {
}
```

## 创建实体类

```java
package com.jonny.springbootjsr303.form;

import com.jonny.springbootjsr303.validation.Insert;
import com.jonny.springbootjsr303.validation.Update;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class UserAddOrUpdateForm {

    @Min(value = 1, message = "主键格式不正确", groups = Update.class)
    @NotNull(message = "主键不能为空", groups = Update.class)
    private Long id;

    @NotEmpty(message = "用户名", groups = Insert.class)
    private String username;

    @NotEmpty(message = "昵称不能为空", groups = Insert.class)
    private String nickname;

    private String email;

}
```

## 控制器

### Java版

```java
package com.jonny.springbootjsr303.controller;

import com.jonny.springbootjsr303.form.UserAddOrUpdateForm;
import com.jonny.springbootjsr303.validation.Insert;
import com.jonny.springbootjsr303.validation.Update;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user1")
public class User1Controller {

    @PostMapping("/save")
    public UserAddOrUpdateForm save(@Validated(Insert.class) @RequestBody UserAddOrUpdateForm form) {
        return form;
    }

    @PostMapping("/update")
    public UserAddOrUpdateForm update(@Validated({Update.class}) @RequestBody UserAddOrUpdateForm form) {
        return form;
    }
}
```

### Groovy版

```java
package com.jonny.springbootjsr303.controller

import com.jonny.springbootjsr303.form.UserAddOrUpdateForm
import com.jonny.springbootjsr303.validation.Insert
import com.jonny.springbootjsr303.validation.Update
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {

    @PostMapping("/save")
    UserAddOrUpdateForm save(@Validated([Insert.class]) @RequestBody UserAddOrUpdateForm form) {
        return form
    }

    @PostMapping("/update")
    UserAddOrUpdateForm update(@Validated([Update.class, Insert.class]) @RequestBody UserAddOrUpdateForm form) {
        return form
    }
}
```

## 全局异常捕获

```java
package com.jonny.springbootjsr303.exception;

import com.jonny.springbootjsr303.base.Result;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception e){
        if (e instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
            //多个错误，取第一个
            FieldError error = fieldErrors.get(0);
            String msg = error.getDefaultMessage();
            return Result.validError(msg);
        }else {
            return Result.error(e.getMessage());
        }
    }
}
```





# 踩坑

* No.1
```bash
Groovyc: Annotation list attributes must use Groovy notation [el1, el2] in @org.springframework.validation.annotation.Validated
```

> 原因分析: `UserController`类我用的Groovy类，`{}`在Groovy有特殊用途，是作为闭包的，所以不能用下面这种方式

```groovy
@PostMapping("/update")
UserAddOrUpdateForm update(@Validated({Update.class}) @RequestBody UserAddOrUpdateForm form) {
    return form
}
```

换成下面这种

```groovy
@PostMapping("/update")
UserAddOrUpdateForm update(@Validated(Update.class) @RequestBody UserAddOrUpdateForm form) {
    return form
}
//或者
@PostMapping("/update")
UserAddOrUpdateForm update1(@Validated([Update.class]) @RequestBody UserAddOrUpdateForm form) {
    return form
}
```

## 注解无效

* `@Validated`注解无效
  
    * 实体类没被扫描到，之前把`@Valid`注解加到`interface`方法上，死活不生效
      * 注解可以加到interface，但是需要保证它能被扫描到
    * `@Validated(Update.class)`注解分组和实体类没对上，可能没加分组等
    
* 控制器的方法上添加`@NotBlank(message="不能为空")`无效

  ```java
  public String loging(@NotBlank(message="用户名不能为空") String username) {}
  ```

  > 注解没用被扫描到，需用在类上添加`@Validated`

## 日期校验yyyy-MM-dd

`@Pattern(message = "结束时间格式不正确，格式如：2021-09-26", regexp = "^$|(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)")`

## 复杂对象嵌套校验

> 实体类中`嵌套`其他对象、`静态内部类`时校验不生效

* 实体类需要在对象属性上方添加注解`@Valid`，List也一样需要添加`@Valid`

```java
@ApiModel("数据源-表结构数据")
public class StructureForm {

    @ApiModelProperty("数据源id")
    private String id;

    @Valid
    @ApiModelProperty("配置结构信息")
    private Structure structure;

    public static class Structure {

        public Structure() {}

        @Valid
        @ApiModelProperty("结构信息【数据列】")
        private List<Data> columns;

        public List<Data> getColumns() {
            return columns;
        }

        public void setColumns(List<Data> columns) {
            this.columns = columns;
        }
    }
}
```

* controller方法上添加`@Valid`或者类上添加`@Validated`

```java
@RestController
public class Main {
    
    @PostMapping("/save")
    public R save(@RequestBody @Valid StructureForm form) {
        return R.ok();
    }
}
```

## 枚举类校验

* 自定义注解类

```java
package com.validation.annotation;

import com.validation.validator.EnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EnumValidator.class})
@Documented
public @interface EnumValid {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?>[] target() default {};

    String method() default "ordinal";

    boolean required() default false;
}
```

* 自定义校验器

```java
package com.validation.validator;

import com.exception.OtherException;
import com.validation.annotation.EnumValid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class EnumValidator implements ConstraintValidator<EnumValid, Object> {

    private Logger LOG = LoggerFactory.getLogger(EnumValidator.class);

    private Class<?>[] clazz; //枚举类
    private String method; //枚举类方法

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        this.clazz = constraintAnnotation.target();
        this.method = constraintAnnotation.method();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (clazz.length > 0) {
            try {
                for (Class<?> cl : clazz) {
                    if (cl.isEnum()) {
                        for (Object obj : cl.getEnumConstants()) {
                            if (Objects.equals(value.toString(), cl.getMethod(method).invoke(obj))) {
                                return true;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error("枚举类校验异常", e);
                throw new OtherException("类型错误");
            }
        } else {
            return true;
        }
        return false;
    }
}
```

* 使用

> method是自定义枚举类的get方法的值，默认为：ordinal或name，如果有自定义的需要自己指定

```
@EnumValid(message = "性别类型错误", method = "name", target = Grender.class)
private Grender grender;
```

* 枚举类空字符串（enpty string）转换异常

```java
@Bean
public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true); //枚举类空字符串序列化
    return objectMapper;
}
```



# 其他

[aop拦截并记录错误1](https://blog.csdn.net/qq_36762677/article/details/83001451)

[aop拦截并记录错误2](https://segmentfault.com/a/1190000021082382)

[aop拦截并记录错误3](https://juejin.cn/post/6844904153672744967)