# mybatis-plus常用

## 配置

```yaml
mybatis-plus:
  configuration: # MyBatis 原生支持的配置
    # 是否将sql打打印到控制面板
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    # 是否开启自动驼峰命名规则（camel case）映射
    mapUnderscoreToCamelCase: true
```

## 注解

```java
//允许字段设置null值（将原先的值置空为null）
@TableField(strategy=FieldStrategy.IGNORED)

//主键NONE,AUTO-自增,ASSIGN_ID-雪花算法
@TableId(value = "id", type = IdType.NONE)
private String id;

//非持久化
@TableField(exist=false)

//表名
@TableName(value = "user")
```
