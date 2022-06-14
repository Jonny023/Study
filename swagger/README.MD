# 问题笔记

* swagger实体类修改属性后，ui界面还是之前的属性
  * 原因分析：实体类的注解同名了，如：有两个@ApiModel("user")，需要设置为不同的值

* 集成Knife4j后启动报错

```bash
Parameter 0 of constructor in com.github.xiaoymin.knife4j.spring.plugin.DynamicResponseModelReader required a bean of type 'springfox.documentation.schema.TypeNameExtractor' that could not be found.


Action:

Consider defining a bean of type 'springfox.documentation.schema.TypeNameExtractor' in your configuration.
```

> 原因：项目用了多模块，多环境`@Profile()`，环境需要指定具体应用的配置
>
> `java -jar demo.jar --spring.profiles.active=dev,swagger`

### NPE空指针问题

> 不同的springboot版本路径匹配不同，高坂本用knife4j报错，则需要修改配置

```yaml
spring:
  mvc:
    pathmatch:
      matching-strategy: ANT_PATH_MATCHER
```
