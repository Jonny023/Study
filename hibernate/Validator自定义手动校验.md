# Validator自定义手动校验

* maven依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

* 自定义注解

```java
package cn.com.common.annotation;

import cn.com.common.validator.JsonValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @description:
 * @author: Jonny
 * @date: 2021/5/21 17:28
 * @version: 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {JsonValidator.class})
public @interface JsonRange {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
```

* 自定义校验器

```java
package cn.com.common.validator;

import cn.com.annotation.JsonRange;
import cn.com.common.utils.JsonUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @description: 自定义json校验器
 * @author: Jonny
 * @date: 2021/5/21 17:29
 * @version: 1.0.0
 */
public class JsonValidator implements ConstraintValidator<JsonRange, String> {
    @Override
    public void initialize(JsonRange constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }
        try {
            JsonUtils.toObject(s);
            return true;
        } catch (Exception e) {
            try {
                JsonUtils.toArray(s);
                return true;
            } catch (Exception arrException) {
                return false;
            }
        }
    }
}
```

* 校验工具类

```java
package cn.com.common;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author: Jonny
 * @description: 自定义校验工具类
 * @date:created in 2021/5/24 17:04
 * @modificed by:
 */
public class ValidatorUtil {

    private enum Validate {
        INSTANCE;
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     *  校验对象是否有不符合校验规则的属性
     *  true-校验通过
     *  false-校验未通过
     * @param object
     * @param <T>
     * @return
     */
    public static <T> boolean isValidate(T object) {
        Set<ConstraintViolation<T>> sets = Validate.INSTANCE.validator.validate(object);
        return !(sets.size() > 0);
    }
}
```

* 调用ValidatorUtil.isValidate()就能实现校验