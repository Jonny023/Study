## 作用

* `@ConfigurationProperties` 的作用
  + 读取配置文件，将配置文件中的值封装为JavaBean对象
* `@Value` 的作用
  + 读取配置文件中某项值

## 区别

| 比较                 | @ConfigurationProperties | @Value       |
| :------------------- | :----------------------- | :----------- |
| 功能                 | 批量注入配置文件中的属性 | 一个一个指定 |
| 松散绑定（松散语法） | *支持*                   | `不支持`     |
| SpEL                 | `不支持`                 | *支持*       |
| JSR303数据校验       | 支持                     | `不支持`     |
| 复杂类型封装         | 支持                     | `不支持`     |

### @ConfigurationProperties的松散绑定（松散语法）

> 下面的格式是指在配置文件中定义的时候用

- `user.firstName`  标准方式
- `user.first-name`  第二个大写字母用-
- `user.first_name`  第二个大写字母用_
- `user.FIRST_NAME`

### JSR303数据校验

* 类上加`@Validated`注解

* 属性上加入对应校验规则，如`@Email` 表示次字段必须为邮箱

  ```java
  @Component
  @Validated
  @ConfigurationProperties(prefix = "user")
  public class User {
  
      @Email
      private String email;
  }
  ```

## @Value简单用法及SpEL表达式

#### 用法

* 简单用法
  + `@Value("true")`

* ${} - 获取配置文件中的属性值
  + `@Value("${user.name}")`
* \#{} - 可传入参数、简单数据类型、获取系统配置文件中的属性的(逻辑运算)
  + `@Value("#{20 * 2}")`
  + `@Value("#{user.name}")`

# 总结

* 如果只需要获取配置文件中某一项值用`@Value`
* 如果要将配置文件映射到JavaBean用`@ConfigurationProperties` 
